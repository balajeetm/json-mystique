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

/**
 * The Class MystiqueMystTurn.
 *
 * @author balajeetm
 */
public class MystiqueMystTurn extends AbstractMystTurn {

  /** The json genie. */
  private JsonMystique jsonMystique;

  /* (non-Javadoc)
   * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
   */
  @Override
  public JsonElement transmute(
      List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
    JsonElement transform = null;
    JsonElement elementSource = mystiqueLever.getFirst(source);
    if (null != elementSource) {
      turn = mystiqueLever.asJsonObject(turn, new JsonObject());
      String value = mystiqueLever.asString(turn.get(MystiqueConstants.VALUE));
      if (null != value) {
        JsonElement granularSource = getGranularSource(elementSource, turn, deps, aces);
        transform = jsonMystique().transform(granularSource, value, deps);
      }
    }
    return transform;
  }

  /**
   * Json mystique.
   *
   * @return the json mystique
   */
  private JsonMystique jsonMystique() {
    if (jsonMystique == null) {
      jsonMystique = JsonMystique.getInstance();
    }
    return jsonMystique;
  }
}
