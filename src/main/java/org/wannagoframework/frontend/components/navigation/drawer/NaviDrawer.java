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

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import elemental.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.wannagoframework.frontend.security.SecurityUtils;
import org.wannagoframework.frontend.utils.UIUtils;

@CssImport("./styles/components/navi-drawer.css")
@JsModule("./swipe-away.js")
public class NaviDrawer extends Div
    implements AfterNavigationObserver {

  private String CLASS_NAME = "navi-drawer";
  private String RAIL = "rail";
  private String OPEN = "open";

  private Div scrim;

  private Div mainContent;
  private TextField search;
  private Div scrollableArea;

  private Button railButton;
  private NaviMenu menu;


  public NaviDrawer(boolean showSearchMenu, String version, String environement ) {
    setClassName(CLASS_NAME);

    initScrim();
    initMainContent();

    initHeader();
    if ( showSearchMenu )
      initSearch();

    initScrollableArea();
    initMenu();

    initFooter(version, environement);
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    UI ui = attachEvent.getUI();
    ui.getPage().executeJavaScript("window.addSwipeAway($0,$1,$2,$3)",
        mainContent.getElement(), this, "onSwipeAway",
        scrim.getElement());
  }

  @ClientCallable
  public void onSwipeAway(JsonObject data) {
    close();
  }

  private void initScrim() {
    // Backdrop on small viewports
    scrim = new Div();
    scrim.addClassName(CLASS_NAME + "__scrim");
    scrim.addClickListener(event -> close());
    add(scrim);
  }

  private void initMainContent() {
    mainContent = new Div();
    mainContent.addClassName(CLASS_NAME + "__content");
    add(mainContent);
  }

  private void initHeader() {
    mainContent.add(new BrandExpression(getTranslation("element.application.title")));
  }

  private void initSearch() {
    search = new TextField();
    search.addValueChangeListener(e -> menu.filter(search.getValue()));
    search.setClearButtonVisible(true);
    search.setPlaceholder("Search");
    search.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    mainContent.add(search);

    search.setVisible(SecurityUtils.isUserLoggedIn());
  }

  public void toogleSearch() {
    if ( search != null )
      search.setVisible(SecurityUtils.isUserLoggedIn());
  }

  private void initScrollableArea() {
    scrollableArea = new Div();
    scrollableArea.addClassName(CLASS_NAME + "__scroll-area");
    mainContent.add(scrollableArea);
  }

  private void initMenu() {
    menu = new NaviMenu();
    scrollableArea.add(menu);
  }

  private void initFooter(String version, String environement) {
    if (StringUtils.isNotBlank( version ) ) {
      Label l = UIUtils.createH5Label("Version " + version + " (" + environement +")");
      l.addClassName(CLASS_NAME + "__footer");
      l.addClassName( "version" );
      mainContent.add(l);
    }

    railButton = UIUtils.createSmallButton("Collapse",
        VaadinIcon.CHEVRON_LEFT_SMALL);
    railButton.addClassName(CLASS_NAME + "__footer");
    railButton.addClickListener(event -> toggleRailMode());
    railButton.getElement().setAttribute("aria-label", "Collapse menu");
    mainContent.add(railButton);
  }

  private void toggleRailMode() {
    if (getElement().hasAttribute(RAIL)) {
      getElement().setAttribute(RAIL, false);
      railButton.setIcon(new Icon(VaadinIcon.CHEVRON_LEFT_SMALL));
      railButton.setText("Collapse");
      UIUtils.setAriaLabel("Collapse menu", railButton);
    } else {
      getElement().setAttribute(RAIL, true);
      railButton.setIcon(new Icon(VaadinIcon.CHEVRON_RIGHT_SMALL));
      railButton.setText("Expand");
      UIUtils.setAriaLabel("Expand menu", railButton);
      getUI().get().getPage().executeJs(
          "var originalStyle = getComputedStyle($0).pointerEvents;" //
              + "$0.style.pointerEvents='none';" //
              + "setTimeout(function() {$0.style.pointerEvents=originalStyle;}, 170);",
          getElement());
    }
  }

  public void toggle() {
    if (getElement().hasAttribute(OPEN)) {
      close();
    } else {
      open();
    }
  }

  private void open() {
    getElement().setAttribute(OPEN, true);
  }

  private void close() {
    getElement().setAttribute(OPEN, false);
    applyIOS122Workaround();
  }

  private void applyIOS122Workaround() {
    // iOS 12.2 sometimes fails to animate the menu away.
    // It should be gone after 240ms
    // This will make sure it disappears even when the browser fails.
    getUI().ifPresent(ui -> ui.getPage().executeJs(
        "var originalStyle = getComputedStyle($0).transitionProperty;" //
            + "setTimeout(function() {$0.style.transitionProperty='padding'; requestAnimationFrame(function() {$0.style.transitionProperty=originalStyle})}, 250);",
        mainContent.getElement()));
  }

  public NaviMenu getMenu() {
    return menu;
  }

  @Override
  public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
    close();
  }

}
