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


package org.wannagoframework.frontend.components.navigation.drawer;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import org.wannagoframework.frontend.utils.UIUtils;

@CssImport("./styles/components/brand-expression.css")
public class BrandExpression extends Div {

  private String CLASS_NAME = "brand-expression";

  private Image logo;
  private Label title;

  public BrandExpression(String text) {
    setClassName(CLASS_NAME);

    logo = new Image(UIUtils.IMG_PATH + "logo.png", "");
    logo.setAlt(text + " logo");
    logo.setClassName(CLASS_NAME + "__logo");

    title = UIUtils.createH3Label(text);
    title.addClassName(CLASS_NAME + "__title");

    add(logo, title);
  }

}
