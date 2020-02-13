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
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.security.access.annotation.Secured;
import org.wannagoframework.dto.domain.i18n.Element;
import org.wannagoframework.dto.serviceQuery.generic.DeleteByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.serviceQuery.i18n.elementTrl.FindByElementQuery;
import org.wannagoframework.dto.utils.SecurityConst;
import org.wannagoframework.frontend.client.i18n.I18NServices;
import org.wannagoframework.frontend.customFields.ElementTrlListField;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.dataproviders.ElementDataProvider;
import org.wannagoframework.frontend.renderer.BooleanRenderer;
import org.wannagoframework.frontend.utils.AppConst;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.i18n.I18NPageTitle;
import org.wannagoframework.frontend.utils.i18n.MyI18NProvider;
import org.wannagoframework.frontend.views.DefaultMasterDetailsView;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-04-21
 */
@I18NPageTitle(messageKey = AppConst.TITLE_ELEMENTS)
@Secured({SecurityConst.ROLE_I18N, SecurityConst.ROLE_ADMIN})
public class ElementsView extends DefaultMasterDetailsView<Element, DefaultFilter> {

  public ElementsView(MyI18NProvider myI18NProvider) {
    super("element.", Element.class, new ElementDataProvider(),
        (e) -> {
          Element elt = I18NServices.getElementService().save(new SaveQuery<>(e))
              .getData();
          myI18NProvider.reloadElements();
          return elt;
        },
        e -> I18NServices.getElementService().delete(new DeleteByIdQuery(e.getId())));

  }

  protected Grid createGrid() {
    grid = new Grid<>();
    grid.setSelectionMode(SelectionMode.SINGLE);

    grid.addSelectionListener(event -> event.getFirstSelectedItem()
        .ifPresent(this::showDetails));

    grid.setDataProvider(dataProvider);
    grid.setHeight("100%");

    grid.addColumn(Element::getName).setKey("name").setSortable(true);
    grid.addColumn(new BooleanRenderer<>(Element::getIsTranslated))
        .setKey("isTranslated").setSortable(true);

    grid.getColumns().forEach(column -> {
      if (column.getKey() != null) {
        column.setHeader(getTranslation("element." + I18N_PREFIX + column.getKey()));
        column.setResizable(true);
      }
    });
    return grid;
  }

  protected Component createDetails(Element element) {
    boolean isNew = element.getId() == null;
    detailsDrawerHeader.setTitle(isNew ? getTranslation("element.global.new") + " : "
        : getTranslation("element.global.update") + " : " + element.getName());

    detailsDrawerFooter.setDeleteButtonVisible(!isNew);

    TextField name = new TextField();
    name.setWidth("100%");

    Checkbox isActive = new Checkbox();

    Checkbox isTranslated = new Checkbox();

    ElementTrlListField elementTrl = new ElementTrlListField();
    elementTrl.setReadOnly(false);
    elementTrl.setWidth("100%");

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
    FormLayout.FormItem translationsItem = editingForm
        .addFormItem(elementTrl, getTranslation("element." + I18N_PREFIX + "translations"));
    editingForm
        .addFormItem(isTranslated, getTranslation("element." + I18N_PREFIX + "isTranslated"));
    editingForm
        .addFormItem(isActive, getTranslation("element." + I18N_PREFIX + "isActive"));

    UIUtils.setColSpan(2, nameItem, translationsItem);

    if (element.getTranslations().size() == 0) {
      element.setTranslations(
          I18NServices.getElementTrlService().findByElement(new FindByElementQuery(element.getId()))
              .getData());
    }
    binder.setBean(element);

    binder.bind(name, Element::getName, Element::setName);
    binder.bind(isActive, Element::getIsActive, Element::setIsActive);
    binder.bind(isTranslated, Element::getIsTranslated, Element::setIsTranslated);
    binder.bind(elementTrl, Element::getTranslations, Element::setTranslations);

    return editingForm;
  }
}
