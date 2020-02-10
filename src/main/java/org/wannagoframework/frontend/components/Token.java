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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import org.wannagoframework.frontend.layout.size.Left;
import org.wannagoframework.frontend.layout.size.Right;
import org.wannagoframework.frontend.utils.FontSize;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.TextColor;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.css.BorderRadius;
import org.wannagoframework.frontend.utils.css.Display;

public class Token extends FlexBoxLayout {

  private final String CLASS_NAME = "token";

  public Token(String text) {
    setAlignItems(FlexComponent.Alignment.CENTER);
    setBackgroundColor(LumoStyles.Color.Primary._10);
    setBorderRadius(BorderRadius.M);
    setClassName(CLASS_NAME);
    setDisplay(Display.INLINE_FLEX);
    setPadding(Left.S, Right.XS);
    setSpacing(Right.XS);

    Label label = UIUtils.createLabel(FontSize.S, TextColor.BODY, text);
    Button button = UIUtils.createButton(VaadinIcon.CLOSE_SMALL,
        ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY_INLINE);
    add(label, button);
  }

}
