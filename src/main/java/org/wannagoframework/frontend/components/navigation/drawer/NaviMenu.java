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


package org.wannagoframework.frontend.components.navigation.drawer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.HasUrlParameter;
import java.util.List;
import java.util.stream.Collectors;

@CssImport("./styles/components/navi-menu.css")
public class NaviMenu extends Div {

  private String CLASS_NAME = "navi-menu";

  public NaviMenu() {
    setClassName(CLASS_NAME);
  }

  protected void addNaviItem(NaviItem item) {
    add(item);
  }

  protected void addNaviItem(NaviItem parent, NaviItem item) {
    parent.addSubItem(item);
    addNaviItem(item);
  }

  public void filter(String filter) {
    getNaviItems().forEach(naviItem -> {
      boolean matches = ((NaviItem) naviItem).getText().toLowerCase()
          .contains(filter.toLowerCase());
      naviItem.setVisible(matches);
    });
  }

  public NaviItem addNaviItem(String text,
      Class<? extends Component> navigationTarget) {
    NaviItem item = new NaviItem(text, navigationTarget);
    addNaviItem(item);
    return item;
  }

  public NaviItem addNaviItem(VaadinIcon icon, String text,
      Class<? extends Component> navigationTarget) {
    NaviItem item = new NaviItem(icon, text, navigationTarget);
    addNaviItem(item);
    return item;
  }

  public NaviItem addNaviItem(Image image, String text,
      Class<? extends Component> navigationTarget) {
    NaviItem item = new NaviItem(image, text, navigationTarget);
    addNaviItem(item);
    return item;
  }

  public NaviItem addNaviItem(NaviItem parent, String text,
      Class<? extends Component> navigationTarget) {
    NaviItem item = new NaviItem(text, navigationTarget);
    addNaviItem(parent, item);
    return item;
  }

  public <T, C extends Component & HasUrlParameter<T>> NaviItem addNaviItem(VaadinIcon icon,
      String text,
      Class<C> navigationTarget, T parameter) {
    NaviItem item = new NaviItem(icon, text, navigationTarget, parameter);
    addNaviItem(item);
    return item;
  }

  public <T, C extends Component & HasUrlParameter<T>> NaviItem addNaviItem(NaviItem parent,
      String text,
      Class<C> navigationTarget, T parameter) {
    NaviItem item = new NaviItem(text, navigationTarget, parameter);
    addNaviItem(parent, item);
    return item;
  }

  public List<NaviItem> getNaviItems() {
    List<NaviItem> items = (List) getChildren()
        .collect(Collectors.toList());
    return items;
  }

}
