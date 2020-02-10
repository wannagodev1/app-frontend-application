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


package org.wannagoframework.frontend.client.security;

import feign.hystrix.FallbackFactory;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.security.SecurityRole;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.serviceQuery.generic.CountAnyMatchingQuery;
import org.wannagoframework.dto.serviceQuery.generic.DeleteByStrIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.FindAnyMatchingQuery;
import org.wannagoframework.dto.serviceQuery.generic.GetByStrIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.serviceQuery.security.securityRole.GetSecurityRoleByNameQuery;
import org.wannagoframework.dto.utils.Page;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-04
 */
@Component
public class SecurityRoleServiceFallback implements SecurityRoleService, HasLogger,
    FallbackFactory<SecurityRoleServiceFallback> {

  final Throwable cause;

  public SecurityRoleServiceFallback() {
    this(null);
  }

  SecurityRoleServiceFallback(Throwable cause) {
    this.cause = cause;
  }

  @Override
  public SecurityRoleServiceFallback create(Throwable cause) {
    if (cause != null) {
      String errMessage = StringUtils.isNotBlank(cause.getMessage()) ? cause.getMessage()
          : "Unknown error occurred : " + cause.toString();
      // I don't see this log statement
      logger().debug("Client fallback called for the cause : {}", errMessage);
    }
    return new SecurityRoleServiceFallback(cause);
  }

  @Override
  public ServiceResult<List<SecurityRole>> getAllowedLoginRoles() {
    logger().error(getLoggerPrefix("getAllowedLoginRoles") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server",
        Collections.emptyList());
  }

  @Override
  public ServiceResult<SecurityRole> getSecurityRoleByName(
      GetSecurityRoleByNameQuery query) {
    logger().error(getLoggerPrefix("getSecurityRoleByName") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<List<SecurityRole>> findAllActive() {
    logger().error(getLoggerPrefix("findAllActive") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server",
        Collections.emptyList());
  }

  @Override
  public ServiceResult<Page<SecurityRole>> findAnyMatching(FindAnyMatchingQuery query) {
    logger().error(getLoggerPrefix("findAnyMatching") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", new Page<>());
  }

  @Override
  public ServiceResult<Long> countAnyMatching(CountAnyMatchingQuery query) {
    logger().error(getLoggerPrefix("countAnyMatching") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", 0L);
  }

  @Override
  public ServiceResult<SecurityRole> getById(GetByStrIdQuery query) {
    logger().error(getLoggerPrefix("getById") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<SecurityRole> save(SaveQuery<SecurityRole> query) {
    logger().error(getLoggerPrefix("save") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<Void> delete(DeleteByStrIdQuery query) {
    logger().error(getLoggerPrefix("delete") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }
}
