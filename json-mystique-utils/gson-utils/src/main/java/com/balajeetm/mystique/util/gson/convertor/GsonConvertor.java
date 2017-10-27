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

import static com.google.gson.internal.$Gson$Preconditions.checkArgument;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.balajeetm.mystique.util.gson.GsonFactory;
import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.balajeetm.mystique.util.json.convertor.JsonConvertor;
import com.balajeetm.mystique.util.json.error.ConvertorException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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

  /**
   * Gets the single instance of GsonConvertor.
   *
   * @return single instance of GsonConvertor
   */
  public static JsonConvertor getInstance() {
    return Creator.INSTANCE;
  }

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized
  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static JsonConvertor INSTANCE = new GsonConvertor();
  }

  /** Instantiates a new gson convertor. */
  private GsonConvertor() {
    GsonFactory factory = GsonFactory.getInstance();
    gsonBuilder = factory.getGsonBuilder();
    gson = factory.getGson();
    jsonLever = JsonLever.getInstance();
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
    try {
      return gson.fromJson(jsonString, pojoType);
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of json string %s to class %s.", jsonString, pojoType));
      throw getConvertorException(e);
    }
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
    try {
      JsonElement jsonElement = getJsonElement(object);
      return gson.fromJson(jsonElement, pojoType);
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of object %s to class %s.", object, pojoType));
      throw getConvertorException(e);
    }
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
    try {
      return gson.fromJson(new InputStreamReader(inputStream), pojoType);
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of json input stream %s to class %s.",
              inputStream, pojoType));
      throw getConvertorException(e);
    }
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
    try {
      return gson.toJson(pojo);
    } catch (Exception e) {
      log.error(String.format("Error during serialisation of object %s.", pojo));
      throw getConvertorException(e);
    }
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
    try {
      checkArgument(groupClass != null && pojoType != null);
      GsonParametrizedType parametrizedType =
          new GsonParametrizedType(groupClass.getName(), pojoType.getName());

      return gson.fromJson(jsonString, parametrizedType);
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of json string %s to %s<%s>.",
              jsonString, groupClass.getSimpleName(), pojoType.getSimpleName()));
      throw getConvertorException(e);
    }
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
    try {
      checkArgument(groupClass != null && pojoType != null);
      GsonParametrizedType parametrizedType =
          new GsonParametrizedType(groupClass.getName(), pojoType.getName());

      return gson.fromJson(new InputStreamReader(inputStream), parametrizedType);
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of json inputstream %s to %s<%s>.",
              inputStream, groupClass.getSimpleName(), pojoType.getSimpleName()));
      throw getConvertorException(e);
    }
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
    try {
      checkArgument(pojoType != null);
      GsonParametrizedType parametrizedType =
          new GsonParametrizedType(List.class.getName(), pojoType.getName());

      return gson.fromJson(new InputStreamReader(inputStream), parametrizedType);
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of json inputstream %s to List<%s>.",
              inputStream, pojoType.getSimpleName()));
      throw getConvertorException(e);
    }
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
    try {
      checkArgument(pojoType != null);
      GsonParametrizedType parametrizedType =
          new GsonParametrizedType(List.class.getName(), pojoType.getName());

      return gson.fromJson(jsonString, parametrizedType);
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of json string %s to List<%s>.",
              jsonString, pojoType.getSimpleName()));
      throw getConvertorException(e);
    }
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
    try {
      checkArgument(pojoType != null);
      JsonElement jsonElement = getJsonElement(object);
      GsonParametrizedType parametrizedType =
          new GsonParametrizedType(List.class.getName(), pojoType.getName());

      return gson.fromJson(jsonElement, parametrizedType);
    } catch (Exception e) {
      log.error(
          String.format(
              "Error during deserialisation of object %s to List<%s>.",
              object, pojoType.getSimpleName()));
      throw getConvertorException(e);
    }
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
}
