/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.balajeetm.mystique.core.bean;

import java.util.List;

import org.springframework.stereotype.Component;

import com.balajeetm.mystique.util.gson.bean.lever.MysCon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class GetFromDepsMystTurn.
 *
 * @author balajeetm
 */
@Component
public class GetFromDepsMystTurn extends AbstractMystTurn {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.AbstractMystique#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject)
	 */
	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {

		JsonElement transform = JsonNull.INSTANCE;
		JsonElement elementSource = mystiqueLever.getFirst(source);
		if (null != elementSource) {
			//element source is key

			turn = mystiqueLever.getAsJsonObject(turn, new JsonObject());
			JsonElement granularSource = getGranularSource(elementSource, turn, deps, aces);
			String reference = mystiqueLever.getAsString(granularSource, (String) null);

			JsonArray keyPath = mystiqueLever.getAsJsonArray(turn.get(MysCon.KEY), new JsonArray());
			JsonElement value = turn.get(MysCon.VALUE);
			value = mystiqueLever.isNull(value) ? new JsonArray() : value;

			//keymap - The source is deps
			JsonObject keyMap = mystiqueLever.getAsJsonObject(mystiqueLever.getField(deps, keyPath, deps, aces),
					new JsonObject());

			JsonElement actualElement = keyMap.get(reference);

			transform = mystiqueLever.getSubset(actualElement, deps, aces, value);
		}
		return transform;
	}

}
