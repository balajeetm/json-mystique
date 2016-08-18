/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 7 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class GetFromDepsMystTurn.
 *
 * @author balajmoh
 */
@Component
public class GetFromDepsMystTurn extends AbstractMystTurn {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.AbstractMystique#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject)
	 */
	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {

		JsonElement transform = JsonNull.INSTANCE;
		JsonElement elementSource = jsonLever.getFirst(source);
		if (null != elementSource) {
			//element source is key

			turn = jsonLever.getAsJsonObject(turn, new JsonObject());
			JsonElement granularSource = getGranularSource(elementSource, turn, aces);
			String reference = jsonLever.getAsString(granularSource, null);

			JsonArray keyPath = jsonLever.getAsJsonArray(turn.get(MysCon.KEY), new JsonArray());
			JsonElement value = turn.get(MysCon.VALUE);
			value = jsonLever.isNull(value) ? new JsonArray() : value;

			//keymap
			JsonObject keyMap = jsonLever.getAsJsonObject(jsonLever.getField(deps, keyPath, aces), new JsonObject());

			JsonElement actualElement = keyMap.get(reference);

			transform = jsonLever.getSubset(actualElement, deps, aces, value);
		}
		return transform;
	}

}