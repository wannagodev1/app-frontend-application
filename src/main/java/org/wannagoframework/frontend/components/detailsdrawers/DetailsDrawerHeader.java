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


package org.wannagoframework.frontend.components.detailsdrawers;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.tabs.Tabs;
import org.wannagoframework.frontend.components.FlexBoxLayout;
import org.wannagoframework.frontend.layout.size.Horizontal;
import org.wannagoframework.frontend.layout.size.Right;
import org.wannagoframework.frontend.layout.size.Vertical;
import org.wannagoframework.frontend.utils.BoxShadowBorders;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.css.FlexDirection;


public class DetailsDrawerHeader extends FlexBoxLayout {

  private Button close;
  private Label title;

  public DetailsDrawerHeader(String title, boolean showClose, boolean showTitle) {
    addClassName(BoxShadowBorders.BOTTOM);
    setFlexDirection(FlexDirection.COLUMN);
    setWidthFull();

    if (showClose) {
      this.close = UIUtils.createTertiaryInlineButton(VaadinIcon.CLOSE);
      UIUtils.setLineHeight("1", this.close);
    }

    if (showTitle) {
      this.title = UIUtils.createH4Label(title);
    }

    FlexBoxLayout wrapper;
    if (showClose & showTitle) {
      wrapper = new FlexBoxLayout(this.close, this.title);
    } else if (showTitle) {
      wrapper = new FlexBoxLayout(this.title);
    } else if (showClose) {
      wrapper = new FlexBoxLayout(this.close);
    } else {
      return;
    }

    wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
    wrapper.setPadding(Horizontal.RESPONSIVE_L, Vertical.M);
    wrapper.setSpacing(Right.L);
    add(wrapper);
  }

  public DetailsDrawerHeader(String title) {
    this(title, true, true);
  }

  public DetailsDrawerHeader(String title, Tabs tabs) {
    this(title, true, true);
    add(tabs);
  }

  public DetailsDrawerHeader(String title, Tabs tabs, boolean showClose, boolean showTitle) {
    this(title, showClose, showTitle);
    add(tabs);
  }

  public void setTitle(String title) {
    this.title.setText(title);
  }

  public void addCloseListener(ComponentEventListener<ClickEvent<Button>> listener) {
    this.close.addClickListener(listener);
  }

}
