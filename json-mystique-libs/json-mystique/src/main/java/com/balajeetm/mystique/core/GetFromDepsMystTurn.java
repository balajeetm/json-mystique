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
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class GetFromDepsMystTurn.
 *
 * @author balajeetm
 */
public class GetFromDepsMystTurn extends AbstractMystTurn {

  /* (non-Javadoc)
   * @see com.futuresight.util.mystique.AbstractMystique#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject)
   */
  @Override
  protected JsonElement transmute(
      List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {

    JsonElement transform = JsonNull.INSTANCE;
    JsonElement elementSource = mystiqueLever.getFirst(source);
    if (null != elementSource) {
      // element source is key

      turn = mystiqueLever.asJsonObject(turn, new JsonObject());
      JsonElement granularSource = getGranularSource(elementSource, turn, deps, aces);
      JsonArray reference = mystiqueLever.getJPath(granularSource);

      /* Default is needed since the key can turn out to be null */
      JsonArray keyPath =
          mystiqueLever.asJsonArray(
              mystiqueLever.getJPath(turn.get(MystiqueConstants.KEY)), new JsonArray());
      JsonElement value = turn.get(MystiqueConstants.VALUE);
      value = mystiqueLever.isNull(value) ? new JsonArray() : value;

      // keymap - The source is deps
      JsonElement keyMap = mystiqueLever.getField(deps, keyPath, deps, aces);

      JsonElement actualElement = mystiqueLever.get(keyMap, reference);

      transform = mystiqueLever.subset(actualElement, deps, aces, value);
    }
    return transform;
  }
}
