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
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import org.wannagoframework.dto.domain.security.SecurityUser;
import org.wannagoframework.dto.serviceQuery.generic.CountAnyMatchingQuery;
import org.wannagoframework.dto.serviceQuery.generic.FindAnyMatchingQuery;
import org.wannagoframework.dto.utils.Page;
import org.wannagoframework.dto.utils.Pageable;
import org.wannagoframework.frontend.client.security.SecurityServices;
import org.wannagoframework.frontend.dataproviders.utils.PageableDataProvider;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-02-14
 */
@SpringComponent
@UIScope
public class SecurityUserStringDataProvider extends
    PageableDataProvider<SecurityUser, String> implements
    Serializable {


  @Override
  protected Page<SecurityUser> fetchFromBackEnd(Query<SecurityUser, String> query,
      Pageable pageable) {

    return SecurityServices.getSecurityUserService().findAnyMatching(
        new FindAnyMatchingQuery(query.getFilter().orElse(null), true, pageable)).getData();
  }

  @Override
  protected List<QuerySortOrder> getDefaultSortOrders() {
    return Collections.singletonList(new QuerySortOrder("username", SortDirection.ASCENDING));
  }


  @Override
  protected int sizeInBackEnd(Query<SecurityUser, String> query) {
    return SecurityServices.getSecurityUserService()
        .countAnyMatching(new CountAnyMatchingQuery(query.getFilter().orElse(null), true))
        .getData().intValue();
  }
}
