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

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import org.wannagoframework.frontend.utils.AppConst;

/**
 * Interface for views showing notifications to users
 *
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-06
 */
public interface HasNotifications extends HasElement {

  default void showNotification(String message) {
    showNotification(message, false);
  }

  default void showNotification(String message, boolean persistent) {
    if (persistent) {
      Button close = new Button("Close");
      close.getElement().setAttribute("theme", "tertiary small error");
      Notification notification = new Notification(new Text(message), close);
      notification.setPosition(Position.BOTTOM_START);
      notification.setDuration(0);
      close.addClickListener(event -> notification.close());
      notification.open();
    } else {
      Notification.show(message, AppConst.NOTIFICATION_DURATION, Position.BOTTOM_STRETCH);
    }
  }
}
