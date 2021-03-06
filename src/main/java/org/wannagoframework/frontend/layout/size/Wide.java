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

public enum Wide implements Size {

  XS("var(--lumo-space-wide-xs)", "spacing-wide-xs"), S(
      "var(--lumo-space-wide-s)",
      "spacing-wide-s"), M("var(--lumo-space-wide-m)",
      "spacing-wide-m"), L("var(--lumo-space-wide-l)",
      "spacing-wide-l"), XL("var(--lumo-space-wide-xl)",
      "spacing-wide-xl"),

  RESPONSIVE_M("var(--lumo-space-wide-r-m)",
      null), RESPONSIVE_L("var(--lumo-space-wide-r-l)", null);

  private String variable;
  private String spacingClassName;

  Wide(String variable, String spacingClassName) {
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
