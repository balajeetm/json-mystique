/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 7 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class GetFromDepsMystique.
 *
 * @author balajmoh
 */
@Component
public class GetFromDepsMystique extends AbstractMystique {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.AbstractMystique#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject)
	 */
	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {

		JsonElement transform = JsonNull.INSTANCE;
		if (CollectionUtils.isNotEmpty(source)) {
			//key1
			JsonElement elementSource = source.get(0);

			turn = jsonLever.isNull(turn) ? new JsonObject() : turn;
			JsonElement granularSource = getGranularSource(elementSource, turn);

			JsonElement key = turn.get("key");
			JsonElement value = turn.get("value");
			JsonArray keyPath = jsonLever.isNull(key) ? new JsonArray() : key.getAsJsonArray();
			JsonArray valuePath = jsonLever.isNull(value) ? new JsonArray() : value.getAsJsonArray();

			//keymap
			JsonElement field = jsonLever.getField(deps, keyPath);
			JsonObject map = jsonLever.isNull(field) ? new JsonObject() : field.getAsJsonObject();

			String asString = jsonLever.isNull(granularSource) ? null : granularSource.getAsString();
			JsonElement actualElement = map.get(asString);
			transform = jsonLever.getField(actualElement, valuePath);
		}
		return transform;
	}
}
