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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class ConditionMystTurn.
 *
 * @author balajeetm
 */
public class ConditionMystTurn extends AbstractMystTurn {

  /* (non-Javadoc)
   * @see com.futuresight.util.mystique.AbstractMystique#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject)
   */
  @Override
  protected JsonElement transmute(
      List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
    JsonElement transform = null;
    JsonElement elementSource = mystiqueLever.getFirst(source);
    turn = mystiqueLever.asJsonObject(turn, new JsonObject());
    JsonElement granularSource = getGranularSource(elementSource, turn, deps, aces);
    JsonElement value = turn.get(MystiqueConstants.VALUE);
    Boolean equals = isEquals(granularSource, value);
    JsonObject defaultJson = mystiqueLever.asJsonObject(turn.get(String.valueOf(equals)));
    transform =
        mystiqueLever.isNull(defaultJson)
            ? new JsonPrimitive(equals)
            : transformToDefault(defaultJson, source, deps, aces);
    return transform;
  }

  /**
   * Checks if is equals.
   *
   * @param source the source
   * @param expected the expected
   * @return the boolean
   */
  private Boolean isEquals(JsonElement source, JsonElement expected) {
    Boolean isEqual = Boolean.FALSE;
    // This is to check the existence case. The condition is to check the
    // existence of the element
    if (null == expected) {
      if (mystiqueLever.isNotNull(source)) {
        isEqual = Boolean.TRUE;
      }
    } else {
      if (mystiqueLever.isNull(source) && expected.isJsonNull()) {
        isEqual = Boolean.TRUE;
      }
      isEqual = expected.equals(source);
    }
    return isEqual;
  }
}
