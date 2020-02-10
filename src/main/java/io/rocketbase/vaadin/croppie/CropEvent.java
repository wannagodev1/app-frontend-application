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

import com.vaadin.flow.component.ComponentEvent;
import io.rocketbase.vaadin.croppie.model.CropPoints;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CropEvent extends ComponentEvent<Croppie> {

  private CropPoints points;
  private float zoom;

  public CropEvent(Croppie source, boolean fromClient, CropPoints points, float zoom) {
    super(source, fromClient);
    this.points = points;
    this.zoom = zoom;
  }
}
