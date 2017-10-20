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

/**
 * The Class DateMystTurn.
 *
 * @author balajeetm
 */
public class DateMystTurn extends AbstractMystTurn {

  /* (non-Javadoc)
   * @see com.futuresight.util.mystique.AbstractMystTurn#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject, com.google.gson.JsonObject)
   */
  @Override
  protected JsonElement transmute(
      List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
    JsonElement elementSource = mystiqueLever.getFirst(source);
    turn = mystiqueLever.asJsonObject(turn, new JsonObject());
    JsonElement granularSource = getGranularSource(elementSource, turn, deps, aces);

    String action =
        StringUtils.trimToEmpty(
                mystiqueLever.asString(turn.get(MystiqueConstants.ACTION), MystiqueConstants.NOW))
            .toLowerCase();
    MystiqueFunction function = factory.getDateFunction(action);
    return function.execute(granularSource, turn);
  }
}
