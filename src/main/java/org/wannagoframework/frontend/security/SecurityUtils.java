/*
 * This file is part of the WannaGo distribution (https://github.com/wannago).
 * Copyright (c) [2019] - [2020].
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */


package org.wannagoframework.frontend.security;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import com.vaadin.flow.server.ServletHelper.RequestType;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.ApplicationConstants;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.wannagoframework.dto.domain.security.RememberMeToken;
import org.wannagoframework.dto.domain.security.SecurityUser;
import org.wannagoframework.dto.messageQueue.EndSession;
import org.wannagoframework.dto.messageQueue.NewSession;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.serviceQuery.authentification.ClearRememberMeTokenQuery;
import org.wannagoframework.dto.serviceQuery.authentification.CreateRememberMeTokenQuery;
import org.wannagoframework.dto.serviceQuery.authentification.GetSecurityUserByRememberMeTokenQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.frontend.annotations.PublicView;
import org.wannagoframework.frontend.client.BaseServices;
import org.wannagoframework.frontend.client.audit.AuditServices;
import org.wannagoframework.frontend.client.security.SecurityServices;

/**
 * SecurityUtils takes care of all such static operations that have to do with security and querying
 * rights from different beans of the UI.
 *
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-26
 */
public final class SecurityUtils {

  public static final String SESSION_USERNAME = "username";
  private static final String COOKIE_NAME = "remember-me";


  private SecurityUtils() {
    // Util methods only
  }

  /**
   * Gets the user name of the currently signed in user.
   *
   * @return the user name of the current user or <code>null</code> if the user has not signed in
   */
  public static String getUsername() {
    SecurityContext context = SecurityContextHolder.getContext();
    if (!isUserLoggedIn()) {
      return "Anonymous";
    }
    Object principal = context.getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
      return userDetails.getUsername();
    }
    // Anonymous or no authentication.
    return "Anonymous";
  }

  public static SecurityUser getSecurityUser() {
    SecurityContext context = SecurityContextHolder.getContext();
    if (!isUserLoggedIn()) {
      return null;
    }
    Object principal = context.getAuthentication().getPrincipal();
    if (principal instanceof SecurityUser) {
      return (SecurityUser) principal;
    }
    // Anonymous or no authentication.
    return null;
  }

  /**
   * Checks if access is granted for the current user for the given secured view, defined by the
   * view class.
   *
   * @param viewClass View class
   * @return true if access is granted, false otherwise.
   */
  public static boolean isAccessGranted(Class<?> viewClass) {
    // Always allow access to public views
    PublicView publicView = AnnotationUtils.findAnnotation(viewClass, PublicView.class);
    if (publicView != null) {
      return true;
    }

    Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

    // All other views require authentication
    if (!isUserLoggedIn(userAuthentication)) {
      return false;
    }

    // Allow if no roles are required.
    Secured secured = AnnotationUtils.findAnnotation(viewClass, Secured.class);
    if (secured == null) {
      return true;
    }

    List<String> allowedRoles = Arrays.asList(secured.value());
    return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .anyMatch(allowedRoles::contains);
  }

  public static boolean hasRole(String role) {
    Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

    // All other views require authentication
    if (!isUserLoggedIn(userAuthentication)) {
      return false;
    }

    return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .anyMatch(s -> s.equals(role));
  }

  /**
   * Checks if the user is logged in.
   *
   * @return true if the user is logged in. False otherwise.
   */
  public static boolean isUserLoggedIn() {
    return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
  }

  private static boolean isUserLoggedIn(Authentication authentication) {
    return (authentication != null
        && !(authentication instanceof AnonymousAuthenticationToken)) || loginRememberedUser();
  }

  /**
   * Tests if the request is an internal framework request. The test consists of checking if the
   * request parameter is present and if its value is consistent with any of the request types
   * know.
   *
   * @param request {@link HttpServletRequest}
   * @return true if is an internal framework request. False otherwise.
   */
  static public boolean isFrameworkInternalRequest(HttpServletRequest request) {
    final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
    return parameterValue != null
        && Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
  }

  public static void newSession(SecurityUser securityUser, boolean rememberMe) {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(securityUser, null,
            securityUser.getAuthorities()));

    VaadinSession.getCurrent().getSession()
        .setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

    securityUser.setLastSuccessfulLogin(Instant.now());
    securityUser.setIsAccountLocked(false);
    securityUser.setFailedLoginAttempts(0);
    SecurityServices.getSecurityUserService().save(new SaveQuery(securityUser));

    AuditServices.getAuditServiceQueue().newSession(
        new NewSession(VaadinRequest.getCurrent().getWrappedSession().getId(),
            securityUser.getUsername(), VaadinRequest.getCurrent().getRemoteAddr(), Instant.now(),
            true, null));

    if (rememberMe) {
      ServiceResult<RememberMeToken> _rememberMeToken = BaseServices
          .getAuthService()
          .createRememberMeToken(new CreateRememberMeTokenQuery(securityUser.getId()));

      if (_rememberMeToken.getIsSuccess()) {
        RememberMeToken rememberMeToken = _rememberMeToken.getData();

        Cookie cookie = new Cookie(COOKIE_NAME, rememberMeToken.getToken());
        cookie.setPath("/");
        cookie.setMaxAge(
            (int) (rememberMeToken.getExpiryDate().getTime() - System.currentTimeMillis() * 1000));
        cookie.setHttpOnly(true);
        VaadinService.getCurrentResponse().addCookie(cookie);
      }
    }
  }

  public static void endSession(String jSessionId) {
    Optional<Cookie> cookie = getRememberMeCookie();
    if (cookie.isPresent()) {
      String id = cookie.get().getValue();
      BaseServices.getAuthService().clearRememberMeToken(new ClearRememberMeTokenQuery(id));
      deleteRememberMeCookie(cookie.get());
    }
    AuditServices.getAuditServiceQueue().endSession(new EndSession(jSessionId, Instant.now()));

    VaadinSession.getCurrent().close();
  }

  private static Optional<Cookie> getRememberMeCookie() {
    Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
    if (cookies != null) {
      return Arrays.stream(cookies).filter(c -> c.getName().equals(COOKIE_NAME)).findFirst();
    }

    return Optional.empty();
  }

  private static boolean loginRememberedUser() {
    Optional<Cookie> rememberMeCookie = getRememberMeCookie();

    if (rememberMeCookie.isPresent()) {
      String id = rememberMeCookie.get().getValue();
      ServiceResult<SecurityUser> _securityUser = BaseServices.getAuthService()
          .getSecurityUserByRememberMeToken(new GetSecurityUserByRememberMeTokenQuery(id));

      if (_securityUser != null && _securityUser.getIsSuccess()) {
        newSession(_securityUser.getData(), true);
        return true;
      } else {
        deleteRememberMeCookie(rememberMeCookie.get());
      }
    }

    return false;
  }

  private static void deleteRememberMeCookie(Cookie existing) {
    Cookie cookie = new Cookie(existing.getName(), (String) null);
    cookie.setPath(existing.getPath());
    cookie.setDomain(existing.getDomain());
    cookie.setMaxAge(0);
    VaadinService.getCurrentResponse().addCookie(cookie);
  }
}
