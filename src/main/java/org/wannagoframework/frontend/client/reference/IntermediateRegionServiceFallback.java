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

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.reference.IntermediateRegion;
import org.wannagoframework.dto.serviceQuery.BaseRemoteQuery;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.serviceQuery.generic.CountAnyMatchingQuery;
import org.wannagoframework.dto.serviceQuery.generic.DeleteByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.FindAnyMatchingQuery;
import org.wannagoframework.dto.serviceQuery.generic.GetByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.utils.Page;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-02
 */
@Component
public class IntermediateRegionServiceFallback implements IntermediateRegionService, HasLogger {

  @Override
  public ServiceResult<List<IntermediateRegion>> findAll(BaseRemoteQuery query) {
    logger().error(getLoggerPrefix("findAll") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server",
        Collections.emptyList());
  }

  @Override
  public ServiceResult<Page<IntermediateRegion>> findAnyMatching(FindAnyMatchingQuery query) {
    logger().error(getLoggerPrefix("findAnyMatching") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", new Page<>());
  }

  @Override
  public ServiceResult<Long> countAnyMatching(CountAnyMatchingQuery query) {
    logger().error(getLoggerPrefix("countAnyMatching") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", 0L);
  }

  @Override
  public ServiceResult<IntermediateRegion> getById(GetByIdQuery query) {
    logger().error(getLoggerPrefix("getById") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<IntermediateRegion> save(SaveQuery<IntermediateRegion> query) {
    logger().error(getLoggerPrefix("save") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<Void> delete(DeleteByIdQuery query) {
    logger().error(getLoggerPrefix("delete") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }
}
