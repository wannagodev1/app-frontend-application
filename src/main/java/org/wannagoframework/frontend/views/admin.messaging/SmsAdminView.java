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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.wannagoframework.dto.domain.notification.Sms;
import org.wannagoframework.dto.domain.notification.SmsActionEnum;
import org.wannagoframework.dto.domain.notification.SmsStatusEnum;
import org.wannagoframework.dto.utils.SecurityConst;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.dataproviders.SmsDataProvider;
import org.wannagoframework.frontend.utils.AppConst;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.i18n.I18NPageTitle;
import org.wannagoframework.frontend.views.DefaultMasterDetailsView;

@I18NPageTitle(messageKey = AppConst.TITLE_SMS_ADMIN)
@Secured(SecurityConst.ROLE_ADMIN)
public class SmsAdminView extends DefaultMasterDetailsView<Sms, DefaultFilter> {

  public SmsAdminView() {
    super("sms.", Sms.class, new SmsDataProvider());
  }

  protected Grid createGrid() {
    grid = new Grid<>();
    grid.setSelectionMode(SelectionMode.SINGLE);

    grid.addSelectionListener(event -> event.getFirstSelectedItem()
        .ifPresent(this::showDetails));

    grid.setDataProvider(dataProvider);
    grid.setHeight("100%");

    grid.addColumn(Sms::getCreated).setKey("created");
    //grid.addColumn(Sms::getSmsAction).setKey("action");
    grid.addColumn(Sms::getPhoneNumber).setKey("phoneNumber");
    grid.addColumn(Sms::getSmsStatus).setKey("smsStatus");

    grid.getColumns().forEach(column -> {
      if (column.getKey() != null) {
        column.setHeader(getTranslation("element." + I18N_PREFIX + column.getKey()));
        column.setResizable(true);
      }
    });
    return grid;
  }

  protected Component createDetails(Sms sms) {
    boolean isNew = sms.getId() == null;
    detailsDrawerHeader.setTitle(isNew ? getTranslation("element.global.new") + " : "
        : getTranslation("element.global.update") + " : " + sms.getPhoneNumber());

    detailsDrawerFooter.setDeleteButtonVisible(false);

    TextField phoneNumberField = new TextField();
    phoneNumberField.setWidth("100%");

    TextArea bodyField = new TextArea();
    bodyField.setWidth("100%");

    ComboBox<SmsActionEnum> smsActionField = new ComboBox<>();
    smsActionField.setItems(SmsActionEnum.values());

    ComboBox<SmsStatusEnum> smsStatusField = new ComboBox<>();
    smsStatusField.setItems(SmsStatusEnum.values());

    TextArea errorMessageField = new TextArea();
    errorMessageField.setWidth("100%");

    NumberField nbRetryField = new NumberField();

    // Form layout
    FormLayout editingForm = new FormLayout();
    editingForm.addClassNames(LumoStyles.Padding.Bottom.L,
        LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
    editingForm.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1,
            FormLayout.ResponsiveStep.LabelsPosition.TOP),
        new FormLayout.ResponsiveStep("21em", 2,
            FormLayout.ResponsiveStep.LabelsPosition.TOP));
    FormLayout.FormItem phoneNumberFieldItem = editingForm
        .addFormItem(phoneNumberField, getTranslation("element." + I18N_PREFIX + "phoneNumber"));
    FormLayout.FormItem bodyFieldItem = editingForm
        .addFormItem(bodyField, getTranslation("element." + I18N_PREFIX + "body"));
    editingForm.addFormItem(smsActionField, getTranslation("element." + I18N_PREFIX + "smsAction"));
    editingForm.addFormItem(smsStatusField, getTranslation("element." + I18N_PREFIX + "smsStatus"));
    editingForm
        .addFormItem(errorMessageField, getTranslation("element." + I18N_PREFIX + "errorMessage"));

    UIUtils.setColSpan(2, phoneNumberField, bodyFieldItem);

    binder.setBean(sms);

    binder.bind(phoneNumberField, Sms::getPhoneNumber, null);
    binder.bind(bodyField, Sms::getBody, null);
    //binder.bind(smsActionField, Sms::getSmsAction, null);
    binder.bind(smsStatusField, Sms::getSmsStatus, null);
    binder.bind(errorMessageField, Sms::getErrorMessage, null);

    return editingForm;
  }

  protected void filter(String filter) {
    dataProvider
        .setFilter(new DefaultFilter(
            StringUtils.isBlank(filter) ? null : "*" + filter + "*",
            Boolean.TRUE));
  }
}
