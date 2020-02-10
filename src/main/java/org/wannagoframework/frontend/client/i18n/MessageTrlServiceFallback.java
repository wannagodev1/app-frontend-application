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


package org.wannagoframework.frontend.client.i18n;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.i18n.MessageTrl;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.serviceQuery.generic.DeleteByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.GetByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.serviceQuery.i18n.FindByIso3Query;
import org.wannagoframework.dto.serviceQuery.i18n.GetByNameAndIso3Query;
import org.wannagoframework.dto.serviceQuery.i18n.messageTrl.CountByMessageQuery;
import org.wannagoframework.dto.serviceQuery.i18n.messageTrl.FindByMessageQuery;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-02
 */
@Component
public class MessageTrlServiceFallback implements MessageTrlService, HasLogger {

  @Override
  public ServiceResult<List<MessageTrl>> findByMessage(FindByMessageQuery query) {
    logger().error(getLoggerPrefix("findByMessage") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", Collections.emptyList());
  }

  @Override
  public ServiceResult<Long> countByMessage(CountByMessageQuery query) {
    logger().error(getLoggerPrefix("countByMessage") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", 0L);
  }

  @Override
  public ServiceResult<List<MessageTrl>> findByIso3(FindByIso3Query query) {
    logger().error(getLoggerPrefix("findByIso3") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", Collections.emptyList());
  }

  @Override
  public ServiceResult<MessageTrl> getByNameAndIso3(GetByNameAndIso3Query query) {
    logger().error(getLoggerPrefix("getByNameAndIso3") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<MessageTrl> getById(GetByIdQuery query) {
    logger().error(getLoggerPrefix("getById") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<MessageTrl> save(SaveQuery<MessageTrl> query) {
    logger().error(getLoggerPrefix("save") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<Void> delete(DeleteByIdQuery query) {
    logger().error(getLoggerPrefix("delete") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }
}