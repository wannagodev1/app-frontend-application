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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import org.wannagoframework.frontend.components.FlexBoxLayout;
import org.wannagoframework.frontend.utils.css.FlexDirection;

@CssImport("./styles/components/details-drawer.css")
public class DetailsDrawer extends FlexBoxLayout {

  private String CLASS_NAME = "details-drawer";

  private FlexBoxLayout header;
  private FlexBoxLayout content;
  private FlexBoxLayout footer;

  public DetailsDrawer(Component... components) {
    this(null, components);
  }

  public DetailsDrawer(Position position, Component... components) {
    setClassName(CLASS_NAME);
    if (position != null) {
      setPosition(position);
    }

    header = new FlexBoxLayout();
    header.setClassName(CLASS_NAME + "__header");

    content = new FlexBoxLayout(components);
    content.setClassName(CLASS_NAME + "__content");
    content.setFlexDirection(FlexDirection.COLUMN);

    footer = new FlexBoxLayout();
    footer.setClassName(CLASS_NAME + "__footer");

    add(header, content, footer);
  }

  public FlexBoxLayout getHeader() {
    return this.header;
  }

  public void setHeader(Component... components) {
    this.header.removeAll();
    this.header.add(components);
  }

  public void setContent(Component... components) {
    this.content.removeAll();
    this.content.add(components);
  }

  public void setFooter(Component... components) {
    this.footer.removeAll();
    this.footer.add(components);
  }

  public void setPosition(Position position) {
    getElement().setAttribute("position", position.name().toLowerCase());
  }

  public void hide() {
    getElement().setAttribute("open", false);
  }

  public void show() {
    getElement().setAttribute("open", true);
  }

  public enum Position {
    BOTTOM, RIGHT
  }

}
