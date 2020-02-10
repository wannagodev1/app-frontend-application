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
import com.vaadin.flow.shared.Registration;
import org.wannagoframework.frontend.components.FlexBoxLayout;
import org.wannagoframework.frontend.layout.size.Horizontal;
import org.wannagoframework.frontend.layout.size.Right;
import org.wannagoframework.frontend.layout.size.Vertical;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;

public class PlacesAdminDetailsDrawerFooter extends FlexBoxLayout {

  private final Button create;
  private final Button cancel;

  public PlacesAdminDetailsDrawerFooter() {
    setBackgroundColor(LumoStyles.Color.Contrast._5);
    setPadding(Horizontal.RESPONSIVE_L, Vertical.S);
    setSpacing(Right.S);
    setWidthFull();

    create = UIUtils.createPrimaryButton(getTranslation("action.global.create"));
    cancel = UIUtils.createTertiaryButton(getTranslation("action.global.cancel"));

    add(create, cancel);
  }

  public Registration addCreateListener(
      ComponentEventListener<ClickEvent<Button>> listener) {
    return create.addClickListener(listener);
  }

  public Registration addCancelListener(
      ComponentEventListener<ClickEvent<Button>> listener) {
    return cancel.addClickListener(listener);
  }

  public void setCreateButtonVisible(boolean isVisible) {
    create.setVisible(isVisible);
  }

  public void setCancelButtonVisible(boolean isVisible) {
    cancel.setVisible(isVisible);
  }
}
