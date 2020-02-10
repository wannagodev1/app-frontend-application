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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.vaadin.flow.server.VaadinSession;
import feign.RequestInterceptor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
//@EnableResourceServer
public class ResourceServerConfig {

  private final OAuth2ClientContext oAuth2ClientContext;

  @Autowired
  public ResourceServerConfig(
      OAuth2ClientContext oAuth2ClientContext) {
    this.oAuth2ClientContext = oAuth2ClientContext;
  }

  @Bean
  @ConfigurationProperties(prefix = "security.oauth2.client")
  public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
    return new ClientCredentialsResourceDetails();
  }

  @Bean
  public RequestInterceptor oauth2FeignRequestInterceptor() {
    return new OAuth2FeignRequestInterceptor(oAuth2ClientContext,
        clientCredentialsResourceDetails());
  }

  @Bean
  public RequestInterceptor oauth2FeignRequestInterceptor2() {
    return template -> {
      if (template.requestBody() == null) {
        return;
      }
      VaadinSession v = VaadinSession.getCurrent();
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = null;
      try {
        node = mapper.readTree(template.requestBody().asString());
        String username = node.path("_username").asText();
        if (StringUtils.isNotBlank(username) && !"null".equals(username)) {
          template.header("X-SecUsername", username);
        }
        String securityUserId = node.path("_securityUserId").asText();
        if (StringUtils.isNotBlank(securityUserId) && !"null".equals(securityUserId)) {
          template.header("X-SecSecurityUserId", securityUserId);
        }
        String sessionId = node.path("_sessionId").asText();
        if (StringUtils.isNotBlank(sessionId) && !"null".equals(sessionId)) {
          template.header("X-SecSessionId", sessionId);
        }
        String iso3Language = node.path("_iso3Language").asText();
        if (StringUtils.isNotBlank(iso3Language) && !"null".equals(iso3Language)) {
          template.header("X-Iso3Language", iso3Language);
        }
        JsonNode currentPosition = node.path("_currentPosition");
        if (currentPosition != null && !currentPosition.equals(NullNode.getInstance())) {
          String currentPositionLat = currentPosition.get("lat").asText();
          String currentPositionLng = currentPosition.get("lng").asText();
          template.header("X-CurrentPosition", currentPositionLat + "," + currentPositionLng);
        }
      } catch (Throwable t) {
      }
    };
  }

  @Bean
  public OAuth2RestOperations restTemplate(OAuth2ClientContext oauth2ClientContext) {
    return new OAuth2RestTemplate(clientCredentialsResourceDetails(), oauth2ClientContext);
  }
}