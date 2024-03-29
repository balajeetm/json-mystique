/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.jackson.convertor;

import com.balajeetm.mystique.util.jackson.JacksonFactory;
import com.balajeetm.mystique.util.json.convertor.JsonConvertor;
import com.balajeetm.mystique.util.json.error.ConvertorException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class JacksonConvertor.
 *
 * @author balajeetm
 */
@Slf4j
public class JacksonConvertor implements JsonConvertor {

  /** Instantiates a new json jackson convertor. */
  private JacksonConvertor() {}

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized

  /**
   * Gets the single instance of JsonJacksonConvertor.
   *
   * @return single instance of JsonJacksonConvertor
   */
  public static JsonConvertor getInstance() {
    return Creator.INSTANCE;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.balajeetm.mystique.util.json.convertor.JsonConvertor#deserialize(java
   * .lang.String, java.lang.Class)
   */
  public <T> T deserialize(String jsonString, Class<T> pojoType) throws ConvertorException {
    T value = null;
    try {
      value = null != jsonString ? getObjectMapper().readValue(jsonString, pojoType) : value;
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of json string %s to class %s.", jsonString, pojoType));
      throw getConvertorException(e);
    }
    return value;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.balajeetm.mystique.util.json.convertor.JsonConvertor#deserialize(java
   * .lang.Object, java.lang.Class)
   */
  public <T> T deserialize(Object object, Class<T> pojoType) throws ConvertorException {
    T value = null;
    try {
      value = null != object ? getObjectMapper().convertValue(object, pojoType) : value;
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of object %s to class %s.", object, pojoType));
      throw getConvertorException(e);
    }
    return value;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.balajeetm.mystique.util.json.convertor.JsonConvertor#deserialize(java
   * .io.InputStream, java.lang.Class)
   */
  public <T> T deserialize(InputStream inputStream, Class<T> pojoType) throws ConvertorException {
    T value = null;
    try {
      value = null != inputStream ? getObjectMapper().readValue(inputStream, pojoType) : value;
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of json input stream %s to class %s.",
              inputStream, pojoType));
      throw getConvertorException(e);
    }
    return value;
  }

  /**
   * Gets the convertor exception.
   *
   * @param e the e
   * @return the convertor exception
   */
  private ConvertorException getConvertorException(Exception e) {
    log.debug("Error occured during conversion", e);
    return new ConvertorException(e);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.balajeetm.mystique.util.json.convertor.JsonConvertor#serialize(java.
   * lang.Object)
   */
  public String serialize(Object pojo) throws ConvertorException {
    try {
      return Objects.isNull(pojo) ? null : getObjectMapper().writeValueAsString(pojo);
    } catch (Exception e) {
      log.error(String.format("Error during serialisation of object %s.", pojo));
      throw getConvertorException(e);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.balajeetm.mystique.util.json.convertor.JsonConvertor#deserializeGroup
   * (java.io.InputStream, java.lang.Class, java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T deserializeGroup(InputStream inputStream, Class<T> groupClass, Class<?> pojoType)
      throws ConvertorException {
    T result = null;
    if (Objects.nonNull(inputStream)) {
      try {
        JavaType javaType = getJavaType(groupClass, pojoType);
        result = (T) getObjectMapper().readValue(inputStream, javaType);
      } catch (Exception e) {
        log.error(
            String.format(
                "Error during deserialisation of json input stream %s to %s<%s>.",
                inputStream, groupClass.getSimpleName(), pojoType.getSimpleName()));
        throw getConvertorException(e);
      }
    }

    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.balajeetm.mystique.util.json.convertor.JsonConvertor#deserializeList(
   * java.io.InputStream, java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> List<T> deserializeList(InputStream inputStream, Class<T> pojoType)
      throws ConvertorException {
    List<T> result = null;
    if (Objects.nonNull(inputStream)) {
      JavaType javaType = getJavaType(List.class, pojoType);
      try {
        result = (List<T>) getObjectMapper().readValue(inputStream, javaType);
      } catch (Exception e) {
        log.error(
            String.format(
                "Error during deserialisation of json inputstream %s to List<%s>.",
                inputStream, pojoType.getSimpleName()));
        throw getConvertorException(e);
      }
    }
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.balajeetm.mystique.util.json.convertor.JsonConvertor#deserializeList(
   * java.lang.Object, java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> List<T> deserializeList(Object object, Class<T> pojoType) throws ConvertorException {
    List<T> result = null;
    if (Objects.nonNull(object)) {
      JavaType javaType = getJavaType(List.class, pojoType);
      try {
        result = (List<T>) getObjectMapper().convertValue(object, javaType);
      } catch (Exception e) {
        log.error(
            String.format(
                "Error during deserialisation of object %s to List<%s>.",
                object, pojoType.getSimpleName()));
        throw getConvertorException(e);
      }
    }
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.balajeetm.mystique.util.json.convertor.JsonConvertor#deserializeGroup
   * (java.lang.String, java.lang.Class, java.lang.Class)
   */
  public <T> T deserializeGroup(String jsonString, Class<T> groupClass, Class<?> pojoType)
      throws ConvertorException {
    return deserializeGroupOnJavaType(jsonString, groupClass, pojoType);
  }

  /**
   * Deserialize group on java type.
   *
   * @param <T> the generic type for the Java Group
   * @param <U> the generic type for the Java POJO
   * @param jsonString the json string
   * @param groupClass the group class
   * @param pojoType the pojo type
   * @return the deserialized java group of POJOs
   */
  private <T, U> T deserializeGroupOnJavaType(
      String jsonString, Class<T> groupClass, Class<U> pojoType) {
    T result = null;
    if (Objects.nonNull(jsonString)) {
      JavaType javaType = getJavaType(groupClass, pojoType);
      try {
        result = getObjectMapper().readValue(jsonString, javaType);
      } catch (Exception e) {
        log.error(
            String.format(
                "Error during deserialisation of json string %s to %s<%s>.",
                jsonString, groupClass.getSimpleName(), pojoType.getSimpleName()));
        throw getConvertorException(e);
      }
    }
    return result;
  }

  /**
   * Gets the java type.
   *
   * @param <T> the generic type for Java Group
   * @param <U> the generic type for Java POJO
   * @param groupClass the group class
   * @param pojoType the pojo type
   * @return the java type
   */
  private <T, U> JavaType getJavaType(Class<T> groupClass, Class<U> pojoType) {
    JavaType javaType = null;
    if (List.class.isAssignableFrom(groupClass)) {
      javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, pojoType);
    } else if (Collection.class.isAssignableFrom(groupClass)) {
      javaType = TypeFactory.defaultInstance().constructCollectionType(Collection.class, pojoType);
    }
    return javaType;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.balajeetm.mystique.util.json.convertor.JsonConvertor#deserializeList(
   * java.lang.String, java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> List<T> deserializeList(String jsonString, Class<T> pojoType)
      throws ConvertorException {
    List<T> result = null;
    if (Objects.nonNull(jsonString)) {
      JavaType javaType = getJavaType(List.class, pojoType);
      try {
        return (List<T>) getObjectMapper().readValue(jsonString, javaType);
      } catch (Exception e) {
        log.error(
            String.format(
                "Error during deserialisation of json string %s to List<%s>.",
                jsonString, pojoType.getSimpleName()));
        throw getConvertorException(e);
      }
    }
    return result;
  }

  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static JsonConvertor INSTANCE = new JacksonConvertor();
  }

  private ObjectMapper getObjectMapper() {
    return JacksonFactory.getInstance().getObjectMapper();
  }
}
