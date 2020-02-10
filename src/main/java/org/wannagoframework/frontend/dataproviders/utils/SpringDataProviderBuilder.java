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

import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.ToLongFunction;
import org.wannagoframework.dto.utils.Page;
import org.wannagoframework.dto.utils.Pageable;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-03
 */
public class SpringDataProviderBuilder<T extends Serializable, F> {

  private final BiFunction<Pageable, F, Page<T>> queryFunction;
  private final ToLongFunction<F> lengthFunction;
  private final List<QuerySortOrder> defaultSortOrders = new ArrayList<>();

  private F defaultFilter = null;

  public SpringDataProviderBuilder(
      BiFunction<Pageable, F, Page<T>> queryFunction,
      ToLongFunction<F> lengthFunction) {
    this.queryFunction = queryFunction;
    this.lengthFunction = lengthFunction;
  }

  public static <T extends Serializable, F> SpringDataProviderBuilder<T, F> forFunctions(
      BiFunction<Pageable, F, Page<T>> queryFunction,
      ToLongFunction<F> lengthFunction) {
    return new SpringDataProviderBuilder<>(queryFunction, lengthFunction);
  }

  public SpringDataProviderBuilder<T, F> withDefaultSort(String column,
      SortDirection direction) {
    defaultSortOrders.add(new QuerySortOrder(column, direction));
    return this;
  }

  public SpringDataProviderBuilder<T, F> withDefaultFilter(F defaultFilter) {
    this.defaultFilter = defaultFilter;
    return this;
  }

  public DataProvider<T, F> build() {
    return new PageableDataProvider<T, F>() {
      @Override
      protected Page<T> fetchFromBackEnd(Query<T, F> query,
          Pageable pageable) {
        return queryFunction.apply(pageable,
            query.getFilter().orElse(defaultFilter));
      }

      @Override
      protected List<QuerySortOrder> getDefaultSortOrders() {
        return defaultSortOrders;
      }

      @Override
      protected int sizeInBackEnd(Query<T, F> query) {
        return (int) lengthFunction
            .applyAsLong(query.getFilter().orElse(defaultFilter));
      }
    };
  }

  public ConfigurableFilterDataProvider<T, Void, F> buildFilterable() {
    return build().withConfigurableFilter();
  }
}