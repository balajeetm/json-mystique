/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.balajeetm.mystique.core.module.MystiqueModule;
import com.balajeetm.mystique.util.jackson.convertor.JacksonConvertor;
import com.balajeetm.mystique.util.json.error.ConvertorException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;

/**
 * The Class JsonMystiqueBDDTest.
 *
 * @author balajeetm
 */
public class GsonJacksonConvertorBDDTest {

  /** The instance. */
  private JacksonConvertor instance;

  /** Instantiates a new gson jackson convertor BDD test. */
  public GsonJacksonConvertorBDDTest() {
    instance = (JacksonConvertor) JacksonConvertor.getInstance();
    instance.getObjectMapper().registerModule(new MystiqueModule());
  }

  /** Inits the. */
  @BeforeEach
  public void init() {}

  /** Jackson gson test. */
  @Test
  public void jacksonGsonTest() {
    try {
      String locationPattern = "classpath:gsonJackson/gsonJackson.json";
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource resource = resourceResolver.getResource(locationPattern);

      JsonNode jacksonObject = instance.deserialize(resource.getInputStream(), JsonNode.class);
      JsonElement gsonObject = instance.deserialize(resource.getInputStream(), JsonElement.class);
      JsonElement transGsonObject = instance.deserialize(jacksonObject, JsonElement.class);
      JsonNode transJacksonObject = instance.deserialize(gsonObject, JsonNode.class);

      Assertions.assertEquals(gsonObject, transGsonObject);
      Assertions.assertEquals(jacksonObject, transJacksonObject);
    } catch (Exception e) {
      Assertions.fail(e.getMessage());
    }
  }

  /** Jackson negative test 7. */
  @Test
  public void jacksonNegativeTest7() {
    try {
      JsonElement deserialize = instance.deserialize((String) null, JsonElement.class);
      Assertions.assertNull(deserialize);
    } catch (ConvertorException e) {
      Assertions.assertTrue(false, e.getMessage());
    }
  }
}
