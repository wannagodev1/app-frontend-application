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


package org.wannagoframework.frontend;

import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.SpringServlet;
import com.vaadin.flow.spring.SpringVaadinServletService;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wannagoframework.commons.utils.HasLogger;
import org.wannagoframework.commons.utils.SpringProfileConstants;


/**
 * @author WannaGo Dev1.
 * @version 1.0
 * @since 2019-03-26
 */
public abstract class BaseApplication implements InitializingBean, HasLogger {

  private static final Logger logger = LoggerFactory.getLogger(BaseApplication.class);

  private final Environment env;

  public BaseApplication(Environment env) {
    this.env = env;
  }

  protected static void logApplicationStartup(Environment env) {
    String protocol = "http";
    if (env.getProperty("server.ssl.key-store") != null) {
      protocol = "https";
    }
    String serverPort = env.getProperty("server.port");
    String contextPath = env.getProperty("server.servlet.context-path");
    if (StringUtils.isBlank(contextPath)) {
      contextPath = "/";
    }
    String hostAddress = "localhost";
    try {
      hostAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      logger.warn("The host name could not be determined, using `localhost` as fallback");
    }
    logger.info("\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}{}\n\t" +
            "External: \t{}://{}:{}{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
        env.getProperty("spring.application.name"),
        protocol,
        serverPort,
        contextPath,
        protocol,
        hostAddress,
        serverPort,
        contextPath,
        env.getActiveProfiles());

    String configServerStatus = env.getProperty("configserver.status");
    if (configServerStatus == null) {
      configServerStatus = "Not found or not setup for this application";
    }
    logger.info("\n----------------------------------------------------------\n\t" +
            "Config Server: \t{}\n----------------------------------------------------------",
        configServerStatus);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
    if (activeProfiles.contains(SpringProfileConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles
        .contains(SpringProfileConstants.SPRING_PROFILE_PRODUCTION)) {
      logger.error("You have misconfigured your application! It should not run " +
          "with both the 'dev' and 'prod' profiles at the same time.");
    }
  }

  @RequestMapping(value = "/")
  public String forward(@Value("${vaadin.url}") String vaadinUrl) {
    return "forward:" + vaadinUrl;
  }

  @Bean
  public ServletRegistrationBean<SpringServlet> springServlet(
      ApplicationContext applicationContext,
      @Value("${vaadin.urlMapping}") String vaadinUrlMapping) {

    SpringServlet servlet = buildSpringServlet(applicationContext);
    ServletRegistrationBean<SpringServlet> registrationBean =
        new ServletRegistrationBean<>(servlet, vaadinUrlMapping, "/frontend/*");
    registrationBean.setLoadOnStartup(1);
    //registrationBean.addInitParameter(Constants.SERVLET_PARAMETER_SYNC_ID_CHECK, "false");

    return registrationBean;
  }

  private SpringServlet buildSpringServlet(ApplicationContext applicationContext) {
    return new SpringServlet(applicationContext, true) {
      @Override
      protected VaadinServletService createServletService(
          DeploymentConfiguration deploymentConfiguration) throws
          ServiceException {
        SpringVaadinServletService service =
            buildSpringVaadinServletService(this, deploymentConfiguration, applicationContext);
        service.init();
        return service;
      }
    };
  }

  private SpringVaadinServletService buildSpringVaadinServletService(SpringServlet servlet,
      DeploymentConfiguration deploymentConfiguration,
      ApplicationContext applicationContext) {
    return new SpringVaadinServletService(servlet, deploymentConfiguration, applicationContext) {
      @Override
      public void requestStart(VaadinRequest request, VaadinResponse response) {
        super.requestStart(request, response);

        /*
        //logger().debug("Current Locale = " + request.getLocale().getLanguage());
        AppContext.setCurrentIso3Language(request.getLocale().getLanguage());
        AppContext.setCurrentSessionId(request.getWrappedSession().getId());

        Object principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal instanceof SecurityUser) {
          AppContext.setCurrentUsername(((SecurityUser) principal).getUsername());
        } else if (principal instanceof UsernamePasswordAuthenticationToken) {
          AppContext
              .setCurrentUsername(((UsernamePasswordAuthenticationToken) principal).getName());
        }
         */
      }

      @Override
      public void requestEnd(
          VaadinRequest request, VaadinResponse response, VaadinSession session) {
        if (session != null) {
          try {
            session.lock();
            writeToHttpSession(request.getWrappedSession(), session);
          } finally {
            session.unlock();
          }
        }
        super.requestEnd(request, response, session);
      }
    };
  }
/*
  @Bean
  public HazelcastInstance hazelcastInstance(
      @Value("${hazelcast.max.no.heartbeat.seconds:60}") String hazelcastHeartbeat) {
    Config config = new Config();

    MapAttributeConfig attributeConfig =
        new MapAttributeConfig().setName(HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE)
            .setExtractor(PrincipalNameExtractor.class.getName());

    config.setProperty("hazelcast.max.no.heartbeat.seconds", hazelcastHeartbeat)
        .getMapConfig("spring:session:sessions")
        .addMapAttributeConfig(attributeConfig)
        .addMapIndexConfig(
            new MapIndexConfig(HazelcastSessionRepository.PRINCIPAL_NAME_ATTRIBUTE, false));

    config.getGroupConfig().setName("doYouWannaPlay-frontoffice");

    return Hazelcast.newHazelcastInstance(config);
  }

  @Bean
  public CookieSerializer cookieSerializer() {
    DefaultCookieSerializer serializer = new DefaultCookieSerializer();
    serializer.setCookieName("FRONTOFFICE-SESSION");
    serializer.setCookiePath("/");
    serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
    return serializer;
  }
 */
}
