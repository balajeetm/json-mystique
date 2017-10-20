/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core.lever;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.balajeetm.mystique.core.MystTurn;
import com.balajeetm.mystique.core.MystiqueFactory;
import com.balajeetm.mystique.core.util.MystiqueConstants;
import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class JsonLever.
 *
 * @author balajeetm
 */

/** The Constant log. */
@Slf4j
public class MystiqueLever extends JsonLever {

  /** The loopy pat. */
  private static String loopyPat = "^\\[\\*\\]$";

  /** The ace pat. */
  private static String acePat = "^@ace\\(([a-zA-Z_0-9$#@!%^&*]+)\\)$";

  /** The value pat. */
  private static String valuePat = "^@value\\(([a-zA-Z_0-9, $#@!%^&*]+)\\)$";

  /** The loopy pattern. */
  private Pattern loopyPattern;

  /** The ace pattern. */
  private Pattern acePattern;

  /** The ace pattern. */
  private Pattern valuePattern;

  /** The factory. */
  private MystiqueFactory factory;

  /** Instantiates a new mystique lever. */
  private MystiqueLever() {
    loopyPattern = Pattern.compile(loopyPat);
    acePattern = Pattern.compile(acePat);
    valuePattern = Pattern.compile(valuePat);
  }

  /**
   * Factory.
   *
   * @return the mystique factory
   */
  private MystiqueFactory factory() {
    if (null == factory) {
      factory = MystiqueFactory.getInstance();
    }
    return factory;
  }

  /**
   * Gets the single instance of MystiqueLever.
   *
   * @return single instance of MystiqueLever
   */
  public static MystiqueLever getInstance() {
    return Creator.INSTANCE;
  }

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized
  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static MystiqueLever INSTANCE = new MystiqueLever();
  }

  /**
   * Checks if is loopy.
   *
   * @param path the path
   * @return the boolean
   */
  private Boolean isLoopy(String path) {
    Boolean loopy = Boolean.FALSE;
    Matcher matcher = loopyPattern.matcher(path);
    while (matcher.find()) {
      loopy = Boolean.TRUE;
    }
    return loopy;
  }

  /**
   * Update fields.
   *
   * @param source the source
   * @param dependencies the dependencies
   * @param aces the aces
   * @param fields the fields
   * @param path the path
   * @return the boolean
   */
  public Boolean updateFields(
      JsonElement source,
      JsonObject dependencies,
      JsonObject aces,
      List<JsonElement> fields,
      JsonArray path) {
    Boolean isLoopy = Boolean.FALSE;
    try {
      JsonElement field = source;
      if (null != path) {
        if (path.size() > 0) {
          try {
            for (int count = 0; count < path.size(); count++) {
              JsonElement p = path.get(count);
              if (isNumber(p)) {
                field = get(field, p);
              } else {
                String key = asString(p);
                if (count == 0 && MystiqueConstants.AT_DEPS.equals(key)) {
                  field = dependencies;
                  continue;
                }
                String ace = getAce(key);
                if (null != ace) {
                  field = aces.get(ace);
                  continue;
                }
                if (isLoopy(key)) {
                  isLoopy = Boolean.TRUE;
                  fields.clear();
                  break;
                } else {
                  p = getPathField(p, aces);
                  field = get(field, p);
                }
              }
            }
          } catch (IllegalStateException e) {
            log.info(
                String.format("Invalid json path %s for %s : %s", path, source, e.getMessage()), e);
            field = JsonNull.INSTANCE;
          }
        }
        fields.add(field);
      }
    }
    /** Would throw an exception for any invalid path, which is logged and ignored */
    catch (RuntimeException e) {
      log.warn(
          String.format(
              "Error getting field from source for %s - %s. Skipping the same",
              path, e.getMessage()),
          e);
    }
    return isLoopy;
  }

  /**
   * Gets the subset.
   *
   * @param source the source
   * @param deps the deps
   * @param aces the aces
   * @param valueObject the value object
   * @return the subset
   */
  public JsonElement subset(
      JsonElement source, JsonObject deps, JsonObject aces, JsonElement valueObject) {
    JsonElement finalValue = null;
    if (valueObject.isJsonArray()) {
      finalValue = subset(source, deps, aces, valueObject.getAsJsonArray());
    } else if (isString(valueObject)) {
      finalValue = subset(source, deps, aces, getJPath(valueObject));
    } else if (isJsonObject(valueObject)) {
      // This is a turn
      JsonObject valueJson = valueObject.getAsJsonObject();
      MystTurn mystique = factory().getMystTurn(valueJson);
      finalValue =
          mystique.transform(Lists.newArrayList(source), deps, aces, valueJson, new JsonObject());
    }
    return finalValue;
  }

  /**
   * Gets the updated aces.
   *
   * @param source the source
   * @param aces the aces
   * @param dependencies the dependencies
   * @param updated the updated
   * @return the updated aces
   */
  public JsonObject getUpdatedAces(
      JsonElement source, JsonObject aces, JsonObject dependencies, JsonObject updated) {
    if (isNotNull(aces)) {
      for (Entry<String, JsonElement> entry : aces.entrySet()) {
        JsonObject value = asJsonObject(entry.getValue());
        // Null check required, since for all other purposes, no turn
        // means a default turn. In this case, turn needs to be executed
        // only if it is explicitly specified
        MystTurn mystique = null != value ? factory().getMystTurn(value) : null;
        if (null != mystique) {
          JsonElement transform =
              mystique.transform(
                  Lists.newArrayList(source), dependencies, aces, value, new JsonObject());
          updated.add(entry.getKey(), transform);
        }
      }
    }
    return updated;
  }

  /**
   * Gets the updated aces.
   *
   * @param source the source
   * @param aces the aces
   * @param dependencies the dependencies
   * @return the updated aces
   */
  protected JsonObject getUpdatedAces(
      JsonElement source, JsonObject aces, JsonObject dependencies) {
    return getUpdatedAces(source, aces, dependencies, aces);
  }

  /**
   * Gets the ace.
   *
   * @param path the path
   * @return the ace
   */
  private String getAce(String path) {
    String index = null;
    Matcher matcher = acePattern.matcher(path);
    while (matcher.find()) {
      index = matcher.group(1);
    }
    return index;
  }

  /**
   * Gets the ace value.
   *
   * @param path the path
   * @return the ace value
   */
  private String getAceValue(String path) {
    String index = null;
    Matcher matcher = valuePattern.matcher(path);
    while (matcher.find()) {
      index = matcher.group(1);
    }
    return index;
  }

  /**
   * Gets the processed path field from an ace.
   *
   * @param field the string representing part of the json path which may include processing of an
   *     ace via @value notations
   * @param aces the pre-processed dependency list in the form of key value pair (json)
   * @return the processed json path field string
   */
  private JsonElement getPathField(JsonElement field, JsonObject aces) {
    if (isNumber(field)) {
      return field;
    } else {
      String ace = getAceValue(asString(field));
      JsonElement output = field;
      if (null != ace) {
        JsonArray newJsonArray = newJsonArray(ace.split(","));
        output = get(aces, newJsonArray);
      }
      return output;
    }
  }

  /**
   * Subset.
   *
   * @param source the source
   * @param deps the deps
   * @param aces the aces
   * @param jPathArray the j path array
   * @return the json element
   */
  private JsonElement subset(
      JsonElement source, JsonObject deps, JsonObject aces, JsonArray jPathArray) {
    JsonElement finalValue = null;
    if (jPathArray.size() == 0) {
      finalValue = getField(source, jPathArray, deps, aces);
    } else {
      JsonElement first = getFirst(jPathArray);
      if (isJsonArray(first)) {
        finalValue = new JsonObject();
        for (JsonElement jsonElement : jPathArray) {
          JsonArray pathArray = asJsonArray(jsonElement);
          JsonElement subset = getField(source, pathArray, deps, aces);
          set(finalValue.getAsJsonObject(), pathArray, subset, aces);
        }
        finalValue = finalValue.getAsJsonObject().get(MystiqueConstants.RESULT);
      } else {
        finalValue = getField(source, jPathArray, deps, aces);
      }
    }
    return finalValue;
  }

  /**
   * Gets the field.
   *
   * @param source the source
   * @param jPath the j path
   * @param deps the deps
   * @param aces the aces
   * @return the field
   */
  public JsonElement getField(
      JsonElement source, JsonArray jPath, JsonObject deps, JsonObject aces) {
    JsonElement field = null;
    try {
      field = source;
      if (null != jPath) {
        for (int count = 0; count < jPath.size(); count++) {
          JsonElement path = jPath.get(count);
          if (isNumber(path)) {
            field = get(field, path);
          } else {
            String key = asString(path);
            if (count == 0 && MystiqueConstants.AT_DEPS.equals(key)) {
              field = deps;
              continue;
            }
            String ace = getAce(key);
            if (null != ace) {
              field = aces.get(ace);
              continue;
            }
            path = getPathField(path, aces);
            field = get(field, path);
          }
        }
      }
    }
    /** Would throw an exception for any invalid path, which is logged and ignored */
    catch (RuntimeException e) {
      log.warn(
          String.format(
              "Error getting field from source for %s - %s. Skipping the same",
              jPath, e.getMessage()),
          e);
      field = null;
    }
    return field;
  }

  /**
   * Sets the field of a json source.
   *
   * @param resultWrapper the json object that wraps the result json. The result is wrapped to
   *     ensure it passed across by reference and fields are updated appropriately
   * @param to the jPath json array defining the full qualified json path to the destination field
   *     where the value must be set
   * @param value the json value that needs to be set to the destination. This can be an Object,
   *     Array or a Primitive
   * @param aces the pre-processed dependency list in the form of key value pair (json)
   * @return the json result wraper object which contains the result in the field called "result"
   */
  public JsonObject set(
      JsonObject resultWrapper, JsonArray to, JsonElement value, JsonObject aces) {
    return set(resultWrapper, to, value, aces, Boolean.FALSE);
  }

  /**
   * Sets the field of a json source.
   *
   * @param resultWrapper the json object that wraps the result json. The result is wrapped to
   *     ensure it passed across by reference and fields are updated appropriately
   * @param to the jPath json array defining the full qualified json path to the destination field
   *     where the value must be set
   * @param value the json value that needs to be set to the destination. This can be an Object,
   *     Array or a Primitive
   * @param aces the pre-processed dependency list in the form of key value pair (json)
   * @param optional the flag that determines if a null value must be set in the destination. If
   *     optional is TRUE, null values are not set in the destination
   * @return the json result wraper object which contains the result in the field called "result"
   */
  public JsonObject set(
      JsonObject resultWrapper,
      JsonArray to,
      JsonElement value,
      JsonObject aces,
      Boolean optional) {

    /**
     * Holding a mutex on result wrapper and not making the method synchronized because, when
     * multiple unrelated threads might be calling mystique for transformation. The resource of
     * contention is only the result wrapper
     */
    synchronized (resultWrapper) {
      if (optional && isNull(value)) {
        // Do Not update result wrapper
        return resultWrapper;
      }
      if (isNotNull(to)) {
        JsonElement result = resultWrapper.get(MystiqueConstants.RESULT);
        JsonElement field = result;
        if (to.size() > 0) {
          JsonElement previousPath = null;
          JsonElement currentPath = null;
          Iterator<JsonElement> iterator = to.iterator();
          if (iterator.hasNext()) {
            previousPath = getPathField(iterator.next(), aces);
          }

          while (iterator.hasNext()) {
            currentPath = getPathField(iterator.next(), aces);

            // get the field
            field = getRepleteField(field, previousPath, currentPath);
            result = updateResult(result, field);
            field =
                isNumber(previousPath)
                    ? field.getAsJsonArray().get(previousPath.getAsInt())
                    : field.getAsJsonObject().get(previousPath.getAsString());
            previousPath = currentPath;
          }

          field = setField(field, previousPath, value);
          result = updateResult(result, field);
        } else {
          result = merge(result, value);
        }
        resultWrapper.add(MystiqueConstants.RESULT, result);
      }
      return resultWrapper;
    }
  }
}
