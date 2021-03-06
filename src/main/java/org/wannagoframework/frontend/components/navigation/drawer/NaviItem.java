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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import java.util.ArrayList;
import java.util.List;
import org.wannagoframework.frontend.utils.UIUtils;

@CssImport("./styles/components/navi-item.css")
public class NaviItem extends Div {

  protected Button expandCollapse;
  private String CLASS_NAME = "navi-item";
  private int level = 0;
  private Component link;
  private Class<? extends Component> navigationTarget;
  private String text;
  private List<NaviItem> subItems;
  private boolean subItemsVisible;

  public NaviItem(VaadinIcon icon, String text, Class<? extends Component> navigationTarget) {
    this(text, navigationTarget);
    link.getElement().insertChild(0, new Icon(icon).getElement());
  }

  public <T, C extends Component & HasUrlParameter<T>> NaviItem(VaadinIcon icon, String text,
      Class<C> navigationTarget, T parameters) {
    this(text, navigationTarget, parameters);
    link.getElement().insertChild(0, new Icon(icon).getElement());
  }

  public NaviItem(Image image, String text, Class<? extends Component> navigationTarget) {
    this(text, navigationTarget);
    link.getElement().insertChild(0, image.getElement());
  }

  public <T, C extends Component & HasUrlParameter<T>> NaviItem(Image image, String text,
      Class<C> navigationTarget, T parameters) {
    this(text, navigationTarget, parameters);
    link.getElement().insertChild(0, image.getElement());
  }

  public NaviItem(String text, Class<? extends Component> navigationTarget) {
    setClassName(CLASS_NAME);
    setLevel(0);
    this.text = text;
    this.navigationTarget = navigationTarget;

    if (navigationTarget != null) {
      RouterLink routerLink = new RouterLink(null, navigationTarget);
      routerLink.add(new Span(text));
      routerLink.setClassName(CLASS_NAME + "__link");
      routerLink.setHighlightCondition(HighlightConditions.sameLocation());
      this.link = routerLink;
    } else {
      Div div = new Div(new Span(text));
      div.addClickListener(e -> expandCollapse.click());
      div.setClassName(CLASS_NAME + "__link");
      this.link = div;
    }

    expandCollapse = UIUtils
        .createButton(VaadinIcon.CARET_UP, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
    expandCollapse.addClickListener(event -> setSubItemsVisible(!subItemsVisible));
    expandCollapse.setVisible(false);

    subItems = new ArrayList<>();
    subItemsVisible = true;

    updateAriaLabel();
    add(link, expandCollapse);
  }

  public <T, C extends Component & HasUrlParameter<T>> NaviItem(String text,
      Class<C> navigationTarget, T parameters) {
    setClassName(CLASS_NAME);
    setLevel(0);
    this.text = text;
    this.navigationTarget = navigationTarget;

    if (navigationTarget != null) {
      RouterLink routerLink = new RouterLink(null, navigationTarget, parameters);
      routerLink.add(new Span(text));
      routerLink.setClassName(CLASS_NAME + "__link");
      routerLink.setHighlightCondition(HighlightConditions.sameLocation());
      this.link = routerLink;
    } else {
      Div div = new Div(new Span(text));
      div.addClickListener(e -> expandCollapse.click());
      div.setClassName(CLASS_NAME + "__link");
      this.link = div;
    }

    expandCollapse = UIUtils
        .createButton(VaadinIcon.CARET_UP, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
    expandCollapse.addClickListener(event -> setSubItemsVisible(!subItemsVisible));
    expandCollapse.setVisible(false);

    subItems = new ArrayList<>();
    subItemsVisible = true;

    updateAriaLabel();
    add(link, expandCollapse);
  }

  private void updateAriaLabel() {
    String action = (subItemsVisible ? "Collapse " : "Expand ") + text;
    UIUtils.setAriaLabel(action, expandCollapse);
  }

  public boolean isHighlighted(AfterNavigationEvent e) {
    return link instanceof RouterLink && ((RouterLink) link)
        .getHighlightCondition().shouldHighlight((RouterLink) link, e);
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
    if (level > 0) {
      getElement().setAttribute("level", Integer.toString(level));
    }
  }

  public Class<? extends Component> getNavigationTarget() {
    return navigationTarget;
  }

  public void addSubItem(NaviItem item) {
    if (!expandCollapse.isVisible()) {
      expandCollapse.setVisible(true);
    }
    item.setLevel(getLevel() + 1);
    subItems.add(item);
  }

  private void setSubItemsVisible(boolean visible) {
    if (level == 0) {
      expandCollapse.setIcon(new Icon(visible ? VaadinIcon.CARET_UP : VaadinIcon.CARET_DOWN));
    }
    subItems.forEach(item -> item.setVisible(visible));
    subItemsVisible = visible;
    updateAriaLabel();
  }

  public String getText() {
    return text;
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);

    // If true, we only update the icon. If false, we hide all the sub items
    if (visible) {
      if (level == 0) {
        expandCollapse.setIcon(new Icon(VaadinIcon.CARET_DOWN));
      }
    } else {
      setSubItemsVisible(visible);
    }
  }
}