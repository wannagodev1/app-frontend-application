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


package org.wannagoframework.frontend.components.card;

import com.github.appreciated.card.label.WhiteSpaceLabel;
import com.vaadin.flow.component.Html;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-07-13
 */
public class SecondaryLabelHtmlComponent extends Html implements WhiteSpaceLabel {

  public SecondaryLabelHtmlComponent(String text) {
    super(text);
    init();
  }

  private void init() {
    getElement().getStyle()
        .set("font-size", "var(--lumo-font-size-s)")
        .set("text-overflow", "ellipsis")
        .set("overflow", "scroll")
        .set("color", "var(--lumo-secondary-text-color)")
        .set("width", "100%")
        .set("min-height", "100px")
        .set("max-height", "200px");
  }

}

