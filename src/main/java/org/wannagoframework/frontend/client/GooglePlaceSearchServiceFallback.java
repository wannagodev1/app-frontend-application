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


package org.wannagoframework.frontend.client;

import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;
import feign.hystrix.FallbackFactory;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.serviceQuery.googlePlaceSearch.AutocompleteSearchRequestQuery;
import org.wannagoframework.dto.serviceQuery.googlePlaceSearch.GetGooglePhotoQuery;
import org.wannagoframework.dto.serviceQuery.googlePlaceSearch.GetPlaceDetailsQuery;
import org.wannagoframework.dto.serviceQuery.googlePlaceSearch.SearchAroundByKeywordQuery;
import org.wannagoframework.dto.serviceQuery.googlePlaceSearch.SearchPlacesQuery;
import org.wannagoframework.dto.utils.StoredFile;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-02
 */
@Component
public class GooglePlaceSearchServiceFallback implements GooglePlaceSearchService, HasLogger,
    FallbackFactory<GooglePlaceSearchServiceFallback> {

  final Throwable cause;

  public GooglePlaceSearchServiceFallback() {
    this(null);
  }

  GooglePlaceSearchServiceFallback(Throwable cause) {
    this.cause = cause;
  }

  @Override
  public GooglePlaceSearchServiceFallback create(Throwable cause) {
    if (cause != null) {
      String errMessage = StringUtils.isNotBlank(cause.getMessage()) ? cause.getMessage()
          : "Unknown error occurred : " + cause.toString();
      // I don't see this log statement
      logger().debug("Client fallback called for the cause : {}", errMessage);
    }
    return new GooglePlaceSearchServiceFallback(cause);
  }

  @Override
  public ServiceResult<List<PlacesSearchResult>> searchPlaces(SearchPlacesQuery query) {
    logger().error(getLoggerPrefix("searchPlaces") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server",
        Collections.emptyList());
  }

  @Override
  public ServiceResult<List<PlacesSearchResult>> searchAroundByKeyword(
      SearchAroundByKeywordQuery query) {
    logger().error(getLoggerPrefix("searchAroundByKeyword") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server",
        Collections.emptyList());
  }

  @Override
  public ServiceResult<List<AutocompletePrediction>> autocompleteSearchRequest(
      AutocompleteSearchRequestQuery query) {
    logger().error(getLoggerPrefix("autocompleteSearchRequest") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server",
        Collections.emptyList());
  }

  @Override
  public ServiceResult<StoredFile> getGooglePhoto(GetGooglePhotoQuery query) {
    logger().error(getLoggerPrefix("getGooglePhoto") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<PlaceDetails> getPlaceDetails(GetPlaceDetailsQuery query) {
    logger().error(getLoggerPrefix("getPlaceDetails") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }
}
