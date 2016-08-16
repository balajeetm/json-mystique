/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 7 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.JsonJacksonConvertor;
import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class MapMystique.
 *
 * @author balajmoh
 */
@Component
public class MapMystique extends AbstractMystique {

	/** The convertor. */
	@Autowired
	private JsonJacksonConvertor convertor;

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.AbstractMystique#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject)
	 */
	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
		JsonObject mapJson = new JsonObject();
		JsonElement elementSource = jsonLever.getFirst(source);

		if (null != elementSource) {
			turn = jsonLever.getAsJsonObject(turn, new JsonObject());
			JsonElement granularSource = getGranularSource(elementSource, turn);
			JsonArray inputArray = jsonLever.getAsJsonArray(granularSource, new JsonArray());
			JsonArray keyArray = jsonLever.getAsJsonArray(turn.get(MysCon.KEY));
			if (jsonLever.isNotNull(keyArray)) {
				JsonElement valueElement = turn.get(MysCon.VALUE);
				valueElement = jsonLever.isNull(valueElement) ? new JsonArray() : valueElement;

				for (JsonElement jsonElement : inputArray) {
					JsonElement keyField = jsonLever.getField(jsonElement, keyArray);
					String key = jsonLever.getAsString(keyField, MysCon.EMPTY);

					JsonElement finalValue = jsonLever.getSubset(jsonElement, deps, aces, valueElement);
					mapJson.add(key, finalValue);
				}
			}
		}

		return mapJson;
	}
}
