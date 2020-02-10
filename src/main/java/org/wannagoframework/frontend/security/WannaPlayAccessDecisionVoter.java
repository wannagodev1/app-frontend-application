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


package org.wannagoframework.frontend.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.dto.domain.security.SecurityRole;
import org.wannagoframework.frontend.client.security.SecurityRoleService;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-20
 */
@Component
public class WannaPlayAccessDecisionVoter implements AccessDecisionVoter, HasLogger {

  private final SecurityRoleService securityRoleService;
  private List<String> allowedRoles;

  public WannaPlayAccessDecisionVoter(
      SecurityRoleService securityRoleService) {
    this.securityRoleService = securityRoleService;

  }

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return true;
  }

  @Override
  public int vote(Authentication authentication, Object object, Collection collection) {
    String loggerPrefix = getLoggerPrefix("vote");

    int result = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .filter(getAllowedRoles()::contains)
        .findAny()
        .map(s -> ACCESS_GRANTED)
        .orElse(ACCESS_ABSTAIN);

    logger().trace(loggerPrefix + "Result = " + (result == ACCESS_GRANTED ? " Access Granted"
        : " Abstain voting"));

    return result;
  }

  protected List<String> getAllowedRoles() {
    if (allowedRoles == null) {
      allowedRoles = securityRoleService.getAllowedLoginRoles().getData().stream().map(
          SecurityRole::getName).collect(
          Collectors.toList());
    }

    return allowedRoles;
  }

  @Override
  public boolean supports(Class clazz) {
    return true;
  }
}