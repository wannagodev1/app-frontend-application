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


package org.wannagoframework.frontend.components.card;

import com.github.appreciated.card.content.HorizontalCardComponentContainer;
import com.github.appreciated.card.label.PrimaryLabelComponent;
import com.vaadin.flow.component.Component;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-07-13
 */
public class PostItem extends
    HorizontalCardComponentContainer<com.github.appreciated.card.content.Item> {

  private Component component;

  public PostItem(String title, String description) {
    component = new PostItemBody(title, description);
    ((PostItemBody) component).setPadding(false);
    add(component);
  }

  public PostItem withWhiteSpaceNoWrap() {
    if (component instanceof PrimaryLabelComponent) {
      ((PrimaryLabelComponent) component).setWhiteSpaceNoWrap();
    } else if (component instanceof PostItemBody) {
      ((PostItemBody) component).withWhiteSpaceNoWrap();
    }
    return this;
  }

}
