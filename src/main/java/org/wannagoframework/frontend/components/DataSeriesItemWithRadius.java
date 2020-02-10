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

import com.vaadin.flow.component.charts.model.DataSeriesItem;

public class DataSeriesItemWithRadius extends DataSeriesItem {

  private String radius;
  private String innerRadius;

  public String getRadius() {
    return radius;
  }

  public void setRadius(String radius) {
    this.radius = radius;
    makeCustomized();
  }

  public String getInnerRadius() {
    return innerRadius;
  }

  public void setInnerRadius(String innerRadius) {
    this.innerRadius = innerRadius;
    makeCustomized();
  }
}
