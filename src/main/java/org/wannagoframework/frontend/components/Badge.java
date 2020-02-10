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


package org.wannagoframework.frontend.components;

import com.vaadin.flow.component.html.Span;
import java.util.StringJoiner;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.css.lumo.BadgeColor;
import org.wannagoframework.frontend.utils.css.lumo.BadgeShape;
import org.wannagoframework.frontend.utils.css.lumo.BadgeSize;

public class Badge extends Span {

  public Badge(String text) {
    this(text, BadgeColor.NORMAL);
  }

  public Badge(String text, BadgeColor color) {
    super(text);
    UIUtils.setTheme(color.getThemeName(), this);
  }

  public Badge(String text, BadgeColor color, BadgeSize size,
      BadgeShape shape) {
    super(text);
    StringJoiner joiner = new StringJoiner(" ");
    joiner.add(color.getThemeName());

    if (shape.equals(BadgeShape.PILL)) {
      joiner.add(shape.getThemeName());
    }

    if (size.equals(BadgeSize.S)) {
      joiner.add(size.getThemeName());
    }
    UIUtils.setTheme(joiner.toString(), this);
  }

}
