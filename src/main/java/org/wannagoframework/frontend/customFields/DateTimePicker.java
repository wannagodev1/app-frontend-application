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

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.timepicker.TimePicker;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.wannagoframework.frontend.components.FlexBoxLayout;
import org.wannagoframework.frontend.layout.size.Right;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-05-27
 */
public class DateTimePicker extends CustomField<LocalDateTime> implements Serializable {

  private final DatePicker datePicker = new DatePicker();
  private final TimePicker timePicker = new TimePicker();

  public DateTimePicker() {
    this(null);
  }

  public DateTimePicker(String label) {
    if (label != null) {
      setLabel(label);
    }
    datePicker.setWidth("150px");
    timePicker.setWidth("130px");
    FlexBoxLayout layout = new FlexBoxLayout(datePicker, timePicker);
    layout.setSpacing(Right.XS);
    add(layout);
  }

  @Override
  protected LocalDateTime generateModelValue() {
    final LocalDate date = datePicker.getValue();
    LocalTime time = timePicker.getValue();
    if (  time == null )
      time = LocalTime.of(0,0);
    return date != null && time != null ?
        LocalDateTime.of(date, time) :
        null;
  }

  @Override
  protected void setPresentationValue(
      LocalDateTime newPresentationValue) {
    datePicker.setValue(newPresentationValue != null ?
        newPresentationValue.toLocalDate() :
        null);
    timePicker.setValue(newPresentationValue != null ?
        newPresentationValue.toLocalTime() :
        null);

  }

}