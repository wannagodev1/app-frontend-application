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

import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.wannagoframework.dto.domain.security.RememberMeToken;
import org.wannagoframework.dto.domain.security.SecurityUser;
import org.wannagoframework.dto.serviceQuery.ServiceResult;
import org.wannagoframework.dto.serviceQuery.authentification.ClearRememberMeTokenQuery;
import org.wannagoframework.dto.serviceQuery.authentification.CreateRememberMeTokenQuery;
import org.wannagoframework.dto.serviceQuery.authentification.ForgetPasswordQuery;
import org.wannagoframework.dto.serviceQuery.authentification.GetSecurityUserByRememberMeTokenQuery;
import org.wannagoframework.dto.serviceQuery.authentification.LoginQuery;
import org.wannagoframework.dto.serviceQuery.authentification.PasswordResetQuery;
import org.wannagoframework.dto.serviceQuery.authentification.ResetVerificationTokenQuery;
import org.wannagoframework.dto.serviceQuery.authentification.SignUpQuery;
import org.wannagoframework.dto.serviceQuery.authentification.ValidateUserQuery;
import org.wannagoframework.dto.serviceResponse.authentification.AuthResponse;

@FeignClient(name = "${app.remote-services.authorization-server.name:null}", url = "${app.remote-services.authorization-server.url:}", path = "/uaa/auth", fallbackFactory = AuthServiceServiceFallback.class)
public interface AuthService {

  @PostMapping(value = "/login")
  ServiceResult<AuthResponse> authenticateUser(@RequestBody LoginQuery query);

  @PostMapping(value = "/signup")
  ServiceResult<String> registerUser(@RequestBody SignUpQuery query);

  @PostMapping(value = "/resetVerificationToken")
  ServiceResult<Void> resetVerificationToken(
      @RequestBody ResetVerificationTokenQuery query);

  @PostMapping(value = "/forgetPassword")
  ServiceResult<Void> forgetPassword(@Valid @RequestBody ForgetPasswordQuery query);

  @PostMapping(value = "/validateUser")
  ServiceResult<String> validateUser(@Valid @RequestBody ValidateUserQuery query);

  @PostMapping(value = "/passwordReset")
  ServiceResult<Void> passwordReset(@Valid @RequestBody PasswordResetQuery query);

  @PostMapping(value = "/createRememberMeToken")
  ServiceResult<RememberMeToken> createRememberMeToken(
      @Valid @RequestBody CreateRememberMeTokenQuery query);

  @PostMapping(value = "/clearRememberMeToken")
  ServiceResult<Void> clearRememberMeToken(
      @Valid @RequestBody ClearRememberMeTokenQuery query);

  @PostMapping(value = "/getSecurityUserIdByRememberMeToken")
  ServiceResult<SecurityUser> getSecurityUserByRememberMeToken(
      @Valid @RequestBody GetSecurityUserByRememberMeTokenQuery query);

}
