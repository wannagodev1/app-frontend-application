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
import org.wannagoframework.dto.domain.reference.Country;
import org.wannagoframework.dto.serviceQuery.generic.CountAnyMatchingQuery;
import org.wannagoframework.dto.serviceQuery.generic.FindAnyMatchingQuery;
import org.wannagoframework.dto.utils.Page;
import org.wannagoframework.dto.utils.Pageable;
import org.wannagoframework.frontend.client.reference.ReferenceServices;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.utils.AppConst;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-02-14
 */
@SpringComponent
@UIScope
public class CountryDataProvider extends DefaultDataProvider<Country, DefaultFilter> implements
    Serializable {

  @Autowired
  public CountryDataProvider() {
    super(AppConst.DEFAULT_SORT_DIRECTION,
        AppConst.COUNTRY_SORT_FIELDS);
  }

  @Override
  protected Page<Country> fetchFromBackEnd(Query<Country, DefaultFilter> query,
      Pageable pageable) {
    DefaultFilter filter = query.getFilter().orElse(DefaultFilter.getEmptyFilter());
    Page<Country> page = ReferenceServices.getCountryService()
        .findAnyMatching(
            new FindAnyMatchingQuery(filter.getFilter(), null,
                pageable)).getData();
    if (getPageObserver() != null) {
      getPageObserver().accept(page);
    }
    return page;
  }


  @Override
  protected int sizeInBackEnd(Query<Country, DefaultFilter> query) {
    DefaultFilter filter = query.getFilter().orElse(DefaultFilter.getEmptyFilter());
    return ReferenceServices.getCountryService()
        .countAnyMatching(
            new CountAnyMatchingQuery(filter.getFilter(), null))
        .getData().intValue();
  }
}
