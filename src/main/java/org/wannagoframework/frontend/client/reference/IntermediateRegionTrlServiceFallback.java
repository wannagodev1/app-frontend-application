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


package org.wannagoframework.frontend.client.reference;

import org.springframework.stereotype.Component;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.reference.IntermediateRegionTrl;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.serviceQuery.generic.DeleteByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.GetByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.serviceQuery.reference.intermediateRegionTrl.GetIntermediateRegionTrlQuery;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-02
 */
@Component
public class IntermediateRegionTrlServiceFallback implements IntermediateRegionTrlService,
    HasLogger {

  @Override
  public ServiceResult<IntermediateRegionTrl> getIntermediateRegionTrl(
      GetIntermediateRegionTrlQuery query) {
    logger().error(getLoggerPrefix("getIntermediateRegionTrl") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<IntermediateRegionTrl> getById(GetByIdQuery query) {
    logger().error(getLoggerPrefix("getById") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<IntermediateRegionTrl> save(SaveQuery<IntermediateRegionTrl> query) {
    logger().error(getLoggerPrefix("save") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<Void> delete(DeleteByIdQuery query) {
    logger().error(getLoggerPrefix("delete") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }
}
