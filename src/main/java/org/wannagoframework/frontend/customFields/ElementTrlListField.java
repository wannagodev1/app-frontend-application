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


package org.wannagoframework.frontend.customFields;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import java.io.Serializable;
import java.util.Locale;
import org.wannagoframework.dto.domain.i18n.ElementTrl;
import org.wannagoframework.frontend.renderer.BooleanRenderer;
import org.wannagoframework.frontend.utils.i18n.MyI18NProvider;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-02-14
 */
public class ElementTrlListField extends
    DefaultCustomListField<ElementTrl> implements
    Serializable {

  public ElementTrlListField() {
    super("elementTrl.");

    dataProvider = new Backend();

    add(initContent());

    getElement().setAttribute("colspan", "2");
  }

  public void setReadOnly(boolean readOnly) {
    super.setReadOnly(readOnly);
    if (gridCrud != null) {
      gridCrud.getGrid().setEnabled(!readOnly);
      newButton.setEnabled(!readOnly);
      editColumn.setVisible(!readOnly);
    }
  }

  protected Component initContent() {
    Grid<ElementTrl> grid = new Grid<>();

    gridCrud = new Crud(ElementTrl.class, grid, createInterfaceTrlEditor());
    gridCrud.setMinHeight("300px");
    gridCrud.setWidth("100%");
    gridCrud.setI18n(createI18n());
    gridCrud.getGrid().setEnabled(false);
    gridCrud.setDataProvider(dataProvider);
    gridCrud.addSaveListener(e -> dataProvider.persist(e.getItem()));
    gridCrud.addDeleteListener(e -> dataProvider.delete(e.getItem()));

    editColumn = grid.addColumn(TemplateRenderer.of(createEditColumnTemplate("Edit")))
        .setKey("vaadin-crud-edit-column").setWidth("4em").setFlexGrow(0);

    grid.addColumn(ElementTrl::getValue)
        .setHeader(getTranslation("element." + i18nPrefix + "value"));
    grid.addColumn(new TextRenderer<>(
        row -> row.getIso3Language() == null ? ""
            : (new Locale(row.getIso3Language())).getDisplayLanguage(getLocale())))
        .setHeader(getTranslation("element." + i18nPrefix + "language"));

    grid.addColumn(new BooleanRenderer<>(ElementTrl::getIsDefault))
        .setHeader(getTranslation("element." + i18nPrefix + "isDefault"));

    newButton = new Button(getTranslation("action.global.addButton"));
    newButton.getElement().setAttribute("theme", "primary");
    newButton
        .addClickListener(
            event -> gridCrud.getElement().executeJs("$0.__openEditor($1)", gridCrud, "'new'"));
    gridCrud.setToolbar(newButton);

    newButton.setEnabled(false);
    editColumn.setVisible(false);

    return gridCrud;
  }

  protected CrudEditor<ElementTrl> createInterfaceTrlEditor() {
    TextField value = new TextField(getTranslation("element." + i18nPrefix + "value"));

    ComboBox<Locale> language = new ComboBox<>(
        getTranslation("element." + i18nPrefix + "language"),
        MyI18NProvider.getAvailableLanguages(getLocale()));
    language.setItemLabelGenerator(Locale::getDisplayLanguage);

    Checkbox isDefault = new Checkbox(getTranslation("element." + i18nPrefix + "isDefault"));
    Checkbox isTranslated = new Checkbox(getTranslation("element." + i18nPrefix + "isTranslated"));

    FormLayout form = new FormLayout(value, isDefault, isTranslated, language);

    Binder<ElementTrl> binder = new BeanValidationBinder<>(ElementTrl.class);
    binder.forField(value).asRequired().bind(ElementTrl::getValue, ElementTrl::setValue);
    binder.forField(isDefault).bind(ElementTrl::getIsDefault, ElementTrl::setIsDefault);
    binder.forField(isTranslated).bind(ElementTrl::getIsTranslated, ElementTrl::setIsTranslated);
    binder.forField(language).asRequired()
        .bind((elementTrl) -> elementTrl.getIso3Language() == null ? null
                : new Locale(elementTrl.getIso3Language()),
            (elementTrl, locale) -> elementTrl.setIso3Language(locale.getLanguage()));

    return new BinderCrudEditor<>(binder, form);
  }
}


