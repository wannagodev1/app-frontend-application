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
import org.wannagoframework.dto.domain.notification.Mail;
import org.wannagoframework.dto.domain.notification.MailActionEnum;
import org.wannagoframework.dto.domain.notification.MailStatusEnum;
import org.wannagoframework.dto.utils.SecurityConst;
import org.wannagoframework.frontend.customFields.AttachmentField;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.dataproviders.MailDataProvider;
import org.wannagoframework.frontend.utils.AppConst;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.i18n.I18NPageTitle;
import org.wannagoframework.frontend.views.DefaultMasterDetailsView;

@I18NPageTitle(messageKey = AppConst.TITLE_MAILS_ADMIN)
@Secured(SecurityConst.ROLE_ADMIN)
public class MailAdminView extends DefaultMasterDetailsView<Mail, DefaultFilter> {

  public MailAdminView() {
    super("mail.", Mail.class, new MailDataProvider());
  }

  protected Grid createGrid() {
    grid = new Grid<>();
    grid.setSelectionMode(SelectionMode.SINGLE);

    grid.addSelectionListener(event -> event.getFirstSelectedItem()
        .ifPresent(this::showDetails));

    grid.setDataProvider(dataProvider);
    grid.setHeight("100%");

    grid.addColumn(Mail::getCreated).setKey("created");
    //grid.addColumn(Mail::getMailAction).setKey("action");
    grid.addColumn(Mail::getTo).setKey("to");
    grid.addColumn(Mail::getMailStatus).setKey("mailStatus");

    grid.getColumns().forEach(column -> {
      if (column.getKey() != null) {
        column.setHeader(getTranslation("element." + I18N_PREFIX + column.getKey()));
        column.setResizable(true);
      }
    });
    return grid;
  }

  protected Component createDetails(Mail mail) {
    boolean isNew = mail.getId() == null;
    detailsDrawerHeader.setTitle(isNew ? getTranslation("element.global.new") + " : "
        : getTranslation("element.global.update") + " : " + mail.getTo());

    detailsDrawerFooter.setDeleteButtonVisible(false);

    TextField fromField = new TextField();
    fromField.setWidth("100%");

    TextField toField = new TextField();
    toField.setWidth("100%");

    TextField copyToField = new TextField();
    copyToField.setWidth("100%");

    TextField subjectField = new TextField();
    subjectField.setWidth("100%");

    TextArea bodyField = new TextArea();
    bodyField.setWidth("100%");

    AttachmentField attachmentField = new AttachmentField();
    attachmentField.setWidth("100%");

    ComboBox<MailActionEnum> mailActionField = new ComboBox<>();
    mailActionField.setItems(MailActionEnum.values());

    ComboBox<MailStatusEnum> mailStatusField = new ComboBox<>();
    mailStatusField.setItems(MailStatusEnum.values());

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
    FormLayout.FormItem fromFieldItem = editingForm
        .addFormItem(fromField, getTranslation("element." + I18N_PREFIX + "from"));
    FormLayout.FormItem toFieldItem = editingForm
        .addFormItem(toField, getTranslation("element." + I18N_PREFIX + "to"));
    FormLayout.FormItem copyToFieldItem = editingForm
        .addFormItem(copyToField, getTranslation("element." + I18N_PREFIX + "copyTo"));
    FormLayout.FormItem subjectFieldItem = editingForm
        .addFormItem(subjectField, getTranslation("element." + I18N_PREFIX + "subject"));
    FormLayout.FormItem bodyFieldItem = editingForm
        .addFormItem(bodyField, getTranslation("element." + I18N_PREFIX + "body"));
    FormLayout.FormItem attachmentFieldItem = editingForm
        .addFormItem(attachmentField, getTranslation("element." + I18N_PREFIX + "attachment"));
    editingForm
        .addFormItem(mailActionField, getTranslation("element." + I18N_PREFIX + "mailAction"));
    editingForm
        .addFormItem(mailStatusField, getTranslation("element." + I18N_PREFIX + "mailStatus"));
    editingForm
        .addFormItem(errorMessageField, getTranslation("element." + I18N_PREFIX + "errorMessage"));
    editingForm.addFormItem(nbRetryField, getTranslation("element." + I18N_PREFIX + "nbRetry"));

    UIUtils
        .setColSpan(2, fromFieldItem, toFieldItem, copyToFieldItem, subjectFieldItem, bodyFieldItem,
            attachmentFieldItem);

    binder.setBean(mail);

    binder.bind(fromField, Mail::getFrom, null);
    binder.bind(toField, Mail::getTo, null);
    binder.bind(copyToField, Mail::getCopyTo, null);
    binder.bind(subjectField, Mail::getSubject, null);
    binder.bind(bodyField, Mail::getBody, null);
    // binder.bind(attachmentField, Mail::getAttachements, Mail::setAttachements);
    //binder.bind(mailActionField, Mail::getMailAction, null);
    binder.bind(mailStatusField, Mail::getMailStatus, null);
    binder.bind(errorMessageField, Mail::getErrorMessage, null);
    binder.bind(nbRetryField, (e) -> e.getNbRetry().doubleValue(), null);

    return editingForm;
  }

  protected void filter(String filter) {
    dataProvider
        .setFilter(new DefaultFilter(
            StringUtils.isBlank(filter) ? null : "*" + filter + "*",
            Boolean.TRUE));
  }
}
