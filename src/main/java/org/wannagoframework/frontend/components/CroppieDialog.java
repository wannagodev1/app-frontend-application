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

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import io.rocketbase.vaadin.croppie.Croppie;
import io.rocketbase.vaadin.croppie.model.ViewPortConfig;
import io.rocketbase.vaadin.croppie.model.ViewPortType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import org.wannagoframework.dto.utils.StoredFile;

/**
 * A generic dialog for confirming or cancelling an action.
 *
 * @param <T> The type of the action's subject
 */
public class CroppieDialog extends Dialog {

  private static final Runnable NO_OP = () -> {
  };
  private final H3 titleField = new H3();
  private final Div croppieContent = new Div();
  private final Button confirmButton = new Button();
  private final Button cancelButton = new Button("Cancel");
  private Registration registrationForConfirm;
  private Registration registrationForCancel;
  private Registration shortcutRegistrationForConfirm;

  /**
   * Constructor.
   */
  public CroppieDialog() {
    setCloseOnEsc(true);
    setCloseOnOutsideClick(false);

    confirmButton.addClickListener(e -> close());
    confirmButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    confirmButton.setAutofocus(true);
    cancelButton.addClickListener(e -> close());
    cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    HorizontalLayout buttonBar = new HorizontalLayout(confirmButton,
        cancelButton);
    buttonBar.setClassName("buttons confirm-buttons");

    titleField.setClassName("confirm-title");

    add(titleField, croppieContent, buttonBar);

    croppieContent.setHeight("350px");
  }

  /**
   * Opens the confirmation dialog.
   *
   * The dialog will display the given title and message(s), then call
   * <code>confirmHandler</code> if the Confirm button is clicked, or
   * <code>cancelHandler</code> if the Cancel button is clicked.
   *
   * @param title The title text
   * @param message Detail message (optional, may be empty)
   * @param additionalMessage Additional message (optional, may be empty)
   * @param actionName The action name to be shown on the Confirm button
   * @param isDisruptive True if the action is disruptive, such as deleting an item
   * @param item The subject of the action
   * @param confirmHandler The confirmation handler function
   * @param cancelHandler The cancellation handler function
   */
  public void open(String title, StoredFile storedFile,
      String actionName,
      Consumer<StoredFile> confirmHandler, Runnable cancelHandler) {
    buildCropie(storedFile);
    titleField.setText(title);
    confirmButton.setText(actionName);

    shortcutRegistrationForConfirm = confirmButton
        .addClickShortcut(Key.ENTER);

    Runnable cancelAction = cancelHandler == null ? NO_OP : cancelHandler;

    if (registrationForConfirm != null) {
      registrationForConfirm.remove();
    }
    registrationForConfirm = confirmButton
        .addClickListener(e -> confirmHandler.accept(storedFile));
    if (registrationForCancel != null) {
      registrationForCancel.remove();
    }
    registrationForCancel = cancelButton
        .addClickListener(e -> cancelAction.run());
    this.addOpenedChangeListener(e -> {
      if (!e.isOpened()) {
        cancelAction.run();
      }
    });
    confirmButton.removeThemeVariants(ButtonVariant.LUMO_ERROR);

    open();
  }

  @Override
  public void close() {
    super.close();
    if (shortcutRegistrationForConfirm != null) {
      shortcutRegistrationForConfirm.remove();
    }
  }

  protected void buildCropie(StoredFile storedFile) {
    StreamResource imageResource = new StreamResource(storedFile.getFilename(),
        () -> new ByteArrayInputStream(storedFile.getOrginalContent()));

    Croppie croppie = new Croppie(imageResource);
    croppie.setWidth("300px");
    croppie.setHeight("300px");

    croppie.withViewport(new ViewPortConfig(150, 150, ViewPortType.SQUARE))
        .withShowZoomer(true).withEnableResize(false).withEnableZoom(true);

    if (storedFile.getZoom() != null) {
      croppie.withZoom(storedFile.getZoom());
    }

    croppie.addCropListener(e -> {
      try {
        final String originalExtension = storedFile.getFilename()
            .substring(storedFile.getFilename().lastIndexOf('.') + 1);
        ByteArrayInputStream outputStream = new ByteArrayInputStream(
            storedFile.getOrginalContent());
        BufferedImage bufferedImage = ImageIO.read(outputStream);

        int imgH = bufferedImage.getHeight();
        int imgW = bufferedImage.getWidth();

        int topX = e.getPoints().getTopLeftX();
        int topY = e.getPoints().getTopLeftY();

        int botX = e.getPoints().getBottomRightX();
        int botY = e.getPoints().getBottomRightY();

        int w = botX - topX;
        int h = botY - topY;

        if ((topY + h) > imgH) {
          h = topY - imgH;
        }

        if ((topX + w) > imgW) {
          w = topX - imgW;
        }

        BufferedImage dest = bufferedImage
            .getSubimage(topX, topY,
                w,
                h);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(dest, originalExtension, baos);
        storedFile.setContent(baos.toByteArray());
        storedFile.setFilesize((long) storedFile.getContent().length);
        storedFile.setZoom(e.getZoom());
        storedFile.setHasChanged(true);
      } catch (IOException ex) {
        Notification.show(ex.getLocalizedMessage());
      }
    });

    croppieContent.removeAll();
    croppieContent.add(croppie);
  }
}
