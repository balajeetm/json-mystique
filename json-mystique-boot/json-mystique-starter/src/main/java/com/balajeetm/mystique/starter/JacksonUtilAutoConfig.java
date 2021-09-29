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

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.balajeetm.mystique.util.jackson.JacksonFactory;
import com.balajeetm.mystique.util.jackson.convertor.JacksonConvertor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class JacksonUtilAutoConfig. This config class is responsible for automatically creating
 * mystique jackson convertors and registering them with the available jackson object mappers
 *
 * @author balajeetm
 */
@Configuration
@ConditionalOnClass(value = {JacksonConvertor.class, JacksonFactory.class})
public class JacksonUtilAutoConfig {

  /**
   * Mystique jacksonn factory.
   *
   * @return the jackson factory
   */
  @Bean
  public JacksonFactory mystiqueJacksonnFactory() {
    return JacksonFactory.getInstance();
  }

  /**
   * Jackson convertor.
   *
   * @return the jackson convertor
   */
  @Bean
  public JacksonConvertor jacksonConvertor() {
    return (JacksonConvertor) JacksonConvertor.getInstance();
  }

  /**
   * Mystique object mapper.
   *
   * @param factory the factory
   * @return the object mapper
   */
  @Bean
  public ObjectMapper mystiqueObjectMapper(JacksonFactory factory) {
    return factory.getObjectMapper();
  }
}
