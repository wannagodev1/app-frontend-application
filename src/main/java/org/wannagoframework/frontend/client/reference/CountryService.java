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

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.wannagoframework.dto.domain.reference.Country;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.serviceQuery.generic.CountAnyMatchingQuery;
import org.wannagoframework.dto.serviceQuery.generic.DeleteByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.FindAnyMatchingQuery;
import org.wannagoframework.dto.serviceQuery.generic.GetByIdQuery;
import org.wannagoframework.dto.serviceQuery.generic.SaveQuery;
import org.wannagoframework.dto.serviceQuery.reference.country.GetByIso2OrIso3Query;
import org.wannagoframework.dto.utils.Page;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-27
 */
@FeignClient(name = "${app.remote-services.backend-server.name:null}", url = "${app.remote-services.backend-server.url:}", path = "/countryService", fallback = CountryServiceFallback.class)
@Primary
public interface CountryService {

  @PostMapping(value = "/findAnyMatching")
  ServiceResult<Page<Country>> findAnyMatching(@RequestBody FindAnyMatchingQuery query);

  @PostMapping(value = "/countAnyMatching")
  ServiceResult<Long> countAnyMatching(@RequestBody CountAnyMatchingQuery query);

  @PostMapping(value = "/getById")
  ServiceResult<Country> getById(@RequestBody GetByIdQuery query);

  @PostMapping(value = "/save")
  ServiceResult<Country> save(@RequestBody SaveQuery<Country> query);

  @PostMapping(value = "/delete")
  ServiceResult<Void> delete(@RequestBody DeleteByIdQuery query);

  @PostMapping(value = "/getByIso2OrIso3")
  ServiceResult<Country> getByIso2OrIso3(@RequestBody GetByIso2OrIso3Query query);
}
