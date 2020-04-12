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


package org.wannagoframework.frontend.views.admin.i18n;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.HeaderRow.HeaderCell;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.olli.FileDownloadWrapper;
import org.wannagoframework.dto.domain.i18n.Action;
import org.wannagoframework.dto.domain.i18n.ActionTrl;
import org.wannagoframework.dto.serviceQuery.BaseRemoteQuery;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.serviceQuery.generic.DeleteByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.serviceQuery.i18n.ImportI18NFileQuery;
import org.wannagoframework.dto.serviceQuery.i18n.actionTrl.FindByActionQuery;
import org.wannagoframework.dto.utils.SecurityConst;
import org.wannagoframework.frontend.client.i18n.I18NServices;
import org.wannagoframework.frontend.components.CheckboxColumnComponent;
import org.wannagoframework.frontend.components.ImportFileDialog;
import org.wannagoframework.frontend.customFields.ActionTrlListField;
import org.wannagoframework.frontend.dataproviders.ActionDataProvider;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.utils.AppConst;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.i18n.I18NPageTitle;
import org.wannagoframework.frontend.utils.i18n.MyI18NProvider;
import org.wannagoframework.frontend.views.DefaultMasterDetailsView;
import org.wannagoframework.frontend.views.WannagoMainView;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-04-21
 */
@I18NPageTitle(messageKey = AppConst.PAGE_ACTIONS)
@Secured({SecurityConst.ROLE_I18N, SecurityConst.ROLE_ADMIN})
public class ActionsView extends DefaultMasterDetailsView<Action, DefaultFilter> {
  public ActionsView(MyI18NProvider myI18NProvider) {
    super("action.", Action.class, new ActionDataProvider(),
        (e) -> {
          ServiceResult<Action> _elt = I18NServices.getActionService().save(new SaveQuery<>(e));
          if ( _elt.getIsSuccess() )
            myI18NProvider.reloadActions();
          return _elt;
        },
        e -> I18NServices.getActionService().delete(new DeleteByIdQuery(e.getId())));
  }

  @Override
  protected boolean beforeSave(Action entity) {
    long hasDefault = 0;
    List<ActionTrl> actionTrlList = entity.getTranslations();
    for (ActionTrl actionTrl : actionTrlList) {
      if (actionTrl != null && actionTrl.getIsDefault() != null && actionTrl.getIsDefault()) {
        hasDefault++;
      }
    }
    if (hasDefault == 0) {
      WannagoMainView.get().displayErrorMessage(getTranslation(
          "message.global.translationNeedsDefault"));
    } else if (hasDefault > 1) {
      WannagoMainView.get().displayErrorMessage(getTranslation(
          "message.global.translationMaxDefault"));
    }
    return hasDefault == 1;
  }

  protected Grid createGrid() {
    grid = new Grid<>();
    grid.setSelectionMode(SelectionMode.SINGLE);

    grid.addSelectionListener(event -> event.getFirstSelectedItem()
        .ifPresent(this::showDetails));

    grid.setDataProvider(dataProvider);
    grid.setHeight("100%");

    Column categoryColumn = grid.addColumn(Action::getCategory).setKey("category")
        .setSortable(true);
    Column nameColumn = grid.addColumn(Action::getName).setKey("name").setSortable(true);
    grid.addComponentColumn(action -> new CheckboxColumnComponent(action.getIsTranslated()))
        .setKey("isTranslated").setSortable(true);

    grid.getColumns().forEach(column -> {
      if (column.getKey() != null) {
        column.setHeader(getTranslation("element." + I18N_PREFIX + column.getKey()));
        column.setResizable(true);
      }
    });

    HeaderRow headerRow = grid.prependHeaderRow();

    HeaderCell buttonsCell = headerRow.join(categoryColumn, nameColumn);

    Button exportI18NButton = new Button(getTranslation("action.i18n.download"));
    FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(
        new StreamResource("i18n.xlsx", () -> {
          ServiceResult<Byte[]> result = I18NServices.getI18NService()
              .getI18NFile(new BaseRemoteQuery());
          if (result.getIsSuccess() && result.getData() != null) {
            return new ByteArrayInputStream(ArrayUtils.toPrimitive(result.getData()));
          } else {
            return null;
          }
        }));
    buttonWrapper.wrapComponent(exportI18NButton);

    Button importI18NButton = new Button(getTranslation("action.i18n.upload"));
    importI18NButton.addClickListener(buttonClickEvent -> {
      ImportFileDialog<byte[]> importFileDialog = new ImportFileDialog();
      importFileDialog
          .open(getTranslation("element.i18n.upload"), getTranslation("message.i18n.upload"), null,
              getTranslation("action.i18n.upload"), bytes -> {
                importFileDialog.close();
                ServiceResult<Void> result = I18NServices.getI18NService()
                    .importI18NFile(new ImportI18NFileQuery(ArrayUtils.toObject(bytes)));
                if (result.getIsSuccess()) {
                  WannagoMainView.get()
                      .displayInfoMessage(getTranslation("message.fileImport.success"));
                } else {
                  WannagoMainView.get().displayInfoMessage(
                      getTranslation("message.fileImport.error", result.getMessage()));
                }
              }, () -> importFileDialog.close());
    });

    HorizontalLayout headerHLayout = new HorizontalLayout(buttonWrapper, importI18NButton);
    buttonsCell.setComponent(headerHLayout);

    return grid;
  }

  protected Component createDetails(Action action) {
    boolean isNew = action.getId() == null;
    detailsDrawerHeader.setTitle(isNew ? getTranslation("element.global.new") + " : "
        : getTranslation("element.global.update") + " : " + action.getName());

    detailsDrawerFooter.setDeleteButtonVisible(!isNew);

    TextField name = new TextField();
    name.setWidth("100%");

    TextField categoryField = new TextField();
    categoryField.setWidth("100%");

    Checkbox isActive = new Checkbox();

    Checkbox isTranslated = new Checkbox();

    ActionTrlListField actionTrl = new ActionTrlListField();
    actionTrl.setReadOnly(false);
    actionTrl.setWidth("100%");

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
    FormLayout.FormItem categoryItem = editingForm
        .addFormItem(categoryField, getTranslation("element." + I18N_PREFIX + "category"));
    FormLayout.FormItem translationsItem = editingForm
        .addFormItem(actionTrl, getTranslation("element." + I18N_PREFIX + "translations"));
    editingForm
        .addFormItem(isTranslated, getTranslation("element." + I18N_PREFIX + "isTranslated"));
    editingForm
        .addFormItem(isActive, getTranslation("element." + I18N_PREFIX + "isActive"));

    UIUtils.setColSpan(2, nameItem, categoryItem, translationsItem);

    if (action.getTranslations().size() == 0) {
      action.setTranslations(
          I18NServices.getActionTrlService().findByAction(new FindByActionQuery(action.getId()))
              .getData());
    }

    binder.setBean(action);

    binder.bind(name, Action::getName, Action::setName);
    binder.bind(categoryField, Action::getCategory, Action::setCategory);
    binder.bind(isActive, Action::getIsActive, Action::setIsActive);
    binder.bind(isTranslated, Action::getIsTranslated, Action::setIsTranslated);
    binder.bind(actionTrl, Action::getTranslations, Action::setTranslations);

    return editingForm;
  }

  protected void filter(String filter) {
    dataProvider
        .setFilter(new DefaultFilter(
            StringUtils.isBlank(filter) ? null : "%" + filter + "%",
            Boolean.TRUE));
  }
}
