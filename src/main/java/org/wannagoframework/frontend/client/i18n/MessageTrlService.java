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

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 * @since 2019-04-21
 */
@FeignClient(name = "${app.remote-services.i18n-server.name:null}", url = "${app.remote-services.i18n-server.url:}", path = "/messageTrlService", fallback = MessageTrlServiceFallback.class)
@Primary
public interface MessageTrlService {

  @PostMapping(value = "/findByMessage")
  ServiceResult<List<MessageTrl>> findByMessage(@RequestBody FindByMessageQuery query);

  @PostMapping(value = "/countByMessage")
  ServiceResult<Long> countByMessage(@RequestBody CountByMessageQuery query);

  @PostMapping(value = "/findByIso3")
  ServiceResult<List<MessageTrl>> findByIso3(@RequestBody FindByIso3Query query);

  @PostMapping(value = "/getByNameAndIso3")
  ServiceResult<MessageTrl> getByNameAndIso3(@RequestBody GetByNameAndIso3Query query);

  @PostMapping(value = "/getById")
  ServiceResult<MessageTrl> getById(@RequestBody GetByIdQuery query);

  @PostMapping(value = "/save")
  ServiceResult<MessageTrl> save(@RequestBody SaveQuery<MessageTrl> query);

  @PostMapping(value = "/delete")
  ServiceResult<Void> delete(@RequestBody DeleteByIdQuery query);
}
