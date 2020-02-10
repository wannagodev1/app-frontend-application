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

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 * @since 2019-04-16
 */
@FeignClient(name = "${app.remote-services.authorization-server.name:null}", url = "${app.remote-services.authorization-server.url:}", path = "/uaa/securityRole", fallbackFactory = SecurityRoleServiceFallback.class)
@Primary
public interface SecurityRoleService {

  @PostMapping(value = "/getAllowedLoginRoles")
  ServiceResult<List<SecurityRole>> getAllowedLoginRoles();

  @PostMapping(value = "/getSecurityRoleByName")
  ServiceResult<SecurityRole> getSecurityRoleByName(@RequestBody GetSecurityRoleByNameQuery query);

  @PostMapping(value = "/findAllActive")
  ServiceResult<List<SecurityRole>> findAllActive();

  @PostMapping(value = "/findAnyMatching")
  ServiceResult<Page<SecurityRole>> findAnyMatching(@RequestBody FindAnyMatchingQuery query);

  @PostMapping(value = "/countAnyMatching")
  ServiceResult<Long> countAnyMatching(@RequestBody CountAnyMatchingQuery query);

  @PostMapping(value = "/getById")
  ServiceResult<SecurityRole> getById(@RequestBody GetByStrIdQuery query);

  @PostMapping(value = "/save")
  ServiceResult<SecurityRole> save(@RequestBody SaveQuery<SecurityRole> query);

  @PostMapping(value = "/delete")
  ServiceResult<Void> delete(@RequestBody DeleteByStrIdQuery query);
}
