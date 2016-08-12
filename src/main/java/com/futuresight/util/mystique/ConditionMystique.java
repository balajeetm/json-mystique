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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class ConditionMystique.
 *
 * @author balajmoh
 */
@Component
public class ConditionMystique extends AbstractMystique {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.AbstractMystique#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject)
	 */
	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		JsonElement transform = null;
		JsonElement elementSource = null;
		if (CollectionUtils.isNotEmpty(source)) {
			//key1
			elementSource = source.get(0);
		}
		turn = jsonLever.isNull(turn) ? new JsonObject() : turn;
		JsonElement value = turn.get("value");
		JsonElement granularSource = getGranularSource(elementSource, turn);
		Boolean equals = isEquals(granularSource, value);
		JsonElement jsonElement = turn.get(String.valueOf(equals));
		if (jsonLever.isNull(jsonElement)) {
			transform = new JsonPrimitive(equals);
		}
		else {
			transform = transformOnCondition(jsonElement, source, deps);
		}
		return transform;
	}

	private Boolean isEquals(JsonElement source, JsonElement expected) {
		Boolean isEqual = Boolean.FALSE;
		if (null == expected) {
			if (jsonLever.isNotNull(source)) {
				isEqual = Boolean.TRUE;
			}
		}
		else {
			if (jsonLever.isNull(source) && expected.isJsonNull()) {
				isEqual = Boolean.TRUE;
			}
			isEqual = expected.equals(source);
		}
		return isEqual;
	}
}
