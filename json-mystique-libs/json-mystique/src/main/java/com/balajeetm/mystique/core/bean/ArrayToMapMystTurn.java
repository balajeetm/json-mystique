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
import com.google.gson.JsonObject;

/**
 * The Class ArrayToMapMystTurn.
 *
 * @author balajeetm
 */
@Component
public class ArrayToMapMystTurn extends AbstractMystTurn {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.AbstractMystique#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject)
	 */
	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
		JsonObject mapJson = new JsonObject();
		JsonElement elementSource = mystiqueLever.getFirst(source);

		if (null != elementSource) {
			turn = mystiqueLever.getAsJsonObject(turn, new JsonObject());
			JsonElement granularSource = getGranularSource(elementSource, turn, deps, aces);
			JsonArray inputArray = mystiqueLever.getAsJsonArray(granularSource, new JsonArray());
			JsonArray keyArray = mystiqueLever.getAsJsonArray(turn.get(MysCon.KEY));
			if (mystiqueLever.isNotNull(keyArray)) {
				JsonElement valueElement = turn.get(MysCon.VALUE);
				valueElement = mystiqueLever.isNull(valueElement) ? new JsonArray() : valueElement;

				for (JsonElement jsonElement : inputArray) {
					JsonElement keyField = mystiqueLever.getField(jsonElement, keyArray, deps, aces);
					String key = mystiqueLever.getAsString(keyField, MysCon.EMPTY);

					JsonElement finalValue = mystiqueLever.getSubset(jsonElement, deps, aces, valueElement);
					mapJson.add(key, finalValue);
				}
			}
		}

		return mapJson;
	}
}
