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

public enum FontSize {

  XXS("var(--lumo-font-size-xxs)"),
  XS("var(--lumo-font-size-xs)"),
  S("var(--lumo-font-size-s)"),
  M("var(--lumo-font-size-m)"),
  L("var(--lumo-font-size-l)"),
  XL("var(--lumo-font-size-xl)"),
  XXL("var(--lumo-font-size-xxl)"),
  XXXL("var(--lumo-font-size-xxxl)");

  private String value;

  FontSize(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
