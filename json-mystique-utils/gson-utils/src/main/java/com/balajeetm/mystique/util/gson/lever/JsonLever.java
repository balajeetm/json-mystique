/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 15 Oct, 2017 by balajeemohan
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.lever;

import java.text.DateFormat;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.core.convert.ConversionException;

import com.balajeetm.mystique.util.gson.GsonFactory;
import com.balajeetm.mystique.util.gson.convertor.GsonConvertor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/** The Constant log. */

/** The Constant log. */
@Slf4j
public class JsonLever {

  /**
   * Gets the json parser.
   *
   * @return the json parser
   */

  /**
   * Gets the json parser.
   *
   * @return the json parser
   */
  @Getter private JsonParser jsonParser;

  /**
   * Gets the gson.
   *
   * @return the gson
   */

  /**
   * Gets the gson.
   *
   * @return the gson
   */
  @Getter private Gson gson;

  /**
   * Gets the single instance of JsonLever.
   *
   * @return single instance of JsonLever
   */
  public static JsonLever getInstance() {
    return Creator.INSTANCE;
  }

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized
  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static JsonLever INSTANCE = new JsonLever();
  }

  /** Instantiates a new json lever. */
  protected JsonLever() {
    jsonParser = GsonFactory.getInstance().getJsonParser();
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat(DateFormat.LONG, DateFormat.LONG);
    gson = gsonBuilder.create();
  }

  /**
   * Gets the json element in the specified jpath.
   *
   * @param source the json source
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the json element at the specified path
   */
  public JsonElement get(JsonElement source, String jpath) {
    if (StringUtils.equals(".", jpath)) {
      return source;
    }
    return get(source, getJpath(jpath));
  }

  /**
   * Gets the json element in the specified jpath.
   *
   * @param source the source, can be a POJO, json string, or a JsonElement
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the json element at the specified path
   */
  public JsonElement get(Object source, String jpath) {
    return get(jsonify(source), jpath);
  }

  /**
   * Gets the json element in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1]) is '2'. Array indexes need to be specified as numerals.
   *     Strings are always presumed to be field names.
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonElement get(JsonElement source, JsonArray jpath) {
    JsonElement result = JsonNull.INSTANCE;
    if (isNotNull(jpath)) {
      result = source;
      for (JsonElement path : jpath) {
        try {
          if (isNumber(path)) {
            result = result.getAsJsonArray().get(path.getAsInt());
          } else {
            result = result.getAsJsonObject().get(path.getAsString());
          }
        } catch (Exception e) {
          return JsonNull.INSTANCE;
        }
      }
    }

    return result;
  }

  /**
   * Gets the json element in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1]) is '2'. Array indexes need to be specified as numerals.
   *     Strings are always presumed to be field names.
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonElement get(Object source, JsonArray jpath) {
    return get(jsonify(source), jpath);
  }

  /**
   * Gets the json element in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonElement get(JsonElement source, Object... jpath) {
    return get(source, newJsonArray(jpath));
  }

  /**
   * Gets the json element in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonElement get(Object source, Object... jpath) {
    return get(jsonify(source), newJsonArray(jpath));
  }

  /**
   * Gets the json element in the specified jpath or returns a default value.
   *
   * @param source the source
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @param defaultValue the default value
   * @return the json element returns defaultValue for any invalid path or if the item at specified
   *     location is Null return the source if the input jpath is '.'
   */
  public JsonElement get(JsonElement source, String jpath, JsonElement defaultValue) {
    JsonElement result = get(source, jpath);
    return isNull(result) ? defaultValue : result;
  }

  /**
   * Gets the json element in the specified jpath oe returns a default value.
   *
   * @param source the source, can be a POJO, json string, or a JsonElement
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @param defaultValue the default value
   * @return the json element returns defaultValue for any invalid path or if the item at specified
   *     location is Null return the source if the input jpath is '.'
   */
  public JsonElement get(Object source, String jpath, JsonElement defaultValue) {
    return get(jsonify(source), jpath, defaultValue);
  }

  /**
   * Gets the.
   *
   * <p>Gets the json element in the specified jpath or returns a default value
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1]) is '2'. Array indexes need to be specified as numerals.
   *     Strings are always presumed to be field names.
   * @param defaultValue the default value
   * @return the json element returns defaultValue for any invalid path or if the item at specified
   *     location is Null return the source if the input jpath is '.'
   */
  public JsonElement get(JsonElement source, JsonArray jpath, JsonElement defaultValue) {
    JsonElement result = get(source, jpath);
    return isNull(result) ? defaultValue : result;
  }

  /**
   * Gets the.
   *
   * <p>Gets the json element in the specified jpath or returns a default value
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1]) is '2'. Array indexes need to be specified as numerals.
   *     Strings are always presumed to be field names.
   * @param defaultValue the default value
   * @return the json element returns defaultValue for any invalid path or if the item at specified
   *     location is Null return the source if the input jpath is '.'
   */
  public JsonElement get(Object source, JsonArray jpath, JsonElement defaultValue) {
    return get(jsonify(source), jpath, defaultValue);
  }

  /**
   * Sets the json element at the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a.b.c.1", 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Strings that can be
   *     casted to numerals are presumed to be array indexes
   * @param value the value
   * @return the json element
   */
  public JsonElement set(JsonElement source, String jpath, JsonElement value) {
    return set(source, getJpath(jpath), value);
  }

  /**
   * Sets the json element at the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @param value the value
   * @return the json element
   */
  public JsonElement set(JsonElement source, JsonArray jpath, JsonElement value) {
    JsonElement result = JsonNull.INSTANCE;
    if (isNotNull(jpath)) {
      result = source;
      JsonElement field = result;
      if (jpath.size() > 0) {
        JsonElement previousPath = null;
        JsonElement currentPath = null;
        Iterator<JsonElement> iterator = jpath.iterator();
        if (iterator.hasNext()) {
          previousPath = iterator.next();
        }

        while (iterator.hasNext()) {
          currentPath = iterator.next();
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
      }
    }
    return result;
  }

  /**
   * Returns the JsonObject identified by the fully qualified json path as a json object if
   * possible. Else returns null
   *
   * @param source the json source element
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public JsonObject getJsonObject(JsonElement source, String jpath) {
    return asJsonObject(get(source, jpath));
  }

  /**
   * Returns the JsonObject identified by the fully qualified json path as a json object if
   * possible. Else returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public JsonObject getJsonObject(Object source, String jpath) {
    return asJsonObject(get(source, jpath));
  }

  /**
   * Returns the JsonObject identified by the fully qualified json path as a json object if
   * possible. Else returns null
   *
   * @param source the json source element
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public JsonObject getJsonObject(JsonElement source, JsonArray jpath) {
    return asJsonObject(get(source, jpath));
  }

  /**
   * Returns the JsonObject identified by the fully qualified json path as a json object if
   * possible. Else returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public JsonObject getJsonObject(Object source, JsonArray jpath) {
    return asJsonObject(get(source, jpath));
  }

  /**
   * Gets the json object in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonObject getJsonObject(JsonElement source, Object... jpath) {
    return asJsonObject(get(source, newJsonArray(jpath)));
  }

  /**
   * Gets the json object in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonObject getJsonObject(Object source, Object... jpath) {
    return asJsonObject(get(jsonify(source), newJsonArray(jpath)));
  }

  /**
   * Returns the JsonArray identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public JsonArray getJsonArray(JsonElement source, String jpath) {
    return asJsonArray(get(source, jpath));
  }

  /**
   * Returns the JsonArray identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public JsonArray getJsonArray(Object source, String jpath) {
    return asJsonArray(get(source, jpath));
  }

  /**
   * Returns the JsonArray identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public JsonArray getJsonArray(JsonElement source, JsonArray jpath) {
    return asJsonArray(get(source, jpath));
  }

  /**
   * Returns the JsonArray identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public JsonArray getJsonArray(Object source, JsonArray jpath) {
    return asJsonArray(get(source, jpath));
  }

  /**
   * Gets the JsonArray in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonArray getJsonArray(JsonElement source, Object... jpath) {
    return asJsonArray(get(source, newJsonArray(jpath)));
  }

  /**
   * Gets the JsonArray in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonArray getJsonArray(Object source, Object... jpath) {
    return asJsonArray(get(jsonify(source), newJsonArray(jpath)));
  }

  /**
   * Returns the String identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public String getString(JsonElement source, String jpath) {
    return asString(get(source, jpath));
  }

  /**
   * Returns the String identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public String getString(Object source, String jpath) {
    return asString(get(source, jpath));
  }

  /**
   * Returns the String identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public String getString(JsonElement source, JsonArray jpath) {
    return asString(get(source, jpath));
  }

  /**
   * Returns the String identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public String getString(Object source, JsonArray jpath) {
    return asString(get(source, jpath));
  }

  /**
   * Gets the String in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public String getString(JsonElement source, Object... jpath) {
    return asString(get(source, newJsonArray(jpath)));
  }

  /**
   * Gets the String in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public String getString(Object source, Object... jpath) {
    return asString(get(jsonify(source), newJsonArray(jpath)));
  }

  /**
   * Returns the Long identified by the fully qualified json path as a json object if possible. Else
   * returns null
   *
   * @param source the json source element
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public Long getLong(JsonElement source, String jpath) {
    return asLong(get(source, jpath));
  }

  /**
   * Returns the Long identified by the fully qualified json path as a json object if possible. Else
   * returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public Long getLong(Object source, String jpath) {
    return asLong(get(source, jpath));
  }

  /**
   * Returns the Long identified by the fully qualified json path as a json object if possible. Else
   * returns null
   *
   * @param source the json source element
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public Long getLong(JsonElement source, JsonArray jpath) {
    return asLong(get(source, jpath));
  }

  /**
   * Returns the Long identified by the fully qualified json path as a json object if possible. Else
   * returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public Long getLong(Object source, JsonArray jpath) {
    return asLong(get(source, jpath));
  }

  /**
   * Gets the Long in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public Long getLong(JsonElement source, Object... jpath) {
    return asLong(get(source, newJsonArray(jpath)));
  }

  /**
   * Gets the Long in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public Long getLong(Object source, Object... jpath) {
    return asLong(get(jsonify(source), newJsonArray(jpath)));
  }

  /**
   * Returns the Boolean identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public Boolean getBoolean(JsonElement source, String jpath) {
    return asBoolean(get(source, jpath));
  }

  /**
   * Returns the Boolean identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public Boolean getBoolean(Object source, String jpath) {
    return asBoolean(get(source, jpath));
  }

  /**
   * Returns the Boolean identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public Boolean getBoolean(JsonElement source, JsonArray jpath) {
    return asBoolean(get(source, jpath));
  }

  /**
   * Returns the Boolean identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public Boolean getBoolean(Object source, JsonArray jpath) {
    return asBoolean(get(source, jpath));
  }

  /**
   * Gets the Boolean in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public Boolean getBoolean(JsonElement source, Object... jpath) {
    return asBoolean(get(source, newJsonArray(jpath)));
  }

  /**
   * Gets the Boolean in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public Boolean getBoolean(Object source, Object... jpath) {
    return asBoolean(get(jsonify(source), newJsonArray(jpath)));
  }

  /**
   * Returns the JsonPrimitive identified by the fully qualified json path as a json object if
   * possible. Else returns null
   *
   * @param source the json source element
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public JsonPrimitive getJsonPrimitive(JsonElement source, String jpath) {
    return asJsonPrimitive(get(source, jpath));
  }

  /**
   * Returns the JsonPrimitive identified by the fully qualified json path as a json object if
   * possible. Else returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public JsonPrimitive getJsonPrimitive(Object source, String jpath) {
    return asJsonPrimitive(get(source, jpath));
  }

  /**
   * Returns the JsonPrimitive identified by the fully qualified json path as a json object if
   * possible. Else returns null
   *
   * @param source the json source element
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public JsonPrimitive getJsonPrimitive(JsonElement source, JsonArray jpath) {
    return asJsonPrimitive(get(source, jpath));
  }

  /**
   * Returns the JsonPrimitive identified by the fully qualified json path as a json object if
   * possible. Else returns null
   *
   * @param source the json source element. Can be any Java POJO, that can be jsonified, or a String
   *     representing a valid json
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @return the field of interest as json object
   */
  public JsonPrimitive getJsonPrimitive(Object source, JsonArray jpath) {
    return asJsonPrimitive(get(source, jpath));
  }

  /**
   * Gets the JsonPrimitive in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonPrimitive getJsonPrimitive(JsonElement source, Object... jpath) {
    return asJsonPrimitive(get(source, newJsonArray(jpath)));
  }

  /**
   * Gets the JsonPrimitive in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", 1]) is '2'. Numerals are presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonPrimitive getJsonPrimitive(Object source, Object... jpath) {
    return asJsonPrimitive(get(jsonify(source), newJsonArray(jpath)));
  }

  /**
   * Checks if is null.
   *
   * @param source the source
   * @return the boolean
   */
  public Boolean isNull(JsonElement source) {
    return null == source || source.isJsonNull();
  }

  /**
   * Checks if is not null.
   *
   * @param source the source
   * @return the boolean
   */
  public Boolean isNotNull(JsonElement source) {
    return !isNull(source);
  }

  /**
   * Checks if is json array.
   *
   * @param source the source
   * @return the boolean
   */
  public Boolean isArray(JsonElement source) {
    return isNotNull(source) && source.isJsonArray();
  }

  /**
   * Checks if is json object.
   *
   * @param source the source
   * @return the boolean
   */
  public Boolean isObject(JsonElement source) {
    return isNotNull(source) && source.isJsonObject();
  }

  /**
   * Checks if is json primitive.
   *
   * @param source the source
   * @return the boolean
   */
  public Boolean isPrimitive(JsonElement source) {
    return isNotNull(source) && source.isJsonPrimitive();
  }

  /**
   * Checks if is number.
   *
   * @param source the source
   * @return the boolean
   */
  public Boolean isNumber(JsonElement source) {
    return isNotNull(source) && source.isJsonPrimitive() && source.getAsJsonPrimitive().isNumber();
  }

  /**
   * Checks if is string.
   *
   * @param source the source
   * @return the boolean
   */
  public Boolean isString(JsonElement source) {
    return isNotNull(source) && source.isJsonPrimitive() && source.getAsJsonPrimitive().isString();
  }

  /**
   * Checks if is boolean.
   *
   * @param source the source
   * @return the boolean
   */
  public Boolean isBoolean(JsonElement source) {
    return isNotNull(source) && source.isJsonPrimitive() && source.getAsJsonPrimitive().isBoolean();
  }

  /**
   * Returns the source json as a json primitive if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as a json primitive
   */
  public JsonPrimitive asJsonPrimitive(JsonElement source) {
    return asJsonPrimitive(source, null);
  }

  /**
   * Returns the source json as a json primitive if possible. Else returns the default value
   *
   * @param source the source
   * @param defaultValue the default value
   * @return the json primitive
   */
  public JsonPrimitive asJsonPrimitive(JsonElement source, JsonPrimitive defaultValue) {
    return isPrimitive(source) ? source.getAsJsonPrimitive() : defaultValue;
  }

  /**
   * Returns the source json as a json object if possible. Else returns null
   *
   * @param element the element
   * @return the source json as a json object
   */
  public JsonObject asJsonObject(JsonElement element) {
    return asJsonObject(element, (JsonObject) null);
  }

  /**
   * Returns the source json as a json object if possible. Else returns the default json
   *
   * @param source the source json element
   * @param defaultJson the default json
   * @return the source json as a json object
   */
  public JsonObject asJsonObject(JsonElement source, JsonObject defaultJson) {
    return isObject(source) ? source.getAsJsonObject() : defaultJson;
  }

  /**
   * Returns the source json as a json array if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as a json array
   */
  public JsonArray asJsonArray(JsonElement source) {
    return asJsonArray(source, (JsonArray) null);
  }

  /**
   * Returns the source json as a json array if possible. Else returns the default json
   *
   * @param source the source json element
   * @param defaultArray the default array
   * @return the source json as a json array
   */
  public JsonArray asJsonArray(JsonElement source, JsonArray defaultArray) {
    return isArray(source) ? source.getAsJsonArray() : defaultArray;
  }

  /**
   * Returns the source json as a json element if not null. Else returns the default json
   *
   * @param source the source json element
   * @param defaultJson the default json
   * @return the source json as a non null json element
   */
  public JsonElement asJsonElement(JsonElement source, JsonElement defaultJson) {
    return isNotNull(source) ? source : defaultJson;
  }

  /**
   * Returns the source json as a string if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as a string
   */
  public String asString(JsonElement source) {
    return asString(source, (String) null);
  }

  /**
   * Returns the source json as a string if possible. Else returns the default string
   *
   * @param source the source json element
   * @param defaultStr the default string
   * @return the source json as a string
   */
  public String asString(JsonElement source, String defaultStr) {
    return isString(source) ? source.getAsString() : defaultStr;
  }

  /**
   * Returns the source json as a boolean if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as a boolean
   */
  public Boolean asBoolean(JsonElement source) {
    return asBoolean(source, (Boolean) null);
  }

  /**
   * Returns the source json as a boolean if possible. Else returns the default boolean
   *
   * @param source the source json element
   * @param defaultBool the default boolean
   * @return the source json as a boolean
   */
  public Boolean asBoolean(JsonElement source, Boolean defaultBool) {
    return isBoolean(source) ? (Boolean) source.getAsBoolean() : defaultBool;
  }

  /**
   * Returns the source json as a long if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as a long
   */
  public Long asLong(JsonElement source) {
    return asLong(source, (Long) null);
  }

  /**
   * Returns the source json as a long if possible. Else returns the default long
   *
   * @param source the source json element
   * @param defaultLong the default long
   * @return the source json as a long
   */
  public Long asLong(JsonElement source, Long defaultLong) {
    return isNumber(source) ? (Long) source.getAsLong() : defaultLong;
  }

  /**
   * Returns the source json as an integer if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as an integer
   */
  public Integer asInt(JsonElement source) {
    return asInt(source, (Integer) null);
  }

  /**
   * Returns the source json as an Integer if possible. Else returns the default integer
   *
   * @param source the source json element
   * @param defaultInt the default integer
   * @return the source json as an integer
   */
  public Integer asInt(JsonElement source, Integer defaultInt) {
    return isNumber(source) ? (Integer) source.getAsInt() : defaultInt;
  }

  /**
   * Gets the first json element, if any, in the list. Else returns null
   *
   * @param elements the json elements
   * @return the first json element in the list
   */
  public JsonElement getFirst(Iterable<JsonElement> elements) {
    JsonElement first = null;
    if (!IterableUtils.isEmpty(elements)) {
      first = IterableUtils.get(elements, 0);
    }
    return first;
  }

  /**
   * Gets the jpath for a '.' separated string defining the fully qualified path of a field in Json.
   * Array Indexes are referred via numbers.
   *
   * @param jpath the jpath
   * @return the fully qualified path as a JsonArray. Array Indexes are represented as numbers
   */
  public JsonArray getJpath(String jpath) {
    JsonArray jlist = new JsonArray();
    if (!StringUtils.contains(jpath, ".")) {
      jlist.add(getTypedPath(jpath));
    } else {
      String[] split = StringUtils.split(jpath, ".");
      for (String sp : split) {
        jlist.add(getTypedPath(sp));
      }
    }
    return jlist;
  }

  /**
   * Gets the jpath for a '.' separated string defining the fully qualified path of a field in Json.
   * Array Indexes are referred via numbers. If the input is anything apart from String,
   * JsonPrimitive String or JsonArray, it returns null
   *
   * @param jpath the jpath
   * @return the fully qualified path as a JsonArray. Array Indexes are represented as numbers
   */
  public JsonArray getJpath(JsonElement jpath) {
    JsonArray path = null;
    if (isArray(jpath)) {
      path = asJsonArray(jpath);
    } else if (isString(jpath)) {
      path = getJpath(asString(jpath));
    }
    return path;
  }

  /**
   * New json array.
   *
   * @param path the path
   * @return the json array
   */
  public JsonArray newJsonArray(Object... path) {
    JsonArray output = new JsonArray();
    for (Object p : path) {
      output.add(getTypedPath(p, Boolean.FALSE));
    }
    return output;
  }

  /**
   * Null safe toString method for a json element.
   *
   * @param source the element
   * @return the json string
   */
  public String toString(JsonElement source) {
    return null != source ? source.toString() : null;
  }

  /**
   * Deep clone.
   *
   * @param source the source json
   * @return the deep clone of the json
   */
  public JsonElement deepClone(JsonElement source) {
    return isNotNull(source) ? source.deepCopy() : source;
  }

  /**
   * Deep clone.
   *
   * @param source the source json
   * @return the deep clone of the json
   */
  public JsonObject deepClone(JsonObject source) {
    return isNotNull(source) ? source.deepCopy() : source;
  }

  /**
   * Deep clone.
   *
   * @param source the source json
   * @return the deep clone of the json
   */
  public JsonArray deepClone(JsonArray source) {
    return isNotNull(source) ? source.deepCopy() : source;
  }

  /**
   * Deep clone.
   *
   * @param source the source json
   * @return the deep clone of the json
   */
  public JsonPrimitive deepClone(JsonPrimitive source) {
    return isNotNull(source) ? source.deepCopy() : source;
  }

  /**
   * Gets the subset json from a source json.
   *
   * @param source the json source which can be Object, Array or a Primitive
   * @param jPathArray the array of jPaths defining the full qualified json paths to the required
   *     fields
   * @return the json subset
   */
  public JsonElement subset(JsonElement source, JsonArray jPathArray) {
    JsonElement finalValue = JsonNull.INSTANCE;
    if (jPathArray.size() == 0) {
      finalValue = source;
    } else {
      finalValue = new JsonObject();
      for (JsonElement jsonElement : jPathArray) {
        JsonElement subset = JsonNull.INSTANCE;
        if (isArray(jsonElement)) {
          JsonArray pathArray = asJsonArray(jsonElement);
          subset = get(source, pathArray);
          finalValue = set(finalValue, pathArray, subset);
        } else if (isString(jsonElement)) {
          String jpath = asString(jsonElement);
          subset = get(source, jpath);
          finalValue = set(finalValue, jpath, subset);
        } else {
          finalValue = JsonNull.INSTANCE;
          break;
        }
      }
    }
    return finalValue;
  }

  /**
   * Simple merge of two json objects. Json fields are not recursively merged
   *
   * @param to the json object to which the other json must be merged
   * @param from the json object which should be merged
   * @return the merged json object
   */
  public JsonObject simpleMerge(JsonObject to, JsonObject from) {
    from = asJsonObject(from, new JsonObject());
    to = asJsonObject(to, new JsonObject());
    for (Entry<String, JsonElement> entry : from.entrySet()) {
      to.add(entry.getKey(), entry.getValue());
    }
    return to;
  }

  /**
   * Simple merge of multiple json objects to a destination json.
   *
   * @param to the json object to which the other json must be merged
   * @param from the json objects which should be merged
   * @return the merged json object
   */
  public JsonObject simpleMerge(JsonObject to, JsonObject... from) {
    to = asJsonObject(to, new JsonObject());
    JsonObject finalTo = to;
    for (JsonObject jsonObject : from) {
      finalTo = simpleMerge(finalTo, jsonObject);
    }
    return finalTo;
  }

  /**
   * A recursive merge of two json elements.
   *
   * @param source1 the first json element
   * @param source2 the second json element
   * @return the recursively merged json element
   */
  public JsonElement merge(JsonElement source1, JsonElement source2) {
    return merge(source1, source2, Boolean.FALSE);
  }

  /**
   * A recursive merge of two json elements.
   *
   * @param source1 the first json element
   * @param source2 the second json element
   * @param mergeArray the flag to denote if arrays should be merged
   * @return the recursively merged json element
   */
  public JsonElement merge(JsonElement source1, JsonElement source2, Boolean mergeArray) {
    mergeArray = null == mergeArray ? Boolean.FALSE : mergeArray;
    JsonElement result = JsonNull.INSTANCE;
    source1 = asJsonElement(source1, JsonNull.INSTANCE);
    source2 = asJsonElement(source2, JsonNull.INSTANCE);
    if (source1.getClass().equals(source2.getClass())) {
      if (source1.isJsonObject()) {
        JsonObject obj1 = asJsonObject(source1);
        JsonObject obj2 = asJsonObject(source2);
        result = obj1;
        JsonObject resultObj = result.getAsJsonObject();
        for (Entry<String, JsonElement> entry : obj1.entrySet()) {
          String key = entry.getKey();
          JsonElement value1 = entry.getValue();
          JsonElement value2 = obj2.get(key);
          JsonElement merge = merge(value1, value2, mergeArray);
          resultObj.add(key, merge);
        }
        for (Entry<String, JsonElement> entry : obj2.entrySet()) {
          String key = entry.getKey();
          if (!resultObj.has(key)) {
            resultObj.add(key, entry.getValue());
          }
        }
      } else if (source1.isJsonArray()) {
        result = new JsonArray();
        JsonArray resultArray = result.getAsJsonArray();
        JsonArray array1 = asJsonArray(source1);
        JsonArray array2 = asJsonArray(source2);
        int index = 0;
        int a1size = array1.size();
        int a2size = array2.size();

        if (!mergeArray) {
          for (; index < a1size && index < a2size; index++) {
            resultArray.add(merge(array1.get(index), array2.get(index), mergeArray));
          }
        }

        for (; index < a1size; index++) {
          resultArray.add(array1.get(index));
        }

        index = mergeArray ? 0 : index;

        for (; index < a2size; index++) {
          resultArray.add(array2.get(index));
        }

      } else {
        result = source1 != null ? source1 : source2;
      }
    } else {
      result = isNotNull(source1) ? source1 : source2;
    }
    return result;
  }

  /**
   * Update result.
   *
   * @param result the result
   * @param field the field
   * @return the json element
   */
  protected JsonElement updateResult(JsonElement result, JsonElement field) {
    if (isNull(result)) {
      result = field;
    }
    return result;
  }

  /**
   * Sets the field.
   *
   * @param field the field
   * @param path the path
   * @param value the value
   * @return the json element
   */
  protected JsonElement setField(JsonElement field, JsonElement path, JsonElement value) {
    if (isNumber(path)) {
      JsonArray jArray = asJsonArray(field.getAsJsonArray(), new JsonArray());
      int index = path.getAsInt();
      repleteArray(jArray, index, JsonNull.class);
      jArray.set(index, value);
      field = jArray;
    } else {
      JsonObject jObject = asJsonObject(field, new JsonObject());
      jObject.add(path.getAsString(), value);
      field = jObject;
    }
    return field;
  }

  /**
   * Gets the typed path.
   *
   * @param p the p
   * @return the typed path
   */
  private JsonElement getTypedPath(Object p) {
    return getTypedPath(p, Boolean.TRUE);
  }

  /**
   * Gets the typed path.
   *
   * @param p the p
   * @param strToNum the str to num
   * @return the typed path
   */
  private JsonElement getTypedPath(Object p, Boolean strToNum) {
    JsonElement result = JsonNull.INSTANCE;
    if (p instanceof Number) {
      result = new JsonPrimitive((Number) p);
    } else if (p instanceof String) {
      String sp = StringUtils.trimToEmpty((String) p);
      result =
          strToNum
              ? (NumberUtils.isCreatable(sp)
                  ? new JsonPrimitive(NumberUtils.toInt(sp))
                  : new JsonPrimitive(sp))
              : new JsonPrimitive(sp);
    } else if (p instanceof Boolean) {
      result = new JsonPrimitive((Boolean) p);
    } else if (p instanceof Character) {
      result = new JsonPrimitive((Character) p);
    } else if (p instanceof JsonElement) {
      result = (JsonElement) p;
    }
    return result;
  }

  /**
   * Gets the replete field.
   *
   * @param field the field
   * @param path the path
   * @param nextPath the next path
   * @return the replete field
   */
  protected JsonElement getRepleteField(JsonElement field, JsonElement path, JsonElement nextPath) {
    if (isNumber(path)) {
      Integer index = path.getAsInt();
      if (!isArray(field)) {
        field = new JsonArray();
      }
      JsonArray fArray =
          repleteArray(
              field.getAsJsonArray(),
              index,
              isNumber(nextPath) ? JsonArray.class : JsonObject.class);
      field = fArray;
    } else {
      String fieldName = path.getAsString();
      if (!isObject(field)) {
        field = new JsonObject();
      }
      JsonObject fJson =
          repleteJson(
              field.getAsJsonObject(),
              fieldName,
              isNumber(nextPath) ? JsonArray.class : JsonObject.class);
      field = fJson;
    }
    return field;
  }

  /**
   * Replete array.
   *
   * @param source the source
   * @param index the index
   * @param type the type
   * @return the json array
   */
  private JsonArray repleteArray(
      JsonArray source, Integer index, Class<? extends JsonElement> type) {
    source = isNull(source) ? new JsonArray() : source;
    for (int i = 0; i <= index; i++) {
      try {
        source.get(i);
      } catch (IndexOutOfBoundsException e) {
        Class<? extends JsonElement> newType = JsonNull.class;
        if (i == index) {
          newType = type;
        }
        source.add(getNewElement(newType));
      }
    }
    if (!source.get(index).getClass().equals(type)) {
      log.warn(
          String.format(
              "The element at index %s in the source %s, does not match %s. Creating a new element.",
              index, source, type));
      source.add(getNewElement(type));
    }
    return source;
  }

  /**
   * Replete json.
   *
   * @param source the source
   * @param fieldName the field name
   * @param type the type
   * @return the json object
   */
  private JsonObject repleteJson(
      JsonObject source, String fieldName, Class<? extends JsonElement> type) {
    source = isNull(source) ? new JsonObject() : source;
    JsonElement field = source.get(fieldName);
    if (isNull(field) || !field.getClass().equals(type)) {
      source.add(fieldName, getNewElement(type));
    }
    return source;
  }

  /**
   * Jsonify.
   *
   * @param source the source
   * @return the json element
   */
  public JsonElement jsonify(Object source) {
    JsonElement result = JsonNull.INSTANCE;
    if (source instanceof JsonElement) {
      result = (JsonElement) source;
    } else {
      try {
        result = GsonConvertor.getInstance().deserialize(source, JsonElement.class);

      } catch (ConversionException e) {
        String msg = String.format("Could not deserialise object %s to json.", source);
        log.error(msg);
        log.debug(msg, e);
      }
    }
    return result;
  }

  /**
   * Gets the new element.
   *
   * @param type the type
   * @return the new element
   */
  private JsonElement getNewElement(Class<? extends JsonElement> type) {
    JsonElement element = JsonNull.INSTANCE;
    if (JsonObject.class.equals(type)) {
      element = new JsonObject();
    } else if (JsonArray.class.equals(type)) {
      element = new JsonArray();
    } else {
      try {
        element = type.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        log.error(String.format("Could not instantiate json element of type %s", type));
        log.debug(String.format("Could not instantiate json element of type %s.", type), e);
      }
    }
    return element;
  }
}
