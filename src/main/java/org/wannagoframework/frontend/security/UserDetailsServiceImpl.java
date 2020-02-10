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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wannagoframework.dto.domain.security.SecurityUser;
import org.wannagoframework.dto.serviceQuery.security.securityUser.GetSecurityUserByUsernameQuery;
import org.wannagoframework.frontend.client.security.SecurityUserService;

/**
 * Implements the {@link UserDetailsService}.
 *
 * This implementation searches for {@link User} entities by the e-mail address supplied in the
 * login screen.
 *
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-26
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

  private final SecurityUserService securityUserService;

  @Autowired
  public UserDetailsServiceImpl(SecurityUserService securityUserService) {
    this.securityUserService = securityUserService;
  }

  /**
   * Recovers the {@link SecurityUser} from the database using the e-mail address supplied in the
   * login screen. If the user is found, returns a {@link org.springframework.security.core.userdetails.User}.
   *
   * @param username User's e-mail address
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    SecurityUser securityUser = securityUserService
        .getSecurityUserByUsername(new GetSecurityUserByUsernameQuery(username)).getData();

    if (securityUser != null) {
      return securityUser;
    } else {
      throw new UsernameNotFoundException("No user present with username: " + username);
    }
  }
}