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


package org.wannagoframework.frontend.components.events;

import com.vaadin.flow.component.HasValue;
import java.util.List;
import org.wannagoframework.dto.domain.BaseEntity;
import org.wannagoframework.frontend.customFields.DefaultCustomListField;

public class CustomListFieldValueChangeEvent<C extends BaseEntity> implements
    HasValue.ValueChangeEvent<List<C>> {

  private final List<C> oldValues;
  private final List<C> newValues;
  private final DefaultCustomListField src;

  public CustomListFieldValueChangeEvent(List<C> oldValues, List<C> newValues,
      DefaultCustomListField src) {
    this.oldValues = oldValues;
    this.newValues = newValues;
    this.src = src;
  }

  @Override
  public HasValue<?, List<C>> getHasValue() {
    return src;
  }

  @Override
  public boolean isFromClient() {
    return true;
  }

  @Override
  public List<C> getOldValue() {
    return oldValues;
  }

  @Override
  public List<C> getValue() {
    return newValues;
  }
}
