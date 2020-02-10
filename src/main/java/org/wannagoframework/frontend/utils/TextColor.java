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


package org.wannagoframework.frontend.utils;

public enum TextColor {

  HEADER("var(--lumo-header-text-color)"),
  BODY("var(--lumo-body-text-color)"),
  SECONDARY("var(--lumo-secondary-text-color)"),
  TERTIARY("var(--lumo-tertiary-text-color)"),
  DISABLED("var(--lumo-disabled-text-color)"),
  PRIMARY("var(--lumo-primary-text-color)"),
  PRIMARY_CONTRAST("var(--lumo-primary-contrast-color)"),
  ERROR("var(--lumo-error-text-color)"),
  ERROR_CONTRAST("var(--lumo-error-contrast-color)"),
  SUCCESS("var(--lumo-success-text-color)"),
  SUCCESS_CONTRAST("var(--lumo-success-contrast-color)");

  private String value;

  TextColor(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
