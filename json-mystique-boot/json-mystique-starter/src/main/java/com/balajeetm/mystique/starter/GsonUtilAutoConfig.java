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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.balajeetm.mystique.util.gson.GsonFactory;
import com.balajeetm.mystique.util.gson.convertor.GsonConvertor;
import com.balajeetm.mystique.util.gson.lever.JsonComparator;
import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.balajeetm.mystique.util.gson.lever.JsonQuery;
import com.google.gson.Gson;

/**
 * The Class JacksonUtilAutoConfig. This config class is responsible for automatically creating
 * mystique jackson convertors and registering them with the available jackson object mappers
 *
 * @author balajeetm
 */
@Configuration
@ConditionalOnClass(value = {GsonConvertor.class})
public class GsonUtilAutoConfig {

  /**
   * Gson convertor.
   *
   * @return the gson convertor
   */
  @Bean
  public GsonConvertor gsonConvertor() {
    return (GsonConvertor) GsonConvertor.getInstance();
  }

  /**
   * Gson factory.
   *
   * @return the gson factory
   */
  @Bean
  public GsonFactory gsonFactory() {
    return GsonFactory.getInstance();
  }

  /**
   * Json lever.
   *
   * @return the json lever
   */
  @Bean
  @Primary
  public JsonLever jsonLever() {
    return JsonLever.getInstance();
  }

  /**
   * Json comparator.
   *
   * @return the json comparator
   */
  @Bean
  public JsonComparator jsonComparator() {
    return JsonComparator.getInstance();
  }

  /**
   * Json query.
   *
   * @return the json query
   */
  @Bean
  public JsonQuery jsonQuery() {
    return JsonQuery.getInstance();
  }

  /** The Class MystiqueGsonConfiguration. */
  @Configuration
  protected static class MystiqueGsonConfiguration {

    /**
     * Mystique gson.
     *
     * @param factory the factory
     * @return the gson
     */
    @Bean
    @ConditionalOnMissingBean(value = {Gson.class})
    public Gson mystiqueGson(GsonFactory factory) {
      return factory.getGson();
    }
  }
}
