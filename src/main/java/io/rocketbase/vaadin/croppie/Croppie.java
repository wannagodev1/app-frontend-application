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


package io.rocketbase.vaadin.croppie;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.PropertyDescriptor;
import com.vaadin.flow.component.PropertyDescriptors;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import io.rocketbase.vaadin.croppie.model.CropPoints;
import io.rocketbase.vaadin.croppie.model.CroppieConfiguration;
import io.rocketbase.vaadin.croppie.model.SizeConfig;
import io.rocketbase.vaadin.croppie.model.ViewPortConfig;
import lombok.Getter;
import lombok.Setter;

@Tag("vaadin-croppie")
@NpmPackage(value = "croppie", version = "2.6.4")
@JsModule("./mycroppie/croppie.js")
public class Croppie extends PolymerTemplate<CroppieModel> implements HasSize, HasStyle {

  private static final PropertyDescriptor<String, String> srcDescriptor = PropertyDescriptors
      .attributeWithDefault("src", "");
  private static final PropertyDescriptor<String, String> labelDescriptor = PropertyDescriptors
      .attributeWithDefault("label", "");

  private CroppieConfiguration config;
  @Getter
  private boolean attached = false;
  private boolean initPhase = true;

  /**
   * size is automatically configured via CroppieConfig boundary/viewport
   */
  @Setter
  private boolean autoConfigSize = true;

  public Croppie(AbstractStreamResource src) {
    withSrc(src);
  }

  public Croppie(String src) {
    withSrc(src);
  }

  public Croppie withConfig(CroppieConfiguration config) {
    this.config = config;
    return this;
  }

  protected CroppieConfiguration getConfigInitialized() {
    if (this.config == null) {
      this.config = new CroppieConfiguration();
    }
    return this.config;
  }

  protected Croppie getAndUpdateConfig() {
    if (isAttached()) {
      updateConfig();
    }
    return this;
  }

  public Croppie withPoints(CropPoints points) {
    getConfigInitialized().setPoints(points);
    return getAndUpdateConfig();
  }

  public Croppie withZoom(Float zoom) {
    getConfigInitialized().setZoom(zoom);
    return getAndUpdateConfig();
  }

  /**
   * The outer container of the cropper
   * <p>
   * Default will default to the size of the container
   */
  public Croppie withBoundary(SizeConfig boundary) {
    getConfigInitialized().setBoundary(boundary);
    return getAndUpdateConfig();
  }

  /**
   * The inner container of the coppie. The visible part of the image
   * <p>
   * Default { width: 100, height: 100, type: 'square' }
   */
  public Croppie withViewport(ViewPortConfig viewport) {
    getConfigInitialized().setViewport(viewport);
    return getAndUpdateConfig();
  }

  /**
   * Enable or disable support for resizing the viewport area.
   * <p>
   * Default false
   */
  public Croppie withEnableResize(boolean enableResize) {
    getConfigInitialized();
    this.config.setEnableResize(enableResize);
    return getAndUpdateConfig();
  }

  /**
   * Enable zooming functionality. If set to false - scrolling and pinching would not zoom.
   * <p>
   * Default true
   */
  public Croppie withEnableZoom(boolean enableZoom) {
    getConfigInitialized();
    this.config.setEnableZoom(enableZoom);
    return getAndUpdateConfig();
  }

  /**
   * Enable or disable the ability to use the mouse wheel to zoom in and out on a croppie instance.
   * <p>
   * Default true
   */
  public Croppie withMouseWheelZoom(boolean mouseWheelZoom) {
    getConfigInitialized();
    this.config.setMouseWheelZoom(mouseWheelZoom);
    return getAndUpdateConfig();
  }

  /**
   * Hide or Show the zoom slider
   * <p>
   * Default true
   */
  public Croppie withShowZoomer(boolean showZoomer) {
    getConfigInitialized();
    this.config.setShowZoomer(showZoomer);
    return getAndUpdateConfig();
  }


  public Registration addCropListener(
      ComponentEventListener<CropEvent> listener) {
    return addListener(CropEvent.class, listener);
  }

  /**
   * Gets the image URL.
   *
   * @return the image URL
   */
  public String getSrc() {
    return get(srcDescriptor);
  }

  /**
   * Sets the image URL.
   *
   * @param src the image URL
   */
  public Croppie withSrc(String src) {
    set(srcDescriptor, src);
    return getAndUpdateConfig();
  }

  /**
   * Sets the image URL with the URL of the given {@link StreamResource}.
   *
   * @param src the resource value, not null
   */
  public Croppie withSrc(AbstractStreamResource src) {
    getElement().setAttribute("src", src);
    return getAndUpdateConfig();
  }

  public String getLabel() {
    return get(labelDescriptor);
  }

  public Croppie withLabel(String label) {
    set(labelDescriptor, label);
    return getAndUpdateConfig();
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    attached = true;
    updateConfig();
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    super.onDetach(detachEvent);
    attached = false;
    initPhase = false;
  }

  protected void updateConfig() {
    getModel().setCroppieOptions(config.getJsonString());
    if (autoConfigSize) {
      if (getHeight() == null || getHeight().isEmpty()) {
        // TODO: needs a better implementation
        int extraHeight = config.isShowZoomer() ? 58 : 0;
        if (getLabel() != null && !getLabel().isEmpty()) {
          extraHeight += 18;
        }
        if (config.getBoundary() != null) {
          setWidth(String.format("%dpx", config.getBoundary().getWidth()));
          setHeight(String.format("%dpx", config.getBoundary().getHeight() + extraHeight));
          return;
        }
        if (config.getViewport() != null) {
          setWidth(String.format("%dpx", config.getViewport().getWidth()));
          setHeight(String.format("%dpx", config.getViewport().getHeight() + extraHeight));
        }
      }
    }
  }

  @ClientCallable
  private void cropChanged(String points, String zoom) {
    CropEvent event = new CropEvent(this, !initPhase,
        CropPoints.parseArray(points), Float.parseFloat(zoom));
    fireEvent(event);
    initPhase = false;
  }
}
