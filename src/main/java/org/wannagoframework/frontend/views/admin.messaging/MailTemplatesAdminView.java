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

package org.wannagoframework.frontend.views.admin.messaging;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.RouterLayout;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.notification.MailTemplate;
import org.wannagoframework.dto.utils.SecurityConst;
import org.wannagoframework.frontend.components.FlexBoxLayout;
import org.wannagoframework.frontend.components.navigation.bar.AppBar;
import org.wannagoframework.frontend.components.navigation.bar.AppBar.NaviMode;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.dataproviders.MailTemplateDataProvider;
import org.wannagoframework.frontend.layout.ViewFrame;
import org.wannagoframework.frontend.layout.size.Horizontal;
import org.wannagoframework.frontend.layout.size.Top;
import org.wannagoframework.frontend.utils.AppConst;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.css.BoxSizing;
import org.wannagoframework.frontend.utils.i18n.I18NPageTitle;
import org.wannagoframework.frontend.views.WannagoMainView;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-04-14
 */
@I18NPageTitle(messageKey = AppConst.TITLE_MAIL_TEMPLATES_ADMIN)
@Secured(SecurityConst.ROLE_ADMIN)
public class MailTemplatesAdminView extends ViewFrame implements RouterLayout, HasLogger {

  private final static String I18N_PREFIX = "mailTemplate.";

  private DefaultDataProvider<MailTemplate, DefaultFilter> dataProvider;

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    initHeader();

    setViewContent(createContent());

    filter(null);
  }

  private void initHeader() {
    AppBar appBar = WannagoMainView.get().getAppBar();
    appBar.setNaviMode(NaviMode.MENU);

    Button searchButton = UIUtils.createTertiaryButton(VaadinIcon.SEARCH);
    searchButton.addClickListener(event -> appBar.searchModeOn());
    appBar.addSearchListener(event -> filter((String) event.getValue()));
    appBar.setSearchPlaceholder(getTranslation("element.global.search"));
    appBar.addActionItem(searchButton);

    Button newPlaceButton = UIUtils.createTertiaryButton(VaadinIcon.PLUS);
    newPlaceButton.addClickListener(event ->
        viewDetails(new MailTemplate())
    );
    appBar.addActionItem(newPlaceButton);
  }

  private Component createContent() {
    FlexBoxLayout content = new FlexBoxLayout(createGrid());
    content.setBoxSizing(BoxSizing.BORDER_BOX);
    content.setHeightFull();
    content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
    return content;
  }

  private Grid createGrid() {
    Grid<MailTemplate> grid = new Grid<>();

    grid.setSelectionMode(SelectionMode.SINGLE);

    grid.addSelectionListener(event -> event.getFirstSelectedItem()
        .ifPresent(this::viewDetails));
    dataProvider = new MailTemplateDataProvider();
    grid.setDataProvider(dataProvider);
    grid.setHeight("100%");

    grid.addColumn(MailTemplate::getName).setKey("name");
    grid.addColumn(MailTemplate::getMailAction).setKey("mailAction");

    grid.getColumns().forEach(column -> {
      if (column.getKey() != null) {
        column.setHeader(getTranslation("element." + I18N_PREFIX + column.getKey()));
        column.setResizable(true);
      }
    });
    return grid;
  }

  private void filter(String filter) {
    dataProvider
        .setFilter(new DefaultFilter(
            StringUtils.isBlank(filter) ? null
                : "(?i).*" + filter + ".*",
            Boolean.TRUE));
  }

  private void viewDetails(MailTemplate mailTemplate) {
    UI.getCurrent()
        .navigate(MailTemplateAdminView.class,
            mailTemplate.getId() == null ? "-1" : mailTemplate.getId());
  }
}