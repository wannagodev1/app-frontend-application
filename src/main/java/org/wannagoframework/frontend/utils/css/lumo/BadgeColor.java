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


package org.wannagoframework.frontend.utils.css.lumo;

public enum BadgeColor {

  NORMAL("badge"), NORMAL_PRIMARY("badge primary"), SUCCESS(
      "badge success"), SUCCESS_PRIMARY("badge success primary"), ERROR(
      "badge error"), ERROR_PRIMARY(
      "badge error primary"), CONTRAST(
      "badge contrast"), CONTRAST_PRIMARY(
      "badge contrast primary");

  private String style;

  BadgeColor(String style) {
    this.style = style;
  }

  public String getThemeName() {
    return style;
  }

}
