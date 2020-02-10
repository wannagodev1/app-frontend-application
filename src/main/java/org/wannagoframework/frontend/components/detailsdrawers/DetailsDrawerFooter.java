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
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import org.wannagoframework.frontend.components.FlexBoxLayout;
import org.wannagoframework.frontend.layout.size.Horizontal;
import org.wannagoframework.frontend.layout.size.Right;
import org.wannagoframework.frontend.layout.size.Vertical;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;

public class DetailsDrawerFooter extends FlexBoxLayout {

  private final Button save;
  private final Button cancel;
  private final Button delete;

  public DetailsDrawerFooter() {
    setBackgroundColor(LumoStyles.Color.Contrast._5);
    setPadding(Horizontal.RESPONSIVE_L, Vertical.S);
    setSpacing(Right.S);
    setWidthFull();

    save = UIUtils.createPrimaryButton(getTranslation("action.global.save"));
    cancel = UIUtils.createTertiaryButton(getTranslation("action.global.cancel"));
    delete = UIUtils.createTertiaryButton(getTranslation("action.global.delete"));
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    HorizontalLayout leftButton = new HorizontalLayout();
    leftButton.add(delete);
    leftButton.setDefaultVerticalComponentAlignment(Alignment.START);

    HorizontalLayout rightButtons = new HorizontalLayout();
    rightButtons.add(cancel, save);
    rightButtons.setDefaultVerticalComponentAlignment(Alignment.END);
    setJustifyContent("space-between");
    add(leftButton, rightButtons);
  }

  public void setSaveButtonTitle(String title) {
    save.setText(title);
  }

  public void setCancelButtonTitle(String title) {
    cancel.setText(title);
  }

  public void setDeleteButtonTitle(String title) {
    delete.setText(title);
  }

  public Registration addSaveListener(
      ComponentEventListener<ClickEvent<Button>> listener) {
    return save.addClickListener(listener);
  }

  public Registration addCancelListener(
      ComponentEventListener<ClickEvent<Button>> listener) {
    return cancel.addClickListener(listener);
  }

  public Registration addDeleteListener(
      ComponentEventListener<ClickEvent<Button>> listener) {
    return delete.addClickListener(listener);
  }

  public void setSaveButtonVisible(boolean isVisible) {
    save.setVisible(isVisible);
  }

  public void setCancelButtonVisible(boolean isVisible) {
    cancel.setVisible(isVisible);
  }

  public void setDeleteButtonVisible(boolean isVisible) {
    delete.setVisible(isVisible);
  }

  public void setSaveButtonEnabled(boolean isEnabled) {
    save.setEnabled(isEnabled);
  }

  ;

  public void setCancelButtonEnabled(boolean isEnabled) {
    cancel.setEnabled(isEnabled);
  }
}
