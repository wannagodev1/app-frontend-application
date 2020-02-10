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


package org.wannagoframework.frontend.views;

import com.vaadin.flow.spring.SpringServlet;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wannagoframework.frontend.views.login.LoginView;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-07-01
 */
@Component
public class ViewManager {

  public static ApplicationContext getApplicationContext() {
    ServletContext servletContext = SpringServlet.getCurrent().getServletContext();
    return WebApplicationContextUtils.getWebApplicationContext(servletContext);
  }

  public static LoginView getLoginView() {
    return getApplicationContext().getBean(LoginView.class);
  }
}
