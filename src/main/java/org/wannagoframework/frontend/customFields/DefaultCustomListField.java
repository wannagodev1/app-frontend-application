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

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudI18n;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.BaseEntity;
import org.wannagoframework.frontend.components.FlexBoxLayout;
import org.wannagoframework.frontend.components.events.CustomListFieldValueChangeEvent;
import org.wannagoframework.frontend.dataproviders.DefaultBackend;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-04-21
 */
public abstract class DefaultCustomListField<C extends BaseEntity> extends FlexBoxLayout
    implements HasValue<CustomListFieldValueChangeEvent<C>, List<C>>, HasLogger, Serializable {

  protected final String i18nPrefix;
  protected Crud<C> gridCrud;
  protected Button newButton;
  protected DefaultBackend<C> dataProvider;
  protected Grid.Column editColumn;
  private List<ValueChangeListener<? super CustomListFieldValueChangeEvent<C>>> changeListeners = new ArrayList<>();

  protected DefaultCustomListField(String i18nPrefix) {
    this.i18nPrefix = i18nPrefix;
  }

  protected static String createEditColumnTemplate(String crudI18n) {
    return "<vaadin-crud-edit aria-label=\"" + crudI18n + "\"></vaadin-crud-edit>";
  }

  @Override
  public List<C> getValue() {
    String loggerPrefix = getLoggerPrefix("generateModelValue");
    logger().debug(loggerPrefix + "Result =  " + dataProvider.getValues());
    return new ArrayList<>(dataProvider.getValues());
  }

  @Override
  public void setValue(List<C> values) {
    String loggerPrefix = getLoggerPrefix("setPresentationValue");
    logger().debug(loggerPrefix + "Param =  " + values);
    dataProvider.setValues(values);
  }

  @Override
  public Registration addValueChangeListener(
      ValueChangeListener<? super CustomListFieldValueChangeEvent<C>> valueChangeListener) {
    changeListeners.add(valueChangeListener);
    return () -> changeListeners.remove(valueChangeListener);
  }

  protected CrudI18n createI18n() {
    Locale currentLocal = UI.getCurrent().getLocale();
    CrudI18n i18nGrid = CrudI18n.createDefault();

    i18nGrid.setNewItem(getTranslation("action.global.addButton", currentLocal));
    i18nGrid.setEditItem(getTranslation("action.global.editButton", currentLocal));
    i18nGrid.setSaveItem(getTranslation("action.global.saveButton", currentLocal));
    i18nGrid.setDeleteItem(getTranslation("action.global.deleteButton", currentLocal));
    i18nGrid.setCancel(getTranslation("action.global.cancel", currentLocal));
    i18nGrid.setEditLabel(getTranslation("action.global.editButton", currentLocal));

    i18nGrid.getConfirm().getCancel()
        .setTitle(getTranslation("element.global.cancel.title", currentLocal));
    i18nGrid.getConfirm().getCancel()
        .setContent(getTranslation("element.global.cancel.content", currentLocal));
    i18nGrid.getConfirm().getCancel().getButton()
        .setDismiss(getTranslation("action.global.cancel.dismissButton", currentLocal));
    i18nGrid.getConfirm().getCancel().getButton()
        .setConfirm(getTranslation("action.global.cancel.confirmButton", currentLocal));

    i18nGrid.getConfirm().getDelete()
        .setTitle(getTranslation("element.global.delete.title", currentLocal));
    i18nGrid.getConfirm().getDelete()
        .setContent(getTranslation("element.global.delete.content", currentLocal));
    i18nGrid.getConfirm().getDelete().getButton()
        .setDismiss(getTranslation("action.global.delete.dismissButton", currentLocal));
    i18nGrid.getConfirm().getDelete().getButton()
        .setConfirm(getTranslation("action.global.delete.confirmButton", currentLocal));

    return i18nGrid;
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  @Override
  public void setReadOnly(boolean b) {
  }

  @Override
  public boolean isRequiredIndicatorVisible() {
    return false;
  }

  @Override
  public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
  }

  public void updateValue(List<C> oldValues, List<C> newValues) {
    changeListeners.forEach(valueChangeListener -> valueChangeListener
        .valueChanged(new CustomListFieldValueChangeEvent<>(oldValues, newValues, this)));
  }

  public class Backend extends DefaultBackend<C> {

    private AtomicLong uniqueLong = new AtomicLong();

    @Override
    public Object getId(C item) {
      return item.getId();
    }

    public void setValues(Collection<C> values) {
      fieldsMap.clear();
      fieldsMap.addAll(values);
    }

    public void persist(C value) {
      List<C> previousValues = new ArrayList<>(fieldsMap);

      if (value.getId() == null) {
        value.setId(uniqueLong.incrementAndGet());
        value.setIsNew(true);
      }
      if (!fieldsMap.contains(value)) {
        fieldsMap.add(value);
      }
      updateValue(previousValues, fieldsMap);
    }

    public void delete(C value) {
      List<C> previousValues = new ArrayList<>(fieldsMap);

      fieldsMap.remove(value);

      updateValue(previousValues, fieldsMap);
    }
  }
}