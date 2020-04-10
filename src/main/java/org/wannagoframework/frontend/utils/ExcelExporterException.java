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

package org.wannagoframework.frontend.utils;

/**
 * @author Alexandre Clavaud.
 * @version 1.0
 * @since 09/04/2020
 */
public class ExcelExporterException extends RuntimeException {

  ExcelExporterException(String message) {
    super(message);
  }

  ExcelExporterException(String message, Exception e) {
    super(message, e);
  }
}
