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

import org.apache.commons.lang3.StringUtils;

import com.balajeetm.mystique.core.util.MystiqueConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class StringUtilsMystTurn.
 *
 * @author balajeetm
 */
public class StringUtilsMystTurn extends AbstractMystTurn {

  /* (non-Javadoc)
   * @see com.futuresight.util.mystique.AbstractMystTurn#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject, com.google.gson.JsonObject)
   */
  @Override
  protected JsonElement transmute(
      List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
    JsonElement elementSource = mystiqueLever.getFirst(source);
    turn = mystiqueLever.asJsonObject(turn, new JsonObject());
    JsonElement granularSource = getGranularSource(elementSource, turn, deps, aces);
    String sourceString = mystiqueLever.asString(granularSource, MystiqueConstants.EMPTY);
    return execute(sourceString, turn);
  }

  /**
   * Execute.
   *
   * @param input the input
   * @param turn the turn
   * @return the json primitive
   */
  private JsonPrimitive execute(String input, JsonObject turn) {
    JsonPrimitive transform = null;
    String action =
        mystiqueLever.asString(turn.get(MystiqueConstants.ACTION), MystiqueConstants.TRIM);
    switch (action) {
      case MystiqueConstants.TRIM:
        transform = new JsonPrimitive(StringUtils.trim(input));
        break;

      case MystiqueConstants.TRIM_TO_EMPTY:
        transform = new JsonPrimitive(StringUtils.trimToEmpty(input));
        break;

      case MystiqueConstants.SUBSTRING_AFTER_LAST:
        String separator =
            mystiqueLever.asString(turn.get(MystiqueConstants.SEPARATOR), MystiqueConstants.EMPTY);
        transform = new JsonPrimitive(StringUtils.substringAfterLast(input, separator));
        break;

      default:
        break;
    }
    return transform;
  }
}
