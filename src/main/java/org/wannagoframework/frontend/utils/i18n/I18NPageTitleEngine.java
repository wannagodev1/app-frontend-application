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


package org.wannagoframework.frontend.utils.i18n;

import static org.rapidpm.frp.matcher.Case.match;
import static org.rapidpm.frp.matcher.Case.matchCase;
import static org.rapidpm.frp.model.Result.failure;
import static org.rapidpm.frp.model.Result.success;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.UIInitEvent;
import com.vaadin.flow.server.UIInitListener;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import java.util.List;
import java.util.Locale;
import org.rapidpm.frp.functions.CheckedFunction;
import org.wannagoframework.commons.utils.HasLogger;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-04-18
 */
public class I18NPageTitleEngine implements VaadinServiceInitListener, UIInitListener,
    BeforeEnterListener,
    HasLogger {

  public static final String ERROR_MSG_NO_LOCALE = "no locale provided and i18nProvider #getProvidedLocales()# list is empty !! ";
  public static final String ERROR_MSG_NO_ANNOTATION = "no annotation found at class ";

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Class<?> navigationTarget = event.getNavigationTarget();
    I18NPageTitle annotation = navigationTarget.getAnnotation(I18NPageTitle.class);

    match(
        matchCase(() -> success(annotation.messageKey())),
        matchCase(() -> annotation == null,
            () -> failure(ERROR_MSG_NO_ANNOTATION + navigationTarget.getName())),
        matchCase(() -> annotation.messageKey().isEmpty(),
            () -> success(annotation.defaultValue()))
    )
        .ifPresentOrElse(
            msgKey -> {
              final I18NProvider i18NProvider = VaadinService
                  .getCurrent()
                  .getInstantiator()
                  .getI18NProvider();
              final Locale locale = event.getUI().getLocale();
              final List<Locale> providedLocales = i18NProvider.getProvidedLocales();
              match(
                  matchCase(() -> success(providedLocales.get(0))),
                  matchCase(() -> locale == null && providedLocales.isEmpty(),
                      () -> failure(ERROR_MSG_NO_LOCALE + i18NProvider.getClass().getName())),
                  matchCase(() -> locale == null,
                      () -> success(providedLocales.get(0))),
                  matchCase(() -> providedLocales.contains(locale),
                      () -> success(locale))
              ).ifPresentOrElse(
                  finalLocale -> ((CheckedFunction<Class<? extends TitleFormatter>, TitleFormatter>) f -> f
                      .getDeclaredConstructor().newInstance())
                      .apply(annotation.formatter())
                      .ifPresentOrElse(
                          formatter -> formatter
                              .apply(i18NProvider, finalLocale, msgKey).
                                  ifPresentOrElse(title -> UI.getCurrent()
                                          .getPage()
                                          .setTitle(title),
                                      failed -> logger().info(failed)),
                          failed -> logger().info(failed)),
                  failed -> logger().info(failed));
            }
            , failed -> logger().info(failed));
  }

  @Override
  public void uiInit(UIInitEvent event) {
    final UI ui = event.getUI();
    ui.addBeforeEnterListener(this);
    //addListener(ui, PermissionsChangedEvent.class, e -> ui.getPage().reload());
  }

  @Override
  public void serviceInit(ServiceInitEvent event) {
    event
        .getSource()
        .addUIInitListener(this);
  }
}
