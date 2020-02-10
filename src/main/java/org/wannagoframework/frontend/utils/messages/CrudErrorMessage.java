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


package org.wannagoframework.frontend.utils.messages;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-06
 */
public final class CrudErrorMessage {

  public static final String ENTITY_NOT_FOUND = "The selected entity was not found.";

  public static final String CONCURRENT_UPDATE = "Somebody else might have updated the data. Please refresh and try again.";

  public static final String OPERATION_PREVENTED_BY_REFERENCES = "The operation can not be executed as there are references to entity in the database.";

  public static final String REQUIRED_FIELDS_MISSING = "Please fill out all required fields before proceeding.";

  private CrudErrorMessage() {
  }
}
