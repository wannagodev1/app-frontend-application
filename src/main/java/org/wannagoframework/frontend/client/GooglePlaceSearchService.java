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
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 * @since 2019-03-16
 */
@FeignClient(name = "${app.remote-services.backend-server.name:null}", url = "${app.remote-services.backend-server.url:}", path = "/googlePlaceSearchService", fallbackFactory = GooglePlaceSearchServiceFallback.class)
@Primary
public interface GooglePlaceSearchService {

  @PostMapping(value = "/searchPlaces")
  ServiceResult searchPlaces(@RequestBody SearchPlacesQuery query);

  @PostMapping(value = "/searchAroundByKeyword")
  ServiceResult<List<PlacesSearchResult>> searchAroundByKeyword(
      @RequestBody SearchAroundByKeywordQuery query);

  @PostMapping(value = "/autocompleteSearchRequest")
  ServiceResult<List<AutocompletePrediction>> autocompleteSearchRequest(
      @RequestBody AutocompleteSearchRequestQuery query);

  @PostMapping(value = "/getGooglePhoto")
  ServiceResult<StoredFile> getGooglePhoto(@RequestBody GetGooglePhotoQuery query);

  @PostMapping(value = "/getPlaceDetails")
  ServiceResult<PlaceDetails> getPlaceDetails(@RequestBody GetPlaceDetailsQuery query);
}