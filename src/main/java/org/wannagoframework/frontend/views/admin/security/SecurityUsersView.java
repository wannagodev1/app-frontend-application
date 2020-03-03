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
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.gatanaso.MultiselectComboBox;
import org.wannagoframework.dto.domain.BaseEntity;
import org.wannagoframework.dto.domain.security.SecurityRole;
import org.wannagoframework.dto.domain.security.SecurityUser;
import org.wannagoframework.dto.domain.security.SecurityUserTypeEnum;
import org.wannagoframework.dto.serviceQuery.generic.DeleteByStrIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.utils.SecurityConst;
import org.wannagoframework.frontend.client.security.SecurityServices;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.dataproviders.SecurityUserDataProvider;
import org.wannagoframework.frontend.renderer.BooleanRenderer;
import org.wannagoframework.frontend.utils.AppConst;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.i18n.I18NPageTitle;
import org.wannagoframework.frontend.views.DefaultMasterDetailsView;


@I18NPageTitle(messageKey = AppConst.TITLE_SECURITY_USERS)
@Secured(SecurityConst.ROLE_ADMIN)
public class SecurityUsersView extends DefaultMasterDetailsView<SecurityUser, DefaultFilter> {

  public SecurityUsersView() {
    super("securityUser.", SecurityUser.class, new SecurityUserDataProvider(),
        (e) -> SecurityServices.getSecurityUserService().save(new SaveQuery<>(e))
            .getData(),
        e -> SecurityServices.getSecurityUserService().delete(new DeleteByStrIdQuery(e.getId())));
  }

  protected Grid createGrid() {
    grid = new Grid<>();
    grid.setSelectionMode(SelectionMode.SINGLE);

    grid.addSelectionListener(event -> event.getFirstSelectedItem()
        .ifPresent(this::showDetails));

    grid.setDataProvider(dataProvider);
    grid.setHeight("100%");

    grid.addColumn(SecurityUser::getUsername).setKey("username");

    grid.addColumn(new BooleanRenderer<>(SecurityUser::getIsAccountLocked))
        .setKey("isAccountLocked");
    grid.addColumn(new BooleanRenderer<>(SecurityUser::getIsAccountExpired))
        .setKey("isAccountExpired");
    grid.addColumn(new BooleanRenderer<>(BaseEntity::getIsActive)).setKey("isActive");

    grid.addColumn(new TextRenderer<>(
        securityUser -> securityUser.getRoles() == null ? ""
            : securityUser.getRoles().stream().map(SecurityRole::getName)
                .reduce((a, b) -> a.concat(", ").concat(b)).orElse(""))).setKey("roles");

    grid.getColumns().forEach(column -> {
      if (column.getKey() != null) {
        column.setHeader(getTranslation("element." + I18N_PREFIX + column.getKey()));
        column.setResizable(true);
        column.setSortable(true);
      }
    });
    return grid;
  }

  protected Component createDetails(SecurityUser securityUser) {
    boolean isNew = securityUser.getId() == null;
    detailsDrawerHeader.setTitle(isNew ? getTranslation("element.global.new") + " : "
        : getTranslation("element.global.update") + " : " + securityUser.getUsername());

    detailsDrawerFooter.setDeleteButtonVisible(!isNew);

    TextField usernameField = new TextField();
    usernameField.setWidthFull();

    PasswordField passwordField = new PasswordField();
    passwordField.setWidthFull();

    TextField firstNameField = new TextField();
    firstNameField.setReadOnly(true);
    firstNameField.setWidthFull();

    TextField lastNameField = new TextField();
    lastNameField.setReadOnly(true);
    lastNameField.setWidthFull();

    EmailField emailField = new EmailField();
    emailField.setReadOnly(true);
    emailField.setWidthFull();

    TextField nickNameField = new TextField();
    nickNameField.setReadOnly(true);
    nickNameField.setWidthFull();

    TextField failedLoginAttemptsField = new TextField();
    failedLoginAttemptsField.setWidthFull();

    DatePicker passwordLastModificationField = new DatePicker();
    passwordLastModificationField.setReadOnly(true);
    passwordLastModificationField.setWidthFull();

    DatePicker lastSuccessfulLoginField = new DatePicker();
    lastSuccessfulLoginField.setReadOnly(true);
    lastSuccessfulLoginField.setWidthFull();

    Checkbox isActiveField = new Checkbox();

    Checkbox isAccountExpiredField = new Checkbox();

    Checkbox isAccountLockedField = new Checkbox();

    Checkbox isActivatedField = new Checkbox();

    Checkbox isCredentialsExpiredField = new Checkbox();

    List<Locale> locales = Arrays.asList(Locale.getAvailableLocales());
    locales.sort(Comparator.comparing(Locale::getDisplayName));

    ComboBox<Locale> defaultLocaleField = new ComboBox<>();
    defaultLocaleField.setItems(locales);
    defaultLocaleField.setItemLabelGenerator((ItemLabelGenerator<Locale>) Locale::getDisplayName);
    defaultLocaleField.setWidthFull();

    ComboBox<SecurityUserTypeEnum> userTypeField = new ComboBox<>();
    userTypeField.setItems(SecurityUserTypeEnum.values());
    userTypeField.setWidthFull();

    MultiselectComboBox<SecurityRole> rolesField = new MultiselectComboBox<>();
    rolesField.setItemLabelGenerator((ItemLabelGenerator<SecurityRole>) SecurityRole::getName);
    rolesField.setItems(SecurityServices.getSecurityRoleService().findAllActive().getData());
    rolesField.setWidthFull();

    // Form layout
    FormLayout editingForm = new FormLayout();
    editingForm.addClassNames(LumoStyles.Padding.Bottom.L,
        LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
    editingForm.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1,
            FormLayout.ResponsiveStep.LabelsPosition.TOP),
        new FormLayout.ResponsiveStep("21em", 2,
            FormLayout.ResponsiveStep.LabelsPosition.TOP));
    editingForm.addFormItem(usernameField, getTranslation("element." + I18N_PREFIX + "username"));
    editingForm.addFormItem(passwordField, getTranslation("element." + I18N_PREFIX + "password"));

    editingForm.addFormItem(firstNameField, getTranslation("element." + I18N_PREFIX + "firstName"));
    editingForm.addFormItem(lastNameField, getTranslation("element." + I18N_PREFIX + "lastName"));

    editingForm.addFormItem(nickNameField, getTranslation("element." + I18N_PREFIX + "nickName"));
    editingForm.addFormItem(emailField, getTranslation("element." + I18N_PREFIX + "email"));

    editingForm.addFormItem(passwordLastModificationField,
        getTranslation("element." + I18N_PREFIX + "passwordLastModification"));
    editingForm
        .addFormItem(lastSuccessfulLoginField,
            getTranslation("element." + I18N_PREFIX + "lastSuccessfulLogin"));

    editingForm
        .addFormItem(isActiveField, getTranslation("element." + I18N_PREFIX + "isActive"));

    editingForm
        .addFormItem(isAccountLockedField,
            getTranslation("element." + I18N_PREFIX + "isAccountLocked"));

    editingForm
        .addFormItem(isActivatedField, getTranslation("element." + I18N_PREFIX + "isActivated"));

    editingForm.addFormItem(isAccountExpiredField,
        getTranslation("element." + I18N_PREFIX + "isAccountExpired"));
    editingForm
        .addFormItem(isCredentialsExpiredField,
            getTranslation("element." + I18N_PREFIX + "isCredentialsExpired"));

    editingForm
        .addFormItem(failedLoginAttemptsField,
            getTranslation("element." + I18N_PREFIX + "failedLoginAttempts"));

    editingForm
        .addFormItem(defaultLocaleField,
            getTranslation("element." + I18N_PREFIX + "defaultLanguage"));
    editingForm.addFormItem(userTypeField, getTranslation("element." + I18N_PREFIX + "userType"));

    FormLayout.FormItem rolesItem = editingForm
        .addFormItem(rolesField, getTranslation("element." + I18N_PREFIX + "roles"));

    UIUtils.setColSpan(2, rolesItem);

    binder.setBean(securityUser);

    binder.forField(usernameField)
        .asRequired(getTranslation("message.securityUser.usernameRequired"))
        .bind(SecurityUser::getUsername, SecurityUser::setUsername);
    binder.forField(passwordField)
        .asRequired(getTranslation("message.securityUser.passwordRequired"))
        .bind(SecurityUser::getPassword, SecurityUser::setPassword);
    binder.bind(firstNameField, SecurityUser::getFirstName, SecurityUser::setFirstName);
    binder.bind(lastNameField, SecurityUser::getLastName, SecurityUser::setLastName);
    binder.bind(emailField, SecurityUser::getEmail, SecurityUser::setEmail);
    binder.bind(nickNameField, SecurityUser::getNickName, SecurityUser::setNickName);
    binder.bind(failedLoginAttemptsField,
        e -> e.getFailedLoginAttempts() == null ? "0" : e.getFailedLoginAttempts().toString(),
        (f, e) -> f.setFailedLoginAttempts(Integer.parseInt(e)));
    binder.bind(passwordLastModificationField, e -> e.getPasswordLastModification() == null ? null
        : e.getPasswordLastModification().atZone(ZoneId.systemDefault()).toLocalDate(), null);
    binder.bind(lastSuccessfulLoginField, e -> e.getLastSuccessfulLogin() == null ? null
        : e.getLastSuccessfulLogin().atZone(ZoneId.systemDefault()).toLocalDate(), null);
    binder.bind(isActiveField, SecurityUser::getIsActive, SecurityUser::setIsActive);
    binder.bind(isAccountExpiredField, SecurityUser::getIsAccountExpired,
        SecurityUser::setIsAccountExpired);
    binder
        .bind(isAccountLockedField, SecurityUser::getIsAccountLocked,
            SecurityUser::setIsAccountLocked);
    binder.bind(isActivatedField, SecurityUser::getIsActivated, SecurityUser::setIsActivated);
    binder.bind(isCredentialsExpiredField, SecurityUser::getIsCredentialsExpired,
        SecurityUser::setIsCredentialsExpired);
    binder.bind(defaultLocaleField, SecurityUser::getDefaultLocale, SecurityUser::setDefaultLocale);
    binder.forField(userTypeField)
        .asRequired(getTranslation("message.securityUser.userTypeRequired"))
        .bind(SecurityUser::getUserType, SecurityUser::setUserType);
    binder.bind(rolesField, SecurityUser::getRoles, SecurityUser::setRoles);

    return editingForm;
  }

  protected void filter(String filter) {
    dataProvider
        .setFilter(new DefaultFilter(
            StringUtils.isBlank(filter) ? null : "*" + filter + "*",
            Boolean.TRUE));
  }
}
