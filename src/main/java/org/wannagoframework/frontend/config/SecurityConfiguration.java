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


package org.wannagoframework.frontend.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.wannagoframework.dto.domain.security.SecurityUser;
import org.wannagoframework.dto.serviceQuery.security.securityUser.GetSecurityUserByUsernameQuery;
import org.wannagoframework.frontend.client.audit.AuditServiceQueue;
import org.wannagoframework.frontend.client.audit.SessionService;
import org.wannagoframework.frontend.client.security.SecurityRoleService;
import org.wannagoframework.frontend.client.security.SecurityUserService;
import org.wannagoframework.frontend.security.AuthenticationFailureHandler;
import org.wannagoframework.frontend.security.AuthenticationSuccessHandler;
import org.wannagoframework.frontend.security.CurrentUser;
import org.wannagoframework.frontend.security.CustomRequestCache;
import org.wannagoframework.frontend.security.LogoutSuccessHandler;
import org.wannagoframework.frontend.security.SecurityUtils;
import org.wannagoframework.frontend.security.UserDetailsServiceImpl;
import org.wannagoframework.frontend.security.WannaPlayAccessDecisionVoter;
import org.wannagoframework.frontend.utils.AppConst;

/**
 * Configures spring security, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form,</li>
 * <li>Configures the {@link UserDetailsServiceImpl}.</li>
 *
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-26
 */
@EnableWebSecurity
@Configuration
@Order(100)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String LOGIN_PROCESSING_URL = "/login";
  private static final String LOGIN_FAILURE_URL = "/login?error";
  private static final String LOGIN_URL = "/login";
  private static final String LOGOUT_SUCCESS_URL = "/";

  @Autowired
  private SecurityRoleService securityRoleService;

  @Autowired
  private SecurityUserService securityUserService;

  @Autowired
  private SessionService sessionService;

  @Autowired
  private AuditServiceQueue auditServiceQueue;

  /**
   * The password encoder to use when encrypting passwords.
   */
  /*
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
*/
  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public CurrentUser currentUser() {
    final String username = SecurityUtils.getUsername();
    SecurityUser user =
        username != null ? securityUserService
            .getSecurityUserByUsername(new GetSecurityUserByUsernameQuery(username)).getData() :
            null;
    return () -> user;
  }

  /**
   * Require login to access internal pages and configure login form.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Not using Spring CSRF here to be able to use plain HTML for the login page
    http.csrf().disable()

        // Register our CustomRequestCache, that saves unauthorized access attempts, so
        // the user is redirected after login.
        .requestCache().requestCache(new CustomRequestCache())

        // Restrict access to our application.
        .and().authorizeRequests()
        .antMatchers("/ping").permitAll()
        // Allow all flow internal requests.
        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

        // Public access to Places
        .antMatchers("/", "/places").permitAll()

        // Allow all requests by logged in users.
        .accessDecisionManager(accessDecisionManager())

        // Configure the login page.
        .and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
        .failureHandler(new AuthenticationFailureHandler(LOGIN_FAILURE_URL, securityUserService,
            sessionService))
        .successHandler(new AuthenticationSuccessHandler(securityUserService, auditServiceQueue))
        .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
        // Configure logout
        .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL)
        .logoutSuccessHandler(new LogoutSuccessHandler(sessionService));
  }

  @Bean
  public AccessDecisionManager accessDecisionManager() {
    List<AccessDecisionVoter<?>> decisionVoters
        = Arrays.asList(
        new WebExpressionVoter(),
        new RoleVoter(),
        new AuthenticatedVoter(),
        new WannaPlayAccessDecisionVoter(securityRoleService));
    return new UnanimousBased(decisionVoters);
  }

  /**
   * Allows access to static resources, bypassing Spring security.
   */
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
        // Vaadin Flow static resources
        "/VAADIN/**",
        // the standard favicon URI
        "/favicon.ico",

        // the robots exclusion standard
        "/robots.txt",

        // web application manifest
        "/manifest.webmanifest",
        "/sw.js",
        "/offline-page.html",

        // icons and images
        "/icons/**",
        "/images/**",

        // (development mode) static resources
        "/frontend/**",

        // (development mode) webjars
        "/webjars/**",

        // (development mode) H2 debugging console
        "/h2-console/**",

        // (production mode) static resources
        "/frontend-es5/**", "/frontend-es6/**");
  }
}
