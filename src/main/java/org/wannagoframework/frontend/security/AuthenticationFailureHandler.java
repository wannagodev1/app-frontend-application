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

import java.io.IOException;
import java.time.Instant;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.wannagoframework.dto.domain.security.SecurityUser;
import org.wannagoframework.dto.messageQueue.NewSession;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.serviceQuery.security.securityUser.GetSecurityUserByUsernameQuery;
import org.wannagoframework.frontend.client.audit.AuditServices;
import org.wannagoframework.frontend.client.audit.SessionService;
import org.wannagoframework.frontend.client.security.SecurityUserService;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-05-01
 */
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final SecurityUserService securityUserService;
  private final SessionService sessionService;

  public AuthenticationFailureHandler(String failureUrl,
      SecurityUserService securityUserService,
      SessionService sessionService) {
    super(failureUrl);
    this.sessionService = sessionService;
    setUseForward(true);
    this.securityUserService = securityUserService;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    String username = request.getParameter("username");

    if (exception instanceof BadCredentialsException) {
      SecurityUser securityUser = securityUserService
          .getSecurityUserByUsername(new GetSecurityUserByUsernameQuery(username)).getData();
      if (securityUser != null) {
        securityUser.setFailedLoginAttempts(securityUser.getFailedLoginAttempts() + 1);
        if (securityUser.getFailedLoginAttempts() > 4) {
          securityUser.setIsAccountLocked(true);
        }
        securityUserService.save(new SaveQuery<>(securityUser));
      }
    }
    AuditServices.getAuditServiceQueue()
        .newSession(new NewSession(request.getRequestedSessionId(), username, null,
            Instant.now(), false, exception.getLocalizedMessage()));

    if (request.getSession() != null) {
      request.changeSessionId();
    }
    super.onAuthenticationFailure(request, response, exception);
  }
}
