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

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.wannagoframework.dto.utils.Page;
import org.wannagoframework.dto.utils.Pageable;
import org.wannagoframework.dto.utils.Pageable.Order;
import org.wannagoframework.dto.utils.Pageable.Order.Direction;
import org.wannagoframework.frontend.utils.Pair;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-03
 */
public abstract class PageableDataProvider<T extends Serializable, F>
    extends AbstractBackEndDataProvider<T, F> {

  private static Order queryOrderToSpringOrder(QuerySortOrder queryOrder) {
    return new Order(queryOrder.getDirection() == SortDirection.ASCENDING
        ? Direction.ASC
        : Direction.DESC, queryOrder.getSorted());
  }

  public static Pair<Integer, Integer> limitAndOffsetToPageSizeAndNumber(
      int offset, int limit) {
    int lastIndex = offset + limit - 1;
    int maxPageSize = lastIndex + 1;

    for (double pageSize = limit; pageSize <= maxPageSize; pageSize++) {
      int startPage = (int) (offset / pageSize);
      int endPage = (int) (lastIndex / pageSize);
      if (startPage == endPage) {
        // It fits on one page, let's go with that
        return Pair.of((int) pageSize, startPage);
      }
    }

    // Should not really get here
    return Pair.of(maxPageSize, 0);
  }

  @Override
  protected Stream<T> fetchFromBackEnd(Query<T, F> query) {
    Pageable pageable = getPageable(query);
    Page<T> result = fetchFromBackEnd(query, pageable);
    return fromPageable(result, pageable, query);
  }

  protected abstract Page<T> fetchFromBackEnd(Query<T, F> query, Pageable pageable);

  private Pageable getPageable(Query<T, F> q) {
    Pair<Integer, Integer> pageSizeAndNumber = limitAndOffsetToPageSizeAndNumber(
        q.getOffset(), q.getLimit());
    return new Pageable(pageSizeAndNumber.getSecond(),
        pageSizeAndNumber.getFirst(), q.getOffset(), createSpringSort(q));
  }

  private <T, F> Collection<Order> createSpringSort(Query<T, F> q) {
    List<QuerySortOrder> sortOrders;
    if (q.getSortOrders().isEmpty()) {
      sortOrders = getDefaultSortOrders();
    } else {
      sortOrders = q.getSortOrders();
    }
    List<Order> orders = sortOrders.stream()
        .map(PageableDataProvider::queryOrderToSpringOrder)
        .collect(Collectors.toList());
    if (orders.isEmpty()) {
      return null;
    } else {
      return new ArrayList<>(orders);
    }
  }

  protected abstract List<QuerySortOrder> getDefaultSortOrders();

  private <T extends Serializable> Stream<T> fromPageable(Page<T> result, Pageable pageable,
      Query<T, ?> query) {
    List<T> items = result.getContent();

    int firstRequested = query.getOffset();
    int nrRequested = query.getLimit();
    int firstReturned = pageable.getOffset();
    int firstReal = firstRequested - firstReturned;
    int afterLastReal = firstReal + nrRequested;
    if (afterLastReal > items.size()) {
      afterLastReal = items.size();
    }
    return items.subList(firstReal, afterLastReal).stream();
  }

}