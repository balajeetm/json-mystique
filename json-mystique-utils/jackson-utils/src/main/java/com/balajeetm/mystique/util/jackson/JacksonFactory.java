/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 12 Oct, 2017 by balajeemohan
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.jackson;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * A factory for creating Jackson objects.
 *
 * @author balajeemohan
 */
public class JacksonFactory {

  /** Instantiates a new jackson factory. */
  private JacksonFactory() {}

  /** The object mapper. */
  private ObjectMapper objectMapper;

  //Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized
  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static JacksonFactory INSTANCE = new JacksonFactory();
  }

  /**
   * Gets the single instance of JacksonFactory.
   *
   * @return single instance of JacksonFactory
   */
  public static JacksonFactory getInstance() {
    return Creator.INSTANCE;
  }

  /**
   * Gets the object mapper.
   *
   * @return the object mapper
   */
  public ObjectMapper getObjectMapper() {
    if (null == objectMapper) {
      objectMapper = new ObjectMapper();

      // to prevent exception when encountering unknown property:
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      // to allow serialization of "empty" POJOs (no properties to serialize)
      // (without this setting, an exception is thrown in those cases)
      objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
      // to write java.util.Date, Calendar as number (timestamp):
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      objectMapper.setSerializationInclusion(Include.NON_NULL);

      // JsonParser.Feature for configuring parsing settings:
      // to allow use of apostrophes (single quotes), non standard
      objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

      objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

      // Add Mix Ins if needed
      // eg. objectMapper.addMixInAnnotations(MagicProductUmbrella.class,
      // Test.class);

      // set various strategies
      // eg.
      // objectMapper.enableDefaultTypingAsProperty(DefaultTyping.OBJECT_AND_NON_CONCRETE,
      // "remoteClass");
    }
    return objectMapper;
  }
}
