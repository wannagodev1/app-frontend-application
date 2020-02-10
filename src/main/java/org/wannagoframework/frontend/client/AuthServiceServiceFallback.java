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

import feign.hystrix.FallbackFactory;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.wannagoframework.commons.utils.HasLogger;
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

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-02
 */
@Component
public class AuthServiceServiceFallback implements AuthService, HasLogger,
    FallbackFactory<AuthServiceServiceFallback> {

  final Throwable cause;

  public AuthServiceServiceFallback() {
    this(null);
  }

  AuthServiceServiceFallback(Throwable cause) {
    this.cause = cause;
  }

  @Override
  public AuthServiceServiceFallback create(Throwable cause) {
    if (cause != null) {
      String errMessage = StringUtils.isNotBlank(cause.getMessage()) ? cause.getMessage()
          : "Unknown error occurred : " + cause.toString();
      // I don't see this log statement
      logger().debug("Client fallback called for the cause : {}", errMessage);
    }
    return new AuthServiceServiceFallback(cause);
  }

  @Override
  public ServiceResult<AuthResponse> authenticateUser(LoginQuery query) {
    logger().error(getLoggerPrefix("authenticateUser") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<String> registerUser(SignUpQuery query) {
    logger().error(getLoggerPrefix("registerUser") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<Void> resetVerificationToken(
      ResetVerificationTokenQuery query) {
    logger().error(getLoggerPrefix("resetVerificationToken") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<Void> forgetPassword(
      @Valid ForgetPasswordQuery query) {
    logger().error(getLoggerPrefix("forgetPassword") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<String> validateUser(@Valid ValidateUserQuery query) {
    logger().error(getLoggerPrefix("validateUser") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<Void> passwordReset(
      @Valid PasswordResetQuery query) {
    logger().error(getLoggerPrefix("passwordReset") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<RememberMeToken> createRememberMeToken(
      @Valid CreateRememberMeTokenQuery query) {
    logger().error(getLoggerPrefix("createRememberMeToken") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<Void> clearRememberMeToken(
      @Valid ClearRememberMeTokenQuery query) {
    logger().error(getLoggerPrefix("clearRememberMeToken") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }

  @Override
  public ServiceResult<SecurityUser> getSecurityUserByRememberMeToken(
      @Valid GetSecurityUserByRememberMeTokenQuery query) {
    logger().error(
        getLoggerPrefix("getSecurityUserByRememberMeToken") + "Cannot connect to the server");

    return new ServiceResult<>(false, "Cannot connect to server", null);
  }
}
