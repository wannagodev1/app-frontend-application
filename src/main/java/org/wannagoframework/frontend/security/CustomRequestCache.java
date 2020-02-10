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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

/**
 * HttpSessionRequestCache that avoids saving internal framework requests.
 *
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-26
 */
public class CustomRequestCache extends HttpSessionRequestCache {

  /**
   * {@inheritDoc}
   *
   * If the method is considered an internal request from the framework, we skip saving it.
   *
   * @see SecurityUtils#isFrameworkInternalRequest(HttpServletRequest)
   */
  @Override
  public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
    if (!SecurityUtils.isFrameworkInternalRequest(request)) {
      super.saveRequest(request, response);
    }
  }

}