/*
 *   ilem group CONFIDENTIAL
 *    __________________
 *
 *    [2019] ilem Group
 *    All Rights Reserved.
 *
 *    NOTICE:  All information contained herein is, and remains the property of "ilem Group"
 *    and its suppliers, if any. The intellectual and technical concepts contained herein are
 *    proprietary to "ilem Group" and its suppliers and may be covered by Morocco, Switzerland and Foreign
 *    Patents, patents in process, and are protected by trade secret or copyright law.
 *    Dissemination of this information or reproduction of this material is strictly forbidden unless
 *    prior written permission is obtained from "ilem Group".
 */

package org.wannagoframework.frontend.dataproviders;

import java.util.function.Function;
import org.wannagoframework.dto.domain.audit.AuditLog;
import org.wannagoframework.dto.serviceQuery.BaseRemoteQuery;
import org.wannagoframework.dto.serviceQuery.auditLog.CountAuditLogQuery;
import org.wannagoframework.dto.serviceQuery.auditLog.FindAuditLogQuery;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.utils.Page;
import org.wannagoframework.dto.utils.Pageable;
import org.wannagoframework.frontend.dataproviders.AuditLogDataProvider.AuditLogFilter;
import org.wannagoframework.frontend.utils.AppConst;

/**
 * @author Alexandre Clavaud.
 * @version 1.0
 * @since 2019-02-14
 */
@SpringComponent
@UIScope
public class AuditLogDataProvider extends
    DefaultDataProvider<AuditLog, AuditLogFilter> implements
    Serializable {
  private Function<FindAuditLogQuery, ServiceResult<Page<AuditLog>>> findHandler;
  private Function<CountAuditLogQuery, ServiceResult<Long>> countHandler;

  public AuditLogDataProvider(Function<FindAuditLogQuery, ServiceResult<Page<AuditLog>>> findHandler,
      Function<CountAuditLogQuery,ServiceResult<Long>> countHandler ) {
    super(AppConst.DEFAULT_SORT_DIRECTION,
        AppConst.DEFAULT_SORT_FIELDS);
    this.findHandler = findHandler;
    this.countHandler = countHandler;
  }

  @Override
  protected Page<AuditLog> fetchFromBackEnd(
      Query<AuditLog, AuditLogFilter> query,
      Pageable pageable) {
    AuditLogFilter filter = query.getFilter().orElse(null);
    Page<AuditLog> page = findHandler.apply(new FindAuditLogQuery(filter.getClassName(), filter.getRecordId(), pageable)).getData();
    if (getPageObserver() != null) {
      getPageObserver().accept(page);
    }
    return page;
  }


  @Override
  protected int sizeInBackEnd(Query<AuditLog, AuditLogFilter> query) {
    AuditLogFilter filter = query.getFilter().orElse(null);

    ServiceResult<Long> _count =  countHandler.apply(new CountAuditLogQuery(filter.getClassName(), filter.getRecordId()));

    if ( _count.getIsSuccess() && _count.getData() != null )
      return _count.getData().intValue();
    else
      return 0;
  }

  @Data
  @AllArgsConstructor
  public static class AuditLogFilter extends DefaultFilter {
    private String className = null;
    private Long recordId = null;
  }
}
