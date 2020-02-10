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


package org.wannagoframework.frontend.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app")
public class WannaGoProperties {

  private final RemoteServices remoteServices = new RemoteServices();

  private final Authorization authorization = new Authorization();

  private final LoginForm loginForm = new LoginForm();

  @Data
  public static class LoginForm {
    private Boolean displaySignup = Boolean.TRUE;
    private Boolean displayForgetPassword = Boolean.TRUE;
    private Boolean displaySocialLogin = Boolean.TRUE;
    private Boolean displayRememberMe = Boolean.TRUE;
    private Boolean displayLanguage = Boolean.TRUE;
  }

  @Data
  public static class RemoteServices {
    private RemoteServer backendServer = new RemoteServer();
    private RemoteServer authorizationServer = new RemoteServer();
    private RemoteServer i18nServer = new RemoteServer();
    private RemoteServer resourceServer = new RemoteServer();
    private RemoteServer auditServer = new RemoteServer();

    @Data
    public static final class RemoteServer {
      private String url;
      private String name;
    }
  }

  @Data
  public static final class Authorization {

    private String facebookUrl;
    private String googleUrl;
    private String publicKey;
  }
}
