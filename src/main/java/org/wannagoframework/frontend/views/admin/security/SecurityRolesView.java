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


package org.wannagoframework.frontend.views.admin.security;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.wannagoframework.dto.domain.security.SecurityRole;
import org.wannagoframework.dto.serviceQuery.generic.DeleteByStrIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.utils.SecurityConst;
import org.wannagoframework.frontend.client.security.SecurityServices;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.dataproviders.SecurityRoleDataProvider;
import org.wannagoframework.frontend.renderer.BooleanRenderer;
import org.wannagoframework.frontend.utils.AppConst;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.i18n.I18NPageTitle;
import org.wannagoframework.frontend.views.DefaultMasterDetailsView;

@I18NPageTitle(messageKey = AppConst.TITLE_SECURITY_ROLES)
@Secured(SecurityConst.ROLE_ADMIN)
public class SecurityRolesView extends DefaultMasterDetailsView<SecurityRole, DefaultFilter> {

  public SecurityRolesView() {
    super("securityRole.", SecurityRole.class, new SecurityRoleDataProvider(),
        (e) -> SecurityServices.getSecurityRoleService().save(new SaveQuery<>(e)),
        e -> SecurityServices.getSecurityRoleService().delete(new DeleteByStrIdQuery(e.getId())));
  }

  protected Grid createGrid() {
    grid = new Grid<>();
    grid.setSelectionMode(SelectionMode.SINGLE);

    grid.addSelectionListener(event -> event.getFirstSelectedItem()
        .ifPresent(this::showDetails));

    grid.setDataProvider(dataProvider);
    grid.setHeight("100%");

    grid.addColumn(SecurityRole::getName).setKey("name");
    grid.addColumn(new BooleanRenderer<>(SecurityRole::getCanLogin)).setKey("canLogin");

    grid.getColumns().forEach(column -> {
      if (column.getKey() != null) {
        column.setHeader(getTranslation("element." + I18N_PREFIX + column.getKey()));
        column.setResizable(true);
      }
    });
    return grid;
  }

  protected Component createDetails(SecurityRole securityRole) {
    boolean isNew = securityRole.getId() == null;
    detailsDrawerHeader.setTitle(isNew ? getTranslation("element.global.new") + " : "
        : getTranslation("element.global.update") + " : " + securityRole.getName());

    detailsDrawerFooter.setDeleteButtonVisible(!isNew);

    TextField name = new TextField();
    name.setWidth("100%");

    TextField description = new TextField();
    description.setWidth("100%");

    Checkbox canLogin = new Checkbox();

    Checkbox isActive = new Checkbox();

    // Form layout
    FormLayout editingForm = new FormLayout();
    editingForm.addClassNames(LumoStyles.Padding.Bottom.L,
        LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
    editingForm.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1,
            FormLayout.ResponsiveStep.LabelsPosition.TOP),
        new FormLayout.ResponsiveStep("21em", 2,
            FormLayout.ResponsiveStep.LabelsPosition.TOP));
    FormLayout.FormItem nameItem = editingForm
        .addFormItem(name, getTranslation("element." + I18N_PREFIX + "name"));
    editingForm.addFormItem(description, getTranslation("element." + I18N_PREFIX + "description"));
    editingForm.addFormItem(canLogin, getTranslation("element." + I18N_PREFIX + "canLogin"));
    FormLayout.FormItem isActiveItem = editingForm
        .addFormItem(isActive, getTranslation("element." + I18N_PREFIX + "isActive"));

    UIUtils.setColSpan(2, nameItem);

    binder.setBean(securityRole);

    binder.bind(name, SecurityRole::getName, SecurityRole::setName);
    binder.bind(description, SecurityRole::getDescription, SecurityRole::setDescription);
    binder.bind(canLogin, SecurityRole::getCanLogin, SecurityRole::setCanLogin);
    binder.bind(isActive, SecurityRole::getIsActive, SecurityRole::setIsActive);

    return editingForm;
  }

  protected void filter(String filter) {
    dataProvider
        .setFilter(new DefaultFilter(
            StringUtils.isBlank(filter) ? null : "*" + filter + "*",
            Boolean.TRUE));
  }
}
