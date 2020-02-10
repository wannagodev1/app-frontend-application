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

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import org.wannagoframework.dto.domain.BaseEntity;
import org.wannagoframework.dto.utils.DirectionEnum;
import org.wannagoframework.dto.utils.Page;
import org.wannagoframework.frontend.dataproviders.DefaultDataProvider.DefaultFilter;
import org.wannagoframework.frontend.dataproviders.utils.FilterablePageableDataProvider;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-02-14
 */
public abstract class DefaultDataProvider<T extends BaseEntity, F extends DefaultFilter> extends
    FilterablePageableDataProvider<T, F> implements Serializable {

  private List<QuerySortOrder> defaultSortOrder;
  private Consumer<Page<T>> pageObserver;

  public DefaultDataProvider(DirectionEnum defaultSortDirection, String[] defaultSortFields) {
    setSortOrder(defaultSortDirection, defaultSortFields);
  }

  private void setSortOrder(DirectionEnum direction, String[] properties) {
    QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
    for (String property : properties) {
      if (direction.equals(DirectionEnum.ASC)) {
        builder.thenAsc(property);
      } else {
        builder.thenDesc(property);
      }
    }
    defaultSortOrder = builder.build();
  }

  @Override
  protected List<QuerySortOrder> getDefaultSortOrders() {
    return defaultSortOrder;
  }

  public Consumer<Page<T>> getPageObserver() {
    return pageObserver;
  }

  public void setPageObserver(Consumer<Page<T>> pageObserver) {
    this.pageObserver = pageObserver;
  }

  @Override
  public Object getId(T item) {
    return item.getId();
  }

  public static class DefaultFilter implements Serializable {

    private String filter;
    private Boolean showInactive;

    public DefaultFilter() {
      this.showInactive = Boolean.FALSE;
    }

    public DefaultFilter(String filter) {
      this(filter, Boolean.FALSE);
    }

    public DefaultFilter(String filter, Boolean showInactive) {
      this.filter = filter;
      this.showInactive = showInactive;
    }

    public static DefaultFilter getEmptyFilter() {
      return new DefaultFilter(null, false);
    }

    public String getFilter() {
      return filter;
    }

    public Boolean isShowInactive() {
      return showInactive;
    }
  }
}
