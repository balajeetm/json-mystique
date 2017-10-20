/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.lever;

import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/** The Class JsonComparator. */
/** @author balajeetm */
public class JsonComparator {

  /** The json lever. */
  private JsonLever jsonLever;

  /**
   * Gets the single instance of JsonComparator.
   *
   * @return single instance of JsonComparator
   */
  public static JsonComparator getInstance() {
    return Creator.INSTANCE;
  }

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized
  // Works non spring as well
  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    public static JsonComparator INSTANCE = new JsonComparator();
  }

  /** Instantiates a new json comparator. */
  private JsonComparator() {
    jsonLever = JsonLever.getInstance();
  }

  /**
   * Checks if is subset.
   *
   * @param subsetStr the subset str
   * @param actualStr the actual str
   * @return the myst result
   */
  public Comparison isSubset(String subsetStr, String actualStr) {
    Comparison result = new Comparison();
    JsonParser jsonParser = jsonLever.getJsonParser();
    try {
      JsonElement actual = jsonParser.parse(actualStr);
      JsonElement subset = jsonParser.parse(subsetStr);
      isSubset("root", subset, actual, result);
    } catch (RuntimeException e) {
      result.setResult(Boolean.FALSE);
      result.addMsg(String.format("Exception % s occured", e.getMessage()));
    }
    return result;
  }

  /**
   * Checks if is subset.
   *
   * @param subset the subset
   * @param actual the actual
   * @return the myst result
   */
  public Comparison isSubset(JsonElement subset, JsonElement actual) {
    Comparison result = new Comparison();
    try {
      isSubset("root", subset, actual, result);
    } catch (RuntimeException e) {
      result.setResult(Boolean.FALSE);
      result.addMsg(String.format("Exception % s occured", e.getMessage()));
    }
    return result;
  }

  /**
   * Checks if is subset.
   *
   * @param tag the tag
   * @param subset the subset
   * @param actual the actual
   * @param result the result
   * @return the myst result
   */
  private Comparison isSubset(
      String tag, JsonElement subset, JsonElement actual, Comparison result) {
    subset = jsonLever.asJsonElement(subset, JsonNull.INSTANCE);
    actual = jsonLever.asJsonElement(actual, JsonNull.INSTANCE);
    if (jsonLever.isNotNull(subset) && jsonLever.isNull(actual)) {
      result.setResult(Boolean.FALSE);
      result.addMsg(String.format("The field %s of actual is null", tag));
    } else if (!subset.getClass().getCanonicalName().equals(actual.getClass().getCanonicalName())) {
      result.setResult(Boolean.FALSE);
      result.addMsg(
          String.format("The field %s of expected and actual are not of the same type", tag));
    } else {
      if (subset.isJsonObject()) {
        JsonObject subJson = jsonLever.asJsonObject(subset);
        JsonObject actJson = jsonLever.asJsonObject(actual);
        Set<Entry<String, JsonElement>> entrySet = subJson.entrySet();
        for (Entry<String, JsonElement> entry : entrySet) {
          String key = entry.getKey();
          JsonElement value = entry.getValue();
          JsonElement actualValue = actJson.get(key);
          isSubset(key, value, actualValue, result);
        }
      } else if (subset.isJsonArray()) {
        JsonArray subJson = jsonLever.asJsonArray(subset);
        JsonArray actJson = jsonLever.asJsonArray(actual);
        if (subJson.size() != actJson.size()) {
          result.setResult(Boolean.FALSE);
          result.addMsg(
              String.format("The field %s of expected and actual are not of same size", tag));

        } else {
          for (int i = 0; i < subJson.size(); i++) {
            isSubset(tag, subJson.get(i), actJson.get(i), result);
          }
        }

      } else {
        if (!subset.equals(actual)) {
          result.setResult(Boolean.FALSE);
          result.addMsg(String.format("The field %s of expected and actual are not same", tag));
        }
      }
    }

    return result;
  }
}
