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


package org.wannagoframework.frontend.crud;

import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import java.util.function.UnaryOperator;
import javax.validation.ConstraintViolationException;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.BaseEntity;
import org.wannagoframework.dto.domain.exception.DataIntegrityViolationException;
import org.wannagoframework.dto.domain.exception.EntityNotFoundException;
import org.wannagoframework.dto.domain.exception.OptimisticLockingFailureException;
import org.wannagoframework.dto.domain.exception.UserFriendlyDataException;
import org.wannagoframework.dto.utils.EntityUtil;
import org.wannagoframework.frontend.client.CrudService;
import org.wannagoframework.frontend.security.CurrentUser;
import org.wannagoframework.frontend.utils.messages.CrudErrorMessage;
import org.wannagoframework.frontend.utils.messages.Message;
import org.wannagoframework.frontend.views.EntityView;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-26
 */
public class EntityPresenter<T extends BaseEntity, V extends EntityView<T>>
    implements HasLogger {

  private CrudService<T> crudService;

  private CurrentUser currentUser;

  private V view;

  private EntityPresenterState<T> state = new EntityPresenterState<>();

  public EntityPresenter(
      CrudService<T> crudService, CurrentUser currentUser) {
    this.crudService = crudService;
    this.currentUser = currentUser;
  }

  public V getView() {
    return view;
  }

  public void setView(V view) {
    this.view = view;
  }

  public void delete(CrudOperationListener<T> onSuccess) {
    Message CONFIRM_DELETE = Message.CONFIRM_DELETE.createMessage();
    confirmIfNecessaryAndExecute(true, CONFIRM_DELETE, () -> {
      if (executeOperation(() -> crudService.delete(state.getEntity()))) {
        onSuccess.execute(state.getEntity());
      }
    }, () -> {
    });
  }

  public void save(CrudOperationListener<T> onSuccess) {
    if (executeOperation(this::saveEntity)) {
      onSuccess.execute(state.getEntity());
    }
  }

  public boolean executeUpdate(UnaryOperator<T> updater) {
    return executeOperation(() -> state.updateEntity(updater.apply(getEntity()), isNew()));
  }

  private boolean executeOperation(Runnable operation) {
    try {
      operation.run();
      return true;
    } catch (UserFriendlyDataException e) {
      // Commit failed because of application-level data constraints
      consumeError(e, e.getMessage(), true);
    } catch (DataIntegrityViolationException e) {
      // Commit failed because of validation errors
      consumeError(
          e, CrudErrorMessage.OPERATION_PREVENTED_BY_REFERENCES, true);
    } catch (OptimisticLockingFailureException e) {
      consumeError(e, CrudErrorMessage.CONCURRENT_UPDATE, true);
    } catch (EntityNotFoundException e) {
      consumeError(e, CrudErrorMessage.ENTITY_NOT_FOUND, false);
    } catch (ConstraintViolationException e) {
      consumeError(e, CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
    }
    return false;
  }

  private void consumeError(
      Exception e, String message, boolean isPersistent) {
    logger().debug(message, e);
    view.showError(message, isPersistent);
  }

  private void saveEntity() {
    state.updateEntity(
        crudService.save(state.getEntity()),
        isNew());
  }

  public boolean writeEntity() {
    try {
      view.write(state.getEntity());
      return true;
    } catch (ValidationException e) {
      view.showError(CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
      return false;
    } catch (NullPointerException e) {
      return false;
    }
  }

  public void close() {
    state.clear();
    view.clear();
  }

  public void cancel(Runnable onConfirmed, Runnable onCancelled) {
    confirmIfNecessaryAndExecute(
        view.isDirty(),
        Message.UNSAVED_CHANGES.createMessage(state.getEntityName()),
        () -> {
          view.clear();
          onConfirmed.run();
        }, onCancelled);
  }

  private void confirmIfNecessaryAndExecute(
      boolean needsConfirmation, Message message, Runnable onConfirmed,
      Runnable onCancelled) {
    if (needsConfirmation) {
      showConfirmationRequest(message, onConfirmed, onCancelled);
    } else {
      onConfirmed.run();
    }
  }

  private void showConfirmationRequest(
      Message message, Runnable onOk, Runnable onCancel) {
    view.getConfirmDialog().setText(message.getMessage());
    view.getConfirmDialog().setHeader(message.getCaption());
    view.getConfirmDialog().setCancelText(message.getCancelText());
    view.getConfirmDialog().setConfirmText(message.getOkText());
    view.getConfirmDialog().setOpened(true);

    final Registration okRegistration =
        view.getConfirmDialog().addConfirmListener(e -> onOk.run());
    final Registration cancelRegistration =
        view.getConfirmDialog().addCancelListener(e -> onCancel.run());
    state.updateRegistration(okRegistration, cancelRegistration);
  }

  public boolean loadEntity(Long id, CrudOperationListener<T> onSuccess) {
    return executeOperation(() -> {
      state.updateEntity(crudService.load(id), false);
      onSuccess.execute(state.getEntity());
    });
  }

  public T getEntity() {
    return state.getEntity();
  }

  public boolean isNew() {
    return state.isNew();
  }

  @FunctionalInterface
  public interface CrudOperationListener<T> {

    void execute(T entity);
  }

}

/**
 * Holds variables that change.
 */
class EntityPresenterState<T extends BaseEntity> {

  private T entity;
  private String entityName;
  private Registration okRegistration;
  private Registration cancelRegistration;
  private boolean isNew = false;

  void updateEntity(T entity, boolean isNew) {
    this.entity = entity;
    this.entityName = EntityUtil.getName(this.entity.getClass());
    this.isNew = isNew;
  }

  void updateRegistration(
      Registration okRegistration, Registration cancelRegistration) {
    clearRegistration(this.okRegistration);
    clearRegistration(this.cancelRegistration);
    this.okRegistration = okRegistration;
    this.cancelRegistration = cancelRegistration;
  }

  void clear() {
    this.entity = null;
    this.entityName = null;
    this.isNew = false;
    updateRegistration(null, null);
  }

  private void clearRegistration(Registration registration) {
    if (registration != null) {
      registration.remove();
    }
  }

  public T getEntity() {
    return entity;
  }

  public String getEntityName() {
    return entityName;
  }

  public boolean isNew() {
    return isNew;
  }

}
