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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.wannagoframework.dto.domain.security.SecurityUser;
import org.wannagoframework.dto.messageQueue.NewSession;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.frontend.client.audit.AuditServiceQueue;
import org.wannagoframework.frontend.client.security.SecurityUserService;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-05-01
 */
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private final SecurityUserService securityUserService;
  private final AuditServiceQueue auditServiceQueue;

  public AuthenticationSuccessHandler(
      SecurityUserService securityUserService, AuditServiceQueue auditServiceQueue) {
    this.securityUserService = securityUserService;
    this.auditServiceQueue = auditServiceQueue;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
      HttpServletResponse response, Authentication authentication)
      throws ServletException, IOException {
    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
    securityUser.setLastSuccessfulLogin(Instant.now());
    securityUser.setIsAccountLocked(false);
    securityUser.setFailedLoginAttempts(0);
    securityUserService.save(new SaveQuery<>(securityUser));

    auditServiceQueue.newSession(
        new NewSession(request.getRequestedSessionId(), securityUser.getUsername(), null,
            Instant.now(), true, null));
    super.onAuthenticationSuccess(request, response, authentication);
  }
}