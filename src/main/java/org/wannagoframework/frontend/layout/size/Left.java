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

public enum Left implements Size {

  AUTO("auto", null),

  XS("var(--lumo-space-xs)", "spacing-l-xs"), S("var(--lumo-space-s)",
      "spacing-l-s"), M("var(--lumo-space-m)", "spacing-l-m"), L(
      "var(--lumo-space-l)",
      "spacing-l-l"), XL("var(--lumo-space-xl)", "spacing-l-xl"),

  RESPONSIVE_M("var(--lumo-space-r-m)", null), RESPONSIVE_L(
      "var(--lumo-space-r-l)",
      null), RESPONSIVE_X("var(--lumo-space-r-x)", null);

  private String variable;
  private String spacingClassName;

  Left(String variable, String spacingClassName) {
    this.variable = variable;
    this.spacingClassName = spacingClassName;
  }

  @Override
  public String[] getMarginAttributes() {
    return new String[]{"margin-left"};
  }

  @Override
  public String[] getPaddingAttributes() {
    return new String[]{"padding-left"};
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
