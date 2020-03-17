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

import feign.Logger.Level;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig extends RibbonAutoConfiguration {

  @Bean
  public Level feignLoggerLevel() {
    return Level.BASIC;
  }
/*
  @Bean
  public Decoder feignDecoder() {
    HttpMessageConverter jacksonConverter =
        new MappingJackson2HttpMessageConverter(customObjectMapper());
    ObjectFactory<HttpMessageConverters> objectFactory =
        () -> new HttpMessageConverters(jacksonConverter);
    return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
  }

  public ObjectMapper customObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    //objectMapper.registerModule(new Jackson2HalModule());

    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    return objectMapper;
  }

x
 */

}
