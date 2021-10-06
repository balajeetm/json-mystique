/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.convertor;

import com.balajeetm.mystique.util.gson.GsonFactory;
import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.balajeetm.mystique.util.json.convertor.JsonConvertor;
import com.balajeetm.mystique.util.json.error.ConvertorException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkArgument;

/** The Class GsonConvertor. */
@Slf4j
public class GsonConvertor implements JsonConvertor {

  /**
   * Gets the gson.
   *
   * @return the gson
   */
  @Getter

  /**
   * Sets the gson.
   *
   * @param gson the new gson
   */
  @Setter
  private Gson gson;

  /** The gson builder. */
  private GsonBuilder gsonBuilder;

  /** The json lever. */
  private JsonLever jsonLever;

  /** Instantiates a new gson convertor. */
  private GsonConvertor() {
    GsonFactory factory = GsonFactory.getInstance();
    gsonBuilder = factory.getGsonBuilder();
    gson = factory.getGson();
    jsonLever = JsonLever.getInstance();
  }

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized

  /**
   * Gets the single instance of GsonConvertor.
   *
   * @return single instance of GsonConvertor
   */
  public static JsonConvertor getInstance() {
    return Creator.INSTANCE;
  }

  /** Update gson. */
  private void updateGson() {
    gson = gsonBuilder.create();
  }

  /**
   * Register type adapter.
   *
   * @param adapters the adapters
   */
  public void registerTypeAdapter(GsonTypeAdapter... adapters) {
    if (!ArrayUtils.isEmpty(adapters)) {
      for (GsonTypeAdapter gsonTypeAdapter : adapters) {
        gsonBuilder.registerTypeAdapter(gsonTypeAdapter.getType(), gsonTypeAdapter.getAdapter());
      }
      updateGson();
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.futuresight.util.mystique.lever.ConvertorInterface#deserialize(java.
   * lang.String, java.lang.Class)
   */
  @Override
  public <T> T deserialize(String jsonString, Class<T> pojoType) throws ConvertorException {
    T result = null;
    try {
      if (Objects.nonNull(jsonString)) {
        result = gson.fromJson(jsonString, pojoType);
      }
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of json string %s to class %s.", jsonString, pojoType));
      throw getConvertorException(e);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.futuresight.util.mystique.lever.ConvertorInterface#deserialize(java.
   * io.InputStream, java.lang.Class)
   */
  @Override
  public <T> T deserialize(InputStream inputStream, Class<T> pojoType) throws ConvertorException {
    T result = null;
    try {
      if (Objects.nonNull(inputStream)) {
        result = gson.fromJson(new InputStreamReader(inputStream), pojoType);
      }
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of json input stream %s to class %s.",
              inputStream, pojoType));
      throw getConvertorException(e);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.futuresight.util.mystique.lever.ConvertorInterface#deserialize(java.
   * lang.Object, java.lang.Class)
   */
  @Override
  public <T> T deserialize(Object object, Class<T> pojoType) throws ConvertorException {
    T result = null;
    if (Objects.nonNull(object)) {
      if (object instanceof String) {
        result = deserialize((String) object, pojoType);
      } else if (object instanceof InputStream) {
        result = deserialize((InputStream) object, pojoType);
      } else {
        try {
          JsonElement jsonElement = getJsonElement(object);
          result = gson.fromJson(jsonElement, pojoType);
        } catch (Exception e) {
          log.error(
              String.format(
                  "Error during deserialisation of object %s to class %s.", object, pojoType));
          throw getConvertorException(e);
        }
      }
    }
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.futuresight.util.mystique.lever.ConvertorInterface#serialize(java.
   * lang.Object)
   */
  @Override
  public String serialize(Object pojo) throws ConvertorException {
    String result = null;
    try {
      if (Objects.nonNull(pojo)) {
        result = gson.toJson(pojo);
      }
    } catch (Exception e) {
      log.error(String.format("Error during serialisation of object %s.", pojo));
      throw getConvertorException(e);
    }
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.futuresight.util.mystique.lever.ConvertorInterface#deserializeGroup(
   * java.lang.String, java.lang.Class, java.lang.Class)
   */
  @Override
  public <T> T deserializeGroup(String jsonString, Class<T> groupClass, Class<?> pojoType)
      throws ConvertorException {
    T result = null;
    if (Objects.nonNull(jsonString)) {
      try {
        checkArgument(groupClass != null && pojoType != null);
        GsonParametrizedType parametrizedType =
            new GsonParametrizedType(groupClass.getName(), pojoType.getName());

        result = gson.fromJson(jsonString, parametrizedType);
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

  /*
   * (non-Javadoc)
   *
   * @see
   * com.futuresight.util.mystique.lever.ConvertorInterface#deserializeGroup(
   * java.io.InputStream, java.lang.Class, java.lang.Class)
   */
  @Override
  public <T> T deserializeGroup(InputStream inputStream, Class<T> groupClass, Class<?> pojoType)
      throws ConvertorException {
    T result = null;
    if (Objects.nonNull(inputStream)) {
      try {
        checkArgument(groupClass != null && pojoType != null);
        GsonParametrizedType parametrizedType =
            new GsonParametrizedType(groupClass.getName(), pojoType.getName());

        result = gson.fromJson(new InputStreamReader(inputStream), parametrizedType);
      } catch (Exception e) {
        log.error(
            String.format(
                "Error during deserialisation of json inputstream %s to %s<%s>.",
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
   * com.futuresight.util.mystique.lever.ConvertorInterface#deserializeList(
   * java.io.InputStream, java.lang.Class)
   */
  @Override
  public <T> List<T> deserializeList(InputStream inputStream, Class<T> pojoType)
      throws ConvertorException {
    List<T> result = null;
    if (Objects.nonNull(inputStream)) {
      try {
        checkArgument(pojoType != null);
        GsonParametrizedType parametrizedType =
            new GsonParametrizedType(List.class.getName(), pojoType.getName());

        result = gson.fromJson(new InputStreamReader(inputStream), parametrizedType);
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
   * com.futuresight.util.mystique.lever.ConvertorInterface#deserializeList(
   * java.lang.String, java.lang.Class)
   */
  @Override
  public <T> List<T> deserializeList(String jsonString, Class<T> pojoType)
      throws ConvertorException {
    List<T> result = null;
    if (Objects.nonNull(jsonString)) {
      try {
        checkArgument(pojoType != null);
        GsonParametrizedType parametrizedType =
            new GsonParametrizedType(List.class.getName(), pojoType.getName());

        result = gson.fromJson(jsonString, parametrizedType);
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

  /*
   * (non-Javadoc)
   *
   * @see
   * com.futuresight.util.mystique.lever.ConvertorInterface#deserializeList(
   * java.lang.Object, java.lang.Class)
   */
  @Override
  public <T> List<T> deserializeList(Object object, Class<T> pojoType) throws ConvertorException {
    List<T> result = null;
    if (Objects.nonNull(object)) {
      try {
        checkArgument(pojoType != null);
        JsonElement jsonElement = getJsonElement(object);
        GsonParametrizedType parametrizedType =
            new GsonParametrizedType(List.class.getName(), pojoType.getName());

        result = gson.fromJson(jsonElement, parametrizedType);
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

  /**
   * Gets the convertor exception.
   *
   * @param e the exception
   * @return the convertor exception
   */
  private ConvertorException getConvertorException(Exception e) {
    log.debug("Error occured during conversion", e);
    return new ConvertorException(e);
  }

  /**
   * Gets the json element from a Java POJO.
   *
   * @param obj the obj
   * @return the json element
   */
  private JsonElement getJsonElement(Object obj) {
    JsonElement jsonElement =
        (obj instanceof JsonElement) ? (JsonElement) obj : gson.toJsonTree(obj);
    return jsonLever.isNull(jsonElement) ? null : jsonElement;
  }

  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static JsonConvertor INSTANCE = new GsonConvertor();
  }
}
