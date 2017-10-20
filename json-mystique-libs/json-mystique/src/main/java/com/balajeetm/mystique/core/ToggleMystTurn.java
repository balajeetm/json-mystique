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

import com.balajeetm.mystique.core.lever.MystiqueLever;
import com.balajeetm.mystique.core.util.MystiqueConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import lombok.Setter;

/**
 * The Class MultiTurnMystique.
 *
 * @author balajeetm
 */
public class ToggleMystTurn implements MystTurn {

  /** The factory. */

  /**
   * Sets the factory.
   *
   * @param factory the new factory
   */
  @Setter private MystiqueFactory factory;

  /** The json lever. */
  private MystiqueLever jsonLever;

  /** Instantiates a new toggle myst turn. */
  public ToggleMystTurn() {
    jsonLever = MystiqueLever.getInstance();
  }

  /* (non-Javadoc)
   * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonElement, com.google.gson.JsonElement)
   */
  @Override
  public JsonElement transform(
      List<JsonElement> source,
      JsonObject deps,
      JsonObject aces,
      JsonObject turn,
      JsonObject resultWrapper) {

    JsonElement transform = JsonNull.INSTANCE;
    turn = jsonLever.asJsonObject(turn, new JsonObject());
    JsonArray turnArray = jsonLever.asJsonArray(turn.get(MystiqueConstants.TURNS), new JsonArray());
    for (JsonElement turnObject : turnArray) {
      if (jsonLever.isJsonObject(turnObject)) {
        JsonObject asJsonObject = turnObject.getAsJsonObject();
        MystTurn mystique = factory.getMystTurn(asJsonObject);
        if (null != mystique) {
          transform = mystique.transform(source, deps, aces, asJsonObject, resultWrapper);
        }
        if (jsonLever.isNotNull(transform)) {
          break;
        }
      }
    }
    return transform;
  }
}
