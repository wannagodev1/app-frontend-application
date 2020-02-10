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


package org.wannagoframework.frontend.renderer;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import org.wannagoframework.frontend.utils.AppConst;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-02-14
 */
public class BooleanRenderer<SOURCE> extends ComponentRenderer<Image, SOURCE> {

  protected ValueProvider<SOURCE, Boolean> valueProvider;

  public BooleanRenderer(ValueProvider<SOURCE, Boolean> valueProvider) {
    this.valueProvider = valueProvider;
  }

  public Image createComponent(SOURCE item) {
    Image image = new Image();

    Boolean val = valueProvider.apply(item);

    if (val != null && val) {
      image.setSrc(AppConst.ICON_OK);
    } else if (val != null) {
      image.setSrc(AppConst.ICON_KO);
    } else {
      image.setSrc(AppConst.ICON_BLANK);
    }

    return image;
  }
}