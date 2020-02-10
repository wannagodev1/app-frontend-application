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


package org.wannagoframework.frontend.views;

import com.vaadin.flow.data.binder.ValidationException;

/**
 * A master / detail view for entities of the type <code>T</code>. The view has a list of entities
 * (the 'master' part) and a dialog to show a single entity (the 'detail' part). The dialog has two
 * modes: a view mode and an edit mode.
 * <p>
 * The view can also show notifications, error messages, and confirmation requests.
 *
 * @param <T> the entity type
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-06
 */
public interface EntityView<T> extends HasConfirmation, HasNotifications {

  /**
   * Shows an error notification with a given text.
   *
   * @param message a user-friendly error message
   * @param isPersistent if <code>true</code> the message requires a user action to disappear (if
   * <code>false</code> it disappears automatically after some time)
   */
  default void showError(String message, boolean isPersistent) {
    showNotification(message, isPersistent);
  }

  /**
   * Returns the current dirty state of the entity dialog.
   *
   * @return <code>true</code> if the entity dialog is open in the 'edit'
   * mode and has unsaved changes
   */
  boolean isDirty();

  /**
   * Remove the reference to the entity and reset dirty status.
   */
  void clear();

  /**
   * Writes the changes from the entity dialog into the given entity instance (see {@link
   * com.vaadin.flow.data.binder.Binder#writeBean(Object)})
   *
   * @param entity the entity instance to save the changes into
   * @throws ValidationException if the values entered into the entity dialog cannot be converted
   * into entity properties
   */
  void write(T entity) throws ValidationException;

  String getEntityName();

  default void showCreatedNotification() {
    showNotification(getEntityName() + " was created");
  }

  default void showUpdatedNotification() {
    showNotification(getEntityName() + " was updated");
  }

  default void showDeletedNotification() {
    showNotification(getEntityName() + " was deleted");
  }
}
