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


package org.wannagoframework.frontend.utils.i18n;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.util.Date;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-22
 */
public class DateTimeFormatter {

  public static String format(Instant instant) {
    return java.time.format.DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault()).format(instant);
  }

  public static String format(Date date) {
    return java.time.format.DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault()).format(date.toInstant());
  }
}
