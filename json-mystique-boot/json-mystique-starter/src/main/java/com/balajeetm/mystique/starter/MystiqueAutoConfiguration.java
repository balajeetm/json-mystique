/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Jan, 2017 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.starter;

import java.io.IOException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.balajeetm.mystique.core.JsonMystique;
import com.balajeetm.mystique.core.lever.MystiqueLever;
import com.balajeetm.mystique.starter.config.JacksonGsonConfig;
import com.balajeetm.mystique.starter.config.MystiqueConfigurer;
import com.balajeetm.mystique.starter.config.RestTemplateConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class MystiqueAutoConfiguration. This configuration class is responsible for creating all the
 * necessary beans of Json Mystique and automically load and prepare json mystique files available
 * in the classpath for transformation
 *
 * @author balajeetm
 */
@Configuration
public class MystiqueAutoConfiguration {

  /**
   * Mystique lever.
   *
   * @return the mystique lever
   */
  @Bean
  public MystiqueLever mystiqueLever() {
    return MystiqueLever.getInstance();
  }

  /**
   * Json mystique.
   *
   * @return the json mystique
   */
  @Bean
  public JsonMystique jsonMystique() {
    return JsonMystique.getInstance();
  }

  /**
   * Mystique configurer.
   *
   * @param jsonMystique the json mystique
   * @return the mystique configurer
   */
  public MystiqueConfigurer mystiqueConfigurer(JsonMystique jsonMystique) {
    return new MystiqueConfigurer(jsonMystique);
  }

  /** The Class MystiqueModuleConfiguration. */
  @Configuration
  @ConditionalOnClass(value = {ObjectMapper.class})
  @Import(value = {JacksonGsonConfig.class})
  protected static class MystiqueModuleConfiguration {}

  /** The Class RestTemplateConfiguration. */
  @Configuration
  @ConditionalOnClass(value = {RestTemplate.class})
  @Import(value = {RestTemplateConfig.class})
  protected static class RestTemplateConfiguration {}

  /**
   * Mystique rest template.
   *
   * @return the rest template
   */
  @Bean
  @ConditionalOnClass(value = {RestTemplate.class})
  @ConditionalOnMissingBean(value = {RestTemplate.class})
  public RestTemplate mystiqueRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    // To ensure non 2xx responses do not throw exceptions
    restTemplate.setErrorHandler(
        new ResponseErrorHandler() {

          @Override
          public boolean hasError(ClientHttpResponse response) throws IOException {
            return false;
          }

          @Override
          public void handleError(ClientHttpResponse response) throws IOException {}
        });
    return restTemplate;
  }
}
