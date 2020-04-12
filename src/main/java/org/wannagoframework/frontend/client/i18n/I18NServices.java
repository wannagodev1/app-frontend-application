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


package org.wannagoframework.frontend.client.i18n;

import com.vaadin.flow.spring.SpringServlet;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-02
 */
@Service
public class I18NServices {

  public static I18NService getI18NService() {
    return getApplicationContext().getBean(I18NService.class);
  }

  public static ActionService getActionService() {
    return getApplicationContext().getBean(ActionService.class);
  }

  public static ActionTrlService getActionTrlService() {
    return getApplicationContext().getBean(ActionTrlService.class);
  }

  public static ElementService getElementService() {
    return getApplicationContext().getBean(ElementService.class);
  }

  public static ElementTrlService getElementTrlService() {
    return getApplicationContext().getBean(ElementTrlService.class);
  }

  public static MessageService getMessageService() {
    return getApplicationContext().getBean(MessageService.class);
  }

  public static MessageTrlService getMessageTrlService() {
    return getApplicationContext().getBean(MessageTrlService.class);
  }

  public static ApplicationContext getApplicationContext() {
    ServletContext servletContext = SpringServlet.getCurrent().getServletContext();
    return WebApplicationContextUtils.getWebApplicationContext(servletContext);
  }
}
