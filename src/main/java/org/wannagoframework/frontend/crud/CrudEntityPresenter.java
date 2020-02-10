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

import java.util.function.Consumer;
import javax.validation.ConstraintViolationException;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.BaseEntity;
import org.wannagoframework.dto.domain.exception.DataIntegrityViolationException;
import org.wannagoframework.dto.domain.exception.EntityNotFoundException;
import org.wannagoframework.dto.domain.exception.OptimisticLockingFailureException;
import org.wannagoframework.dto.domain.exception.UserFriendlyDataException;
import org.wannagoframework.frontend.client.CrudService;
import org.wannagoframework.frontend.utils.messages.CrudErrorMessage;
import org.wannagoframework.frontend.views.HasNotifications;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-26
 */
public class CrudEntityPresenter<E extends BaseEntity> implements HasLogger {

  private final CrudService<E> crudService;

  private final HasNotifications view;

  public CrudEntityPresenter(CrudService<E> crudService, HasNotifications view) {
    this.crudService = crudService;
    this.view = view;
  }

  public void delete(E entity, Consumer<E> onSuccess, Consumer<E> onFail) {
    if (executeOperation(() -> crudService.delete(entity))) {
      onSuccess.accept(entity);
    } else {
      onFail.accept(entity);
    }
  }

  public void save(E entity, Consumer<E> onSuccess, Consumer<E> onFail) {
    if (executeOperation(() -> saveEntity(entity))) {
      onSuccess.accept(entity);
    } else {
      onFail.accept(entity);
    }
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

  private void consumeError(Exception e, String message, boolean isPersistent) {
    logger().debug(message, e);
    view.showNotification(message, isPersistent);
  }

  private void saveEntity(E entity) {
    crudService.save(entity);
  }

  public boolean loadEntity(Long id, Consumer<E> onSuccess) {
    return executeOperation(() -> onSuccess.accept(crudService.load(id)));
  }
}
