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
import org.wannagoframework.dto.utils.StoredFile;
import org.wannagoframework.frontend.customFields.AttachmentField;

public class AttachmentsFieldValueChangeEvent implements HasValue.ValueChangeEvent<StoredFile[]> {

  private final StoredFile[] oldValue;
  private final StoredFile[] value;
  private final AttachmentField src;

  public AttachmentsFieldValueChangeEvent(StoredFile[] oldValue, StoredFile[] value,
      AttachmentField src) {
    this.oldValue = oldValue;
    this.value = value;
    this.src = src;
  }

  @Override
  public HasValue<?, StoredFile[]> getHasValue() {
    return src;
  }

  @Override
  public boolean isFromClient() {
    return true;
  }

  @Override
  public StoredFile[] getOldValue() {
    return oldValue;
  }

  @Override
  public StoredFile[] getValue() {
    return value;
  }
}
