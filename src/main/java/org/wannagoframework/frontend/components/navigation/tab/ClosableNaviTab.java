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


package org.wannagoframework.frontend.components.navigation.tab;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.wannagoframework.frontend.utils.FontSize;
import org.wannagoframework.frontend.utils.UIUtils;

public class ClosableNaviTab extends NaviTab {

  private final Button close;

  public ClosableNaviTab(String label,
      Class<? extends Component> navigationTarget) {
    super(label, navigationTarget);
    getElement().setAttribute("closable", true);

    close = UIUtils.createButton(VaadinIcon.CLOSE, ButtonVariant.LUMO_TERTIARY_INLINE);
    // ButtonVariant.LUMO_SMALL isn't small enough.
    UIUtils.setFontSize(FontSize.XXS, close);
    add(close);
  }

  public Button getCloseButton() {
    return close;
  }
}
