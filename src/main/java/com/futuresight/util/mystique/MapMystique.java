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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.JsonJacksonConvertor;
import com.google.common.collect.Lists;
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
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		JsonObject mapJson = new JsonObject();
		if (CollectionUtils.isNotEmpty(source)) {
			JsonElement elementSource = source.get(0);
			turn = jsonLever.isNotNull(turn) ? turn : new JsonObject();
			JsonElement granularSource = getGranularSource(elementSource, turn);
			JsonArray jsonArray = jsonLever.isNotNull(granularSource) && granularSource.isJsonArray() ? granularSource
					.getAsJsonArray() : new JsonArray();

			JsonElement keyObject = turn.get("key");
			if (jsonLever.isNotNull(keyObject) && keyObject.isJsonArray()) {
				JsonElement valueObject = turn.get("value");
				valueObject = jsonLever.isNull(valueObject) ? new JsonArray() : valueObject;
				for (JsonElement jsonElement : jsonArray) {
					JsonElement keyField = jsonLever.getField(jsonElement, keyObject.getAsJsonArray());
					String key = (jsonLever.isNotNull(keyField) && keyField.isJsonPrimitive()) ? StringUtils
							.trimToEmpty(keyField.getAsString()) : null;

					JsonElement finalValue = null;

					if (valueObject.isJsonArray()) {
						JsonArray valueArray = valueObject.getAsJsonArray();
						if (valueArray.size() == 0) {
							finalValue = jsonLever.getField(jsonElement, valueArray);
						}
						else {
							for (JsonElement valuePath : valueArray) {
								if (valuePath.isJsonArray()) {
									finalValue = new JsonObject();
									for (JsonElement path : valueArray) {
										JsonArray pathArray = path.getAsJsonArray();
										JsonElement subset = jsonLever.getField(jsonElement, pathArray);
										jsonLever.setField(finalValue.getAsJsonObject(), pathArray, subset);
									}
									finalValue = finalValue.getAsJsonObject().get("result");
								}
								else {
									finalValue = jsonLever.getField(jsonElement, valueArray);
									break;
								}
							}
						}
					}
					else {
						// This is a turn
						Mystique mystique = factory.getMystique(valueObject);
						finalValue = mystique.transform(Lists.newArrayList(jsonElement), deps, valueObject,
								new JsonObject());
					}
					mapJson.add(key, finalValue);
				}
			}
		}
		return mapJson;
	}
}
