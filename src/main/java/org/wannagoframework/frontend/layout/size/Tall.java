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


package org.wannagoframework.frontend.layout.size;

public enum Tall implements Size {

  XS("var(--lumo-space-xs)", "spacing-tall-xs"), S("var(--lumo-space-s)",
      "spacing-tall-s"), M("var(--lumo-space-m)", "spacing-tall-m"), L(
      "var(--lumo-space-l)", "spacing-tall-l"), XL(
      "var(--lumo-space-xl)", "spacing-tall-xl");

  private String variable;
  private String spacingClassName;

  Tall(String variable, String spacingClassName) {
    this.variable = variable;
    this.spacingClassName = spacingClassName;
  }

  @Override
  public String[] getMarginAttributes() {
    return new String[]{"margin"};
  }

  @Override
  public String[] getPaddingAttributes() {
    return new String[]{"padding"};
  }

  @Override
  public String getSpacingClassName() {
    return this.spacingClassName;
  }

  @Override
  public String getVariable() {
    return this.variable;
  }
}
