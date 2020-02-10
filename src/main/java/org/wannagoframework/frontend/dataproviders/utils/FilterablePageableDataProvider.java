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


package org.wannagoframework.frontend.dataproviders.utils;

import com.vaadin.flow.data.provider.Query;
import java.io.Serializable;
import java.util.stream.Stream;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-03
 */
public abstract class FilterablePageableDataProvider<T extends Serializable, F> extends
    PageableDataProvider<T, F> {

  private F filter = null;

  public void setFilter(F filter) {
    if (filter == null) {
      throw new IllegalArgumentException("Filter cannot be null");
    }
    this.filter = filter;
    refreshAll();
  }

  @Override
  public int size(Query<T, F> query) {
    return super.size(getFilterQuery(query));
  }

  @Override
  public Stream<T> fetch(Query<T, F> query) {
    return super.fetch(getFilterQuery(query));
  }

  private Query<T, F> getFilterQuery(Query<T, F> t) {
    return new Query<>(t.getOffset(), t.getLimit(), t.getSortOrders(),
        t.getInMemorySorting(), filter);
  }
}