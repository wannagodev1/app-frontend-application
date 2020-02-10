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


package org.wannagoframework.frontend.client.reference;

import com.vaadin.flow.spring.SpringServlet;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-06-02
 */
@Service
public class ReferenceServices {

  public static CountryService getCountryService() {
    return getApplicationContext().getBean(CountryService.class);
  }

  public static CountryTrlService getCountryTrlService() {
    return getApplicationContext().getBean(CountryTrlService.class);
  }

  public static IntermediateRegionService getIntermediateRegionService() {
    return getApplicationContext().getBean(IntermediateRegionService.class);
  }

  public static IntermediateRegionTrlService getIntermediateRegionTrlService() {
    return getApplicationContext().getBean(IntermediateRegionTrlService.class);
  }

  public static RegionService getRegionService() {
    return getApplicationContext().getBean(RegionService.class);
  }

  public static RegionTrlService getRegionTrlService() {
    return getApplicationContext().getBean(RegionTrlService.class);
  }

  public static SubRegionService getSubRegionService() {
    return getApplicationContext().getBean(SubRegionService.class);
  }

  public static SubRegionTrlService getSubRegionTrlService() {
    return getApplicationContext().getBean(SubRegionTrlService.class);
  }

  public static ApplicationContext getApplicationContext() {
    ServletContext servletContext = SpringServlet.getCurrent().getServletContext();
    return WebApplicationContextUtils.getWebApplicationContext(servletContext);
  }
}
