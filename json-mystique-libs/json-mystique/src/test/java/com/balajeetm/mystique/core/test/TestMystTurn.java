/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core.test;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.balajeetm.mystique.core.MystTurn;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class TestMystique.
 *
 * @author balajeetm
 */
public class TestMystTurn implements MystTurn {

  /* (non-Javadoc)
   * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
   */
  @Override
  public JsonElement transform(
      List<JsonElement> source,
      JsonObject deps,
      JsonObject aces,
      JsonObject turn,
      JsonObject resultWrapper) {
    JsonElement transform = null;
    if (CollectionUtils.isNotEmpty(source)) {
      JsonElement jsonElement = source.get(0);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder
          .append(StringUtils.strip(jsonElement.toString(), "\""))
          .append(deps.get("bala").getAsString());
      transform = new JsonPrimitive(stringBuilder.toString());
    }
    return transform;
  }
}
