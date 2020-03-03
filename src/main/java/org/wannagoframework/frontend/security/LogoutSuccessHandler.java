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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.frontend.client.audit.SessionService;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-05-24
 */
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements HasLogger {

  private final SessionService sessionService;

  public LogoutSuccessHandler(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    String loggerPrefix = getLoggerPrefix("onLogoutSuccess");
    logger().debug(loggerPrefix + "Logout !");
    super.onLogoutSuccess(request, response, authentication);
  }
}
