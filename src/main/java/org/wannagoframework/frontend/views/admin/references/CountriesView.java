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


package org.wannagoframework.frontend.views.admin.references;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import org.springframework.security.access.annotation.Secured;
import org.wannagoframework.dto.domain.reference.Country;
import org.wannagoframework.dto.domain.reference.IntermediateRegion;
import org.wannagoframework.dto.domain.reference.IntermediateRegionTrl;
import org.wannagoframework.dto.domain.reference.Region;
import org.wannagoframework.dto.domain.reference.RegionTrl;
import org.wannagoframework.dto.domain.reference.SubRegion;
import org.wannagoframework.dto.domain.reference.SubRegionTrl;
import org.wannagoframework.dto.serviceQuery.BaseRemoteQuery;
import org.wannagoframework.dto.serviceQuery.generic.DeleteByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.GetByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.serviceQuery.reference.intermediateRegionTrl.GetIntermediateRegionTrlQuery;
import org.wannagoframework.dto.serviceQuery.reference.regionTrl.GetRegionTrlQuery;
import org.wannagoframework.dto.serviceQuery.reference.subRegionTrl.GetSubRegionTrlQuery;
import org.wannagoframework.dto.utils.AppContext;
import org.wannagoframework.dto.utils.SecurityConst;
import org.wannagoframework.frontend.client.reference.ReferenceServices;
import org.wannagoframework.frontend.customFields.CountryTrlListField;
import org.wannagoframework.frontend.dataproviders.CountryDataProvider;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.renderer.BooleanRenderer;
import org.wannagoframework.frontend.utils.AppConst;
import org.wannagoframework.frontend.utils.LumoStyles;
import org.wannagoframework.frontend.utils.UIUtils;
import org.wannagoframework.frontend.utils.i18n.I18NPageTitle;
import org.wannagoframework.frontend.views.DefaultMasterDetailsView;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-04-21
 */
@I18NPageTitle(messageKey = AppConst.TITLE_COUNTRIES)
@Secured(SecurityConst.ROLE_ADMIN)
public class CountriesView extends DefaultMasterDetailsView<Country, DefaultFilter> {

  public CountriesView() {
    super("country.", Country.class, new CountryDataProvider(),
        (e) -> ReferenceServices.getCountryService().save(new SaveQuery<>(e))
            .getData(),
        e -> ReferenceServices.getCountryService().delete(new DeleteByIdQuery(e.getId())));
  }

  protected Grid createGrid() {
    grid = new Grid<>();
    grid.setSelectionMode(SelectionMode.SINGLE);

    grid.addSelectionListener(event -> event.getFirstSelectedItem()
        .ifPresent(this::showDetails));

    grid.setDataProvider(dataProvider);
    grid.setHeight("100%");

    grid.addColumn(Country::getName).setKey("name")
        .setSortProperty("name." + AppContext.getInstance().getCurrentIso3Language() + ".value");
    grid.addColumn(Country::getIso2).setKey("iso2").setSortProperty("iso3");
    grid.addColumn(Country::getIso3).setKey("iso3").setSortProperty("iso3");
    grid.addColumn(new BooleanRenderer<>(Country::getIsEU)).setKey("isEU");

    grid.addColumn(e -> (e.getRegion() != null) ? e.getRegion().getName() : "").setKey("region")
        .setSortProperty(
            "r.`name." + AppContext.getInstance().getCurrentIso3Language() + ".value`");

    grid.addColumn(e -> (e.getSubRegion() != null ? e.getSubRegion().getName() : ""))
        .setKey("subRegion").setSortProperty(
        "s.`name." + AppContext.getInstance().getCurrentIso3Language() + ".value`");
    grid.addColumn(
        e -> (e.getIntermediateRegion() != null ? e.getIntermediateRegion().getName() : ""))
        .setKey("intermediateRegion").setSortProperty(
        "i.`name." + AppContext.getInstance().getCurrentIso3Language() + ".value`");

    grid.getColumns().forEach(column -> {
      if (column.getKey() != null) {
        column.setHeader(getTranslation("element." + I18N_PREFIX + column.getKey()));
        column.setResizable(true);
        column.setSortable(true);
      }
    });
    return grid;
  }

  protected Component createDetails(Country country) {
    Div mainView = new Div();

    boolean isNew = country.getId() == null;
    detailsDrawerHeader.setTitle(isNew ? getTranslation("element.global.new") + " : "
        : getTranslation("element.global.update") + " : " + country.getIso3());

    detailsDrawerFooter.setDeleteButtonVisible(!isNew);

    TextField iso2 = new TextField();
    iso2.setWidth("100%");

    TextField iso3 = new TextField();
    iso3.setWidth("100%");

    Checkbox isEU = new Checkbox();

    TextField dialingCode = new TextField();
    dialingCode.setWidth("100%");

    ComboBox<Region> region = new ComboBox<>();
    region.setItems(ReferenceServices.getRegionService().findAll().getData());
    region.setItemLabelGenerator(
        (ItemLabelGenerator<Region>) e -> {
          RegionTrl regionTrl = ReferenceServices.getRegionTrlService()
              .getRegionTrl(new GetRegionTrlQuery(e.getId(), getLocale().getLanguage()))
              .getData();
          if (regionTrl != null) {
            return regionTrl.getName();
          } else {
            return "";
          }
        });
    region.setWidth("100%");

    ComboBox<SubRegion> subRegion = new ComboBox<>();
    subRegion.setItems(ReferenceServices.getSubRegionService().findAll().getData());
    subRegion.setItemLabelGenerator(
        (ItemLabelGenerator<SubRegion>) e -> {
          SubRegionTrl subRegionTrl = ReferenceServices.getSubRegionTrlService()
              .getSubRegionTrl(
                  new GetSubRegionTrlQuery(e.getId(), getLocale().getLanguage()))
              .getData();
          if (subRegionTrl != null) {
            return subRegionTrl.getName();
          } else {
            return "";
          }
        });
    subRegion.setWidth("100%");

    ComboBox<IntermediateRegion> intermediateRegion = new ComboBox<>();
    intermediateRegion
        .setItems(ReferenceServices.getIntermediateRegionService().findAll(new BaseRemoteQuery())
            .getData());
    intermediateRegion.setItemLabelGenerator(
        (ItemLabelGenerator<IntermediateRegion>) e -> {
          IntermediateRegionTrl intermediateRegionTrl = ReferenceServices
              .getIntermediateRegionTrlService()
              .getIntermediateRegionTrl(
                  new GetIntermediateRegionTrlQuery(e.getId(), getLocale().getLanguage()))
              .getData();
          if (intermediateRegionTrl != null) {
            return intermediateRegionTrl.getName();
          } else {
            return "";
          }
        });
    intermediateRegion.setWidth("100%");

    RadioButtonGroup<Boolean> isActive = new RadioButtonGroup<>();
    isActive.setItems(true, false);
    isActive.setRenderer(new TextRenderer<>(
        value -> value ? getTranslation("element.global.active")
            : getTranslation("element.global.inactive")));

    CountryTrlListField translations = new CountryTrlListField();
    translations.setReadOnly(false);
    translations.setWidth("100%");

    // Form layout
    FormLayout editingForm = new FormLayout();
    editingForm.addClassNames(LumoStyles.Padding.Bottom.L,
        LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
    editingForm.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1,
            FormLayout.ResponsiveStep.LabelsPosition.TOP),
        new FormLayout.ResponsiveStep("21em", 2,
            FormLayout.ResponsiveStep.LabelsPosition.TOP));

    FormLayout.FormItem iso2Item = editingForm
        .addFormItem(iso2, getTranslation("element." + I18N_PREFIX + "iso2"));
    FormLayout.FormItem iso3Item = editingForm
        .addFormItem(iso3, getTranslation("element." + I18N_PREFIX + "iso3"));
    FormLayout.FormItem isEUItem = editingForm
        .addFormItem(isEU, getTranslation("element." + I18N_PREFIX + "isEU"));
    FormLayout.FormItem dialingCodeItem = editingForm
        .addFormItem(dialingCode, getTranslation("element." + I18N_PREFIX + "dialingCode"));
    FormLayout.FormItem regionItem = editingForm
        .addFormItem(region, getTranslation("element." + I18N_PREFIX + "region"));
    FormLayout.FormItem subRegionItem = editingForm
        .addFormItem(subRegion, getTranslation("element." + I18N_PREFIX + "subRegion"));
    FormLayout.FormItem intermediateRegionItem = editingForm
        .addFormItem(intermediateRegion,
            getTranslation("element." + I18N_PREFIX + "intermediateRegion"));

    FormLayout.FormItem translationsItem = editingForm
        .addFormItem(translations, getTranslation("element." + I18N_PREFIX + "translations"));

    FormLayout.FormItem isActiveItem = editingForm
        .addFormItem(isActive, getTranslation("element." + I18N_PREFIX + "isActive"));

    UIUtils.setColSpan(2, translationsItem, isActiveItem);

    binder.setBean(country);

    binder.bind(iso2, Country::getIso2, Country::setIso2);
    binder.bind(iso3, Country::getIso3, Country::setIso3);
    binder.bind(isEU, Country::getIsEU, Country::setIsEU);
    binder.bind(dialingCode, Country::getDialingCode, Country::setDialingCode);
    binder.bind(region, this::getRegion, Country::setRegion);
    binder.bind(subRegion, this::getSubRegion, Country::setSubRegion);
    binder.bind(intermediateRegion, this::getIntermediateRegion, Country::setIntermediateRegion);
    // binder.bind(translations, country1 -> new ArrayList<>(country1.getTranslations()), (country1, countryTrls) -> country1.setTranslations(new HashSet<>(countryTrls)));

    binder.bind(isActive, Country::getIsActive, Country::setIsActive);

    return editingForm;
  }

  private Region getRegion(Country country) {
    Country c = ReferenceServices.getCountryService().getById(new GetByIdQuery(country.getId()))
        .getData();
    if (c == null || c.getRegion() == null) {
      return null;
    } else {
      return c.getRegion();
    }
  }

  private SubRegion getSubRegion(Country country) {
    Country c = ReferenceServices.getCountryService().getById(new GetByIdQuery(country.getId()))
        .getData();
    if (c == null || c.getSubRegion() == null) {
      return null;
    } else {
      return c.getSubRegion();
    }
  }

  private IntermediateRegion getIntermediateRegion(Country country) {
    Country c = ReferenceServices.getCountryService().getById(new GetByIdQuery(country.getId()))
        .getData();
    if (c == null || c.getIntermediateRegion() == null) {
      return null;
    } else {
      return c.getIntermediateRegion();
    }
  }
}
