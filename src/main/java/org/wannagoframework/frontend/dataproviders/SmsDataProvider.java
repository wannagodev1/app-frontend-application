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

package org.wannagoframework.frontend.dataproviders;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.wannagoframework.dto.domain.notification.Sms;
import org.wannagoframework.dto.serviceQuery.generic.CountAnyMatchingQuery;
import org.wannagoframework.dto.serviceQuery.generic.FindAnyMatchingQuery;
import org.wannagoframework.dto.utils.Page;
import org.wannagoframework.dto.utils.Pageable;
import org.wannagoframework.frontend.client.notification.NotificationServices;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.utils.AppConst;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-02-14
 */
@SpringComponent
@UIScope
public class SmsDataProvider extends DefaultDataProvider<Sms, DefaultFilter> implements
    Serializable {

  @Autowired
  public SmsDataProvider() {
    super(AppConst.DEFAULT_SORT_DIRECTION,
        AppConst.DEFAULT_SORT_FIELDS);
  }

  @Override
  protected Page<Sms> fetchFromBackEnd(Query<Sms, DefaultFilter> query,
      Pageable pageable) {
    DefaultFilter filter = query.getFilter().orElse(DefaultFilter.getEmptyFilter());
    Page<Sms> page = NotificationServices.getSmsService()
        .findAnyMatching(
            new FindAnyMatchingQuery(filter.getFilter(), filter.isShowInactive(), pageable))
        .getData();
    if (getPageObserver() != null) {
      getPageObserver().accept(page);
    }
    return page;
  }


  @Override
  protected int sizeInBackEnd(Query<Sms, DefaultFilter> query) {
    DefaultFilter filter = query.getFilter().orElse(DefaultFilter.getEmptyFilter());
    return NotificationServices.getSmsService()
        .countAnyMatching(new CountAnyMatchingQuery(filter.getFilter(), filter.isShowInactive()))
        .getData().intValue();
  }
}
