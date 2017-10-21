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
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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
@Slf4j
public class JsonLever {

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
    jsonParser = new JsonParser();
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat(DateFormat.LONG, DateFormat.LONG);
    gson = gsonBuilder.create();
  }

  /**
   * Gets the json element in the specified jpath.
   *
   * @param source the source
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
   * Gets the json element in the specified jpath oe returns a default value.
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
   * Gets the json element in the specified jpath.
   *
   * @param source the source
   * @param jpath the fully qualified json path to the field required. eg get({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a", "b" "c", "1"]) is '2'. Strings that can be casted to numerals are
   *     presumed to be array indexes
   * @return the json element returns 'Null' for any invalid path return the source if the input
   *     jpath is '.'
   */
  public JsonElement get(JsonElement source, String... jpath) {
    return get(source, newJsonArray(jpath));
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
   * @param source the source
   * @param jpath the jpath
   * @return the json element
   */
  public JsonElement get(JsonElement source, Object... jpath) {
    return get(source, newJsonArray(jpath));
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
  public Boolean isJsonArray(JsonElement source) {
    return isNotNull(source) && source.isJsonArray();
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
   * Checks if is json primitive.
   *
   * @param source the source
   * @return the boolean
   */
  public Boolean isJsonPrimitive(JsonElement source) {
    return isNotNull(source) && source.isJsonPrimitive();
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
   * Checks if is json object.
   *
   * @param source the source
   * @return the boolean
   */
  public Boolean isJsonObject(JsonElement source) {
    return isNotNull(source) && source.isJsonObject();
  }

  /**
   * New json array.
   *
   * @param str the str
   * @return the json array
   */
  public JsonArray newJsonArray(String... str) {
    JsonArray output = new JsonArray();
    for (String string : str) {
      output.add(new JsonPrimitive(StringUtils.trim(string)));
    }
    return output;
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
      output.add(getTypedPath(p));
    }
    return output;
  }

  /**
   * Returns the source json as a json primitive if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as a json primitive
   */
  public JsonPrimitive asJsonPrimitive(JsonElement source) {
    return isJsonPrimitive(source) ? source.getAsJsonPrimitive() : null;
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
   * Returns the source json as a json object if possible. Else returns the default json
   *
   * @param source the source json element
   * @param defaultJson the default json
   * @return the source json as a json object
   */
  public JsonObject asJsonObject(JsonElement source, JsonObject defaultJson) {
    return isJsonObject(source) ? source.getAsJsonObject() : defaultJson;
  }

  /**
   * Returns the field identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element
   * @param jPath '.' separated string defining the fully qualified json path to the field required.
   *     eg get({'a': {'b': {'c': [1, 2, 3, 4]}}}, 'a.b.c.1') is '2'
   * @return the field of interest as json object
   */
  public JsonObject getJsonObject(JsonElement source, String jPath) {
    JsonElement field = get(source, getJpath(jPath));
    return asJsonObject(field);
  }

  /**
   * Returns the field identified by the fully qualified json path as a json array if possible. Else
   * returns null
   *
   * @param source the json source element
   * @param jPath the string array defining the full qualified json path to the field required
   * @return the field of interest as json array
   */
  public JsonArray getJsonArray(JsonElement source, String jPath) {
    JsonElement field = get(source, getJpath(jPath));
    return asJsonArray(field);
  }

  /**
   * Returns the field identified by the fully qualified json path as a json string if possible.
   * Else returns null
   *
   * @param source the json source element
   * @param jPath the string array defining the full qualified json path to the field required
   * @return the field of interest as string
   */
  public String getString(JsonElement source, String jPath) {
    JsonElement field = get(source, getJpath(jPath));
    return asString(field);
  }

  /**
   * Returns the field identified by the fully qualified json path as a json object if possible.
   * Else returns null
   *
   * @param source the json source element
   * @param jPath the string array defining the full qualified json path to the field required
   * @return the field of interest as long
   */
  public Long getLong(JsonElement source, String jPath) {
    JsonElement field = get(source, getJpath(jPath));
    return asLong(field);
  }

  /**
   * Returns the field identified by the fully qualified json path as a boolean if possible. Else
   * returns null
   *
   * @param source the json source element
   * @param jPath the string array defining the full qualified json path to the field required
   * @return the field of interest as boolean
   */
  public Boolean getBoolean(JsonElement source, String jPath) {
    JsonElement field = get(source, getJpath(jPath));
    return asBoolean(field);
  }

  /**
   * Returns the field identified by the fully qualified json path as a json primitive if possible.
   * Else returns null
   *
   * @param source the json source element
   * @param jPath the string array defining the full qualified json path to the field required
   * @return the field of interest as json primitive
   */
  public JsonPrimitive getJsonPrimitive(JsonElement source, String jPath) {
    JsonElement field = get(source, getJpath(jPath));
    return asJsonPrimitive(field);
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
    return isJsonArray(source) ? source.getAsJsonArray() : defaultArray;
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
   * Returns the source json as a string if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as a string
   */
  public String asString(JsonElement source) {
    return asString(source, (String) null);
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
   * Returns the source json as a boolean if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as a boolean
   */
  public Boolean asBoolean(JsonElement source) {
    return asBoolean(source, (Boolean) null);
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
   * Returns the source json as a long if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as a long
   */
  public Long asLong(JsonElement source) {
    return asLong(source, (Long) null);
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
   * Returns the source json as an integer if possible. Else returns null
   *
   * @param source the source json element
   * @return the source json as an integer
   */
  public Integer asInt(JsonElement source) {
    return asInt(source, (Integer) null);
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
   * Gets the first json element, if any, in the list. Else returns null
   *
   * @param elements the json elements
   * @return the first json element in the list
   */
  public JsonElement getFirst(List<JsonElement> elements) {
    JsonElement first = null;
    if (CollectionUtils.isNotEmpty(elements)) {
      first = elements.get(0);
    }
    return first;
  }

  /**
   * Gets the first json element, if any, in the json array. Else returns null
   *
   * @param jsonArray the json array
   * @return the first json element in the json array
   */
  public JsonElement getFirst(JsonArray jsonArray) {
    JsonElement first = null;
    first = isNotNull(jsonArray) && jsonArray.size() > 0 ? jsonArray.get(0) : first;
    return first;
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
        if (isJsonArray(jsonElement)) {
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
   * Gets the typed path.
   *
   * @param p the p
   * @return the typed path
   */
  private JsonElement getTypedPath(Object p) {
    JsonElement result = JsonNull.INSTANCE;
    if (p instanceof Number) {
      result = new JsonPrimitive((Number) p);
    } else if (p instanceof String) {
      String sp = (String) p;
      result =
          NumberUtils.isCreatable(sp)
              ? new JsonPrimitive(NumberUtils.toInt(sp))
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
      if (!isJsonArray(field)) {
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
      if (!isJsonObject(field)) {
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

  /**
   * Gets the jpath.
   *
   * @param jpath the jpath
   * @return the jpath
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
   * Gets the jpath.
   *
   * @param jpath the jpath
   * @return the j path
   */
  public JsonArray getJPath(JsonElement jpath) {
    JsonArray path = null;
    if (isJsonArray(jpath)) {
      path = asJsonArray(jpath);
    } else if (isString(jpath)) {
      path = getJpath(asString(jpath));
    }
    return path;
  }
}
