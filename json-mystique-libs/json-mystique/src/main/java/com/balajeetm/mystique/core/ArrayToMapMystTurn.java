/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core;

import java.util.List;

import com.balajeetm.mystique.core.util.MystiqueConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class ArrayToMapMystTurn.
 *
 * @author balajeetm
 */
public class ArrayToMapMystTurn extends AbstractMystTurn {

  /* (non-Javadoc)
   * @see com.futuresight.util.mystique.AbstractMystique#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject)
   */
  @Override
  protected JsonElement transmute(
      List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
    JsonObject mapJson = new JsonObject();
    JsonElement elementSource = mystiqueLever.getFirst(source);

    if (null != elementSource) {
      turn = mystiqueLever.asJsonObject(turn, new JsonObject());
      JsonElement granularSource = getGranularSource(elementSource, turn, deps, aces);
      JsonArray inputArray = mystiqueLever.asJsonArray(granularSource, new JsonArray());
      JsonArray keyArray = mystiqueLever.getJPath(turn.get(MystiqueConstants.KEY));
      if (mystiqueLever.isNotNull(keyArray)) {
        JsonElement valueElement = turn.get(MystiqueConstants.VALUE);
        valueElement = mystiqueLever.isNull(valueElement) ? new JsonArray() : valueElement;

        for (JsonElement jsonElement : inputArray) {
          JsonElement keyField = mystiqueLever.getField(jsonElement, keyArray, deps, aces);
          String key = mystiqueLever.asString(keyField, MystiqueConstants.EMPTY);

          JsonElement finalValue = mystiqueLever.subset(jsonElement, deps, aces, valueElement);
          mapJson.add(key, finalValue);
        }
      }
    }

    return mapJson;
  }
}
