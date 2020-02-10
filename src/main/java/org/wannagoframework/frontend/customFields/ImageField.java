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


package org.wannagoframework.frontend.customFields;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;
import io.rocketbase.vaadin.croppie.Croppie;
import io.rocketbase.vaadin.croppie.model.ViewPortConfig;
import io.rocketbase.vaadin.croppie.model.ViewPortType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import org.wannagoframework.dto.utils.StoredFile;
import org.wannagoframework.frontend.components.detailsdrawers.DetailsDrawer;
import org.wannagoframework.frontend.components.detailsdrawers.DetailsDrawerFooter;
import org.wannagoframework.frontend.components.detailsdrawers.DetailsDrawerHeader;
import org.wannagoframework.frontend.utils.AppConst;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-05-13
 */
public class ImageField extends CustomField<StoredFile> implements HasStyle, Serializable {

  private final Image image;
  private StoredFile storedFile;
  private StoredFile initialImage;
  private Div croppieContent;

  public ImageField() {
    this(null);
  }

  public ImageField(String label) {
    if (label != null) {
      setLabel(label);
    }

    image = new Image(AppConst.NO_PICTURE, "");
    image.setWidth("150px");
    image.setHeight("150px");
    image.addClickListener(e -> addOrUpdateImage());

    croppieContent = new Div();
    croppieContent.setWidth("200px");
    croppieContent.setHeight("350px");

    add(image);
  }

  private void addOrUpdateImage() {
    Dialog uploadDialog = new Dialog();

    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setAcceptedFileTypes("image/*");
    upload.setAutoUpload(true);
    upload.setMaxFiles(1);

    final DetailsDrawer detailsDrawer = new DetailsDrawer(upload, croppieContent);

    upload.addSucceededListener(event -> {
      storedFile = new StoredFile();
      storedFile.setMimeType(event.getMIMEType());
      try {
        storedFile.setContent(IOUtils.toByteArray(buffer.getInputStream()));
      } catch (IOException e) {
        Notification.show(e.getLocalizedMessage());
      }
      storedFile.setOrginalContent(storedFile.getContent());
      storedFile.setFilesize((long) storedFile.getContent().length);
      storedFile.setFilename(buffer.getFileName());

      buildCropie(storedFile.getContent(), storedFile.getFilename(), null);

    });
    if (initialImage != null) {
      buildCropie(initialImage.getOrginalContent(), initialImage.getFilename(),
          storedFile != null ? storedFile.getZoom() : null);
    }

    DetailsDrawerHeader header = new DetailsDrawerHeader(
        getTranslation("element.global.addOrUpdate"));
    DetailsDrawerFooter footer = new DetailsDrawerFooter();
    footer.addCancelListener(cancelEvent -> {
      if (storedFile != null) {
        storedFile.setOrginalContent(initialImage.getOrginalContent());
        storedFile.setFilesize(initialImage.getFilesize());
        storedFile.setMimeType(initialImage.getMimeType());
        storedFile.setFilename(initialImage.getFilename());
        storedFile.setContent(initialImage.getContent());

        image.setSrc(new StreamResource(storedFile.getFilename(),
            () -> new ByteArrayInputStream(storedFile.getContent())));
      } else {
        image.setSrc(AppConst.NO_PICTURE);
      }
      uploadDialog.setOpened(false);
    });
    footer.addSaveListener(saveEvent -> {
      image.setSrc(new StreamResource(storedFile.getFilename(),
          () -> new ByteArrayInputStream(storedFile.getContent())));
      updateValue();
      uploadDialog.setOpened(false);
    });
    footer.addDeleteListener(deleteEvent -> {
      storedFile = null;
      initialImage = null;
      image.setSrc(AppConst.NO_PICTURE);
      updateValue();
      uploadDialog.setOpened(false);
    });
    detailsDrawer.setHeader(header);
    detailsDrawer.setFooter(footer);

    uploadDialog.add(detailsDrawer);

    uploadDialog.setOpened(true);
  }

  @Override
  protected StoredFile generateModelValue() {
    return storedFile;
  }

  protected void buildCropie(byte[] content, String filename, Float zoom) {
    StreamResource imageResource = new StreamResource(filename,
        () -> new ByteArrayInputStream(content));

    Croppie croppie = new Croppie(imageResource);
    croppie.setWidth("300px");
    croppie.setHeight("300px");

    if (zoom != null) {
      croppie.withViewport(new ViewPortConfig(150, 150, ViewPortType.CIRCLE))
          .withShowZoomer(true).withEnableResize(false).withEnableZoom(true).withZoom(zoom);
    } else {
      croppie.withViewport(new ViewPortConfig(150, 150, ViewPortType.CIRCLE))
          .withShowZoomer(true).withEnableResize(false).withEnableZoom(true);
    }

    croppie.addCropListener(e -> {
      if (content != null && e.isFromClient()) {
        try {
          final String originalExtension = filename.substring(filename.lastIndexOf('.') + 1);
          BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(content));

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
          storedFile.setZoom(e.getZoom());
          storedFile.setContent(baos.toByteArray());
          storedFile.setFilesize((long) storedFile.getContent().length);
          storedFile.setHasChanged(true);
        } catch (IOException ex) {
          Notification.show(ex.getLocalizedMessage());
        }
      }
    });
    croppieContent.removeAll();
    croppieContent.add(croppie);
  }

  @Override
  protected void setPresentationValue(StoredFile newPresentationValue) {
    if (newPresentationValue.getContent() != null) {
      storedFile = newPresentationValue;
      initialImage = new StoredFile();
      initialImage.setOrginalContent(newPresentationValue.getOrginalContent());
      initialImage.setFilesize(newPresentationValue.getFilesize());
      initialImage.setMimeType(newPresentationValue.getMimeType());
      initialImage.setFilename(newPresentationValue.getFilename());
      initialImage.setContent(newPresentationValue.getContent());

      image.setSrc(new StreamResource(newPresentationValue.getFilename(),
          () -> new ByteArrayInputStream(initialImage.getContent())));
    } else {
      storedFile = null;
      initialImage = null;
      image.setSrc(AppConst.NO_PICTURE);
    }
  }
}
