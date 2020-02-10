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


package org.wannagoframework.frontend.utils.css;

public enum Shadow {

  XS("var(--lumo-box-shadow-xs)"), S("var(--lumo-box-shadow-s)"), M(
      "var(--lumo-box-shadow-m)"), L("var(--lumo-box-shadow-l)"), XL(
      "var(--lumo-box-shadow-xl)");

  private String value;

  Shadow(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
