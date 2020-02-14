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
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.RouterLayout;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.notification.MailTemplate;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.serviceQuery.generic.GetByStrIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.utils.SecurityConst;
import org.wannagoframework.frontend.client.notification.NotificationServices;
import org.wannagoframework.frontend.components.FlexBoxLayout;
import org.wannagoframework.frontend.components.navigation.bar.AppBar;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.layout.ViewFrame;
import org.wannagoframework.frontend.layout.size.Horizontal;
import org.wannagoframework.frontend.layout.size.Top;
import org.wannagoframework.frontend.utils.AppConst;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.css.BoxSizing;
import org.wannagoframework.frontend.utils.i18n.I18NPageTitle;
import org.wannagoframework.frontend.views.WannagoMainView;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-08-14
 */
@Tag("mail-template-admin-view")
@I18NPageTitle(messageKey = AppConst.TITLE_MAIL_TEMPLATE_ADMIN)
@Secured(SecurityConst.ROLE_ADMIN)
public class MailTemplateAdminView extends ViewFrame implements RouterLayout, HasLogger,
    HasUrlParameter<String> {

  private Binder<MailTemplate> binder = new Binder<>();
  private MailTemplate mailTemplate;
  private DefaultDataProvider<MailTemplate, DefaultFilter> securityUserDataProvider;

  public MailTemplateAdminView() {
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    AppBar appBar = initAppBar();
    String title = mailTemplate == null ? "" : mailTemplate.getName();
    appBar.setTitle(title);

    setViewContent(createContent());
    setViewFooter(getFooter());
  }

  private AppBar initAppBar() {
    AppBar appBar = WannagoMainView.get().getAppBar();
    appBar.setNaviMode(AppBar.NaviMode.CONTEXTUAL);
    appBar.getContextIcon().addClickListener(event -> goBack());
    return appBar;
  }

  protected Component getFooter() {
    Button saveButton = new Button(getTranslation("action.global.saveButton"));
    saveButton.addClickListener(event -> save());

    Button cancelButton = new Button(getTranslation("action.global.cancelButton"));
    cancelButton.addClickListener(event -> cancel());

    HorizontalLayout footer = new HorizontalLayout(saveButton, cancelButton);
    footer.setPadding(true);
    footer.setSpacing(true);

    return footer;
  }

  private Component createContent() {
    FlexBoxLayout content = new FlexBoxLayout(getSettingsForm());
    content.setBoxSizing(BoxSizing.BORDER_BOX);
    content.setHeightFull();
    content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
    return content;
  }

  public Component getSettingsForm() {
    FormLayout formLayout = new FormLayout();
    formLayout.addClassNames(LumoStyles.Padding.Bottom.L,
        LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
    formLayout.setResponsiveSteps(
        new ResponsiveStep("25em", 1,
            ResponsiveStep.LabelsPosition.TOP),
        new ResponsiveStep("32em", 2,
            ResponsiveStep.LabelsPosition.TOP));

    TextField nameField = new TextField();
    nameField.setWidthFull();

    TextField subjectField = new TextField();
    subjectField.setWidthFull();

    EmailField fromField = new EmailField();
    fromField.setWidthFull();

    EmailField copyToField = new EmailField();
    copyToField.setWidthFull();

    RichTextEditor bodyField = new RichTextEditor();
    bodyField.setWidthFull();

    TextField bodyAsHtml = new TextField();
    bodyAsHtml.setVisible(false);
    bodyField.addValueChangeListener(value -> {
      try {
        bodyAsHtml.setValue(bodyField.getHtmlValue());
      } catch (Exception ignored) {
      }
    });

    TextField mailActionField = new TextField();
    mailActionField.setWidthFull();

    formLayout.addFormItem(nameField, getTranslation("element.mailTemplate.name"));
    formLayout.addFormItem(mailActionField, getTranslation("element.mailTemplate.action"));
    formLayout.addFormItem(subjectField, getTranslation("element.mailTemplate.subject"));
    formLayout.addFormItem(fromField, getTranslation("element.mailTemplate.from"));
    formLayout.addFormItem(copyToField, getTranslation("element.mailTemplate.copyTo"));

    FormItem bodyFieldItem = formLayout
        .addFormItem(bodyField, getTranslation("element.mailTemplate.body"));

    UIUtils.setColSpan(2, bodyFieldItem);

    binder.forField(nameField).asRequired(getTranslation("message.error.nameRequired"))
        .bind(MailTemplate::getName, MailTemplate::setName);
    binder.forField(subjectField).asRequired(getTranslation("message.error.subjectRequired"))
        .bind(MailTemplate::getSubject, MailTemplate::setSubject);
    binder.forField(fromField).asRequired(getTranslation("message.error.fromRequired"))
        .bind(MailTemplate::getFrom, MailTemplate::setFrom);
    binder.bind(copyToField, MailTemplate::getCopyTo, MailTemplate::setCopyTo);
    binder.forField(bodyField.asHtml()).asRequired(getTranslation("message.error.bodyRequired"))
        .bind(MailTemplate::getBody, MailTemplate::setBody);
    binder.forField(bodyAsHtml).bind(MailTemplate::getBodyHtml, MailTemplate::setBodyHtml);
    binder.forField(mailActionField).asRequired(getTranslation("message.error.mailAction"))
        .bind(MailTemplate::getMailAction, MailTemplate::setMailAction);

    Div content = new Div(formLayout);
    content.addClassName("grid-view");
    return content;
  }

  protected void save() {
    String id = binder.getBean().getId();

    boolean isNew = id == null;

    if (binder.writeBeanIfValid(mailTemplate)) {
      ServiceResult<MailTemplate> _mailTemplate = NotificationServices.getMailTemplateService()
          .save(new SaveQuery<>(mailTemplate));
      if (_mailTemplate.getIsSuccess() && _mailTemplate.getData() != null) {
        WannagoMainView.get().displayInfoMessage(getTranslation("message.global.recordSavedMessage"));
        mailTemplate = _mailTemplate.getData();
        if (isNew) {
          UI.getCurrent().navigate(MailTemplateAdminView.class, mailTemplate.getId());
        } else {
          binder.readBean(mailTemplate);
        }
      } else {
        WannagoMainView.get()
            .displayErrorMessage(
                getTranslation("message.global.error", _mailTemplate.getMessage()));
        // Notification.show(getTranslation("message.global.error", _user.getMessage()), 3000, Position.MIDDLE);
      }
    } else {
      WannagoMainView.get()
          .displayErrorMessage(getTranslation("message.global.validationErrorMessage"));
      // Notification.show(getTranslation("message.global.validationErrorMessage"), 3000, Notification.Position.BOTTOM_CENTER);
    }
  }

  protected void cancel() {
    binder.readBean(mailTemplate);
  }

  @Override
  public void setParameter(BeforeEvent beforeEvent, String id) {
    if (StringUtils.isBlank(id) || "-1".equals(id)) {
      mailTemplate = new MailTemplate();
    } else {
      mailTemplate = NotificationServices.getMailTemplateService().getById(new GetByStrIdQuery(id))
          .getData();
      if (mailTemplate == null) {
        mailTemplate = new MailTemplate();
      }
    }

    binder.setBean(mailTemplate);
  }

  private void goBack() {
    UI.getCurrent().navigate(MailTemplatesAdminView.class);
  }
}
