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
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class ConditionMystique.
 *
 * @author balajmoh
 */
@Component
public class StringUtilsMystique extends AbstractMystique {

	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		JsonElement elementSource = JsonNull.INSTANCE;
		if (CollectionUtils.isNotEmpty(source)) {
			elementSource = source.get(0);
		}
		turn = jsonLever.isNull(turn) ? new JsonObject() : turn;
		JsonElement granularSource = getGranularSource(elementSource, turn);
		String sourceString = jsonLever.isNotNull(granularSource) && granularSource.isJsonPrimitive() ? granularSource
				.getAsString() : "";

		return execute(sourceString, turn);
	}

	private JsonPrimitive execute(String input, JsonObject turn) {
		JsonPrimitive transform = null;
		String action = jsonLever.getStringFromJson(turn.get("action"), "trim");
		switch (action) {
		case "trim":
			transform = new JsonPrimitive(StringUtils.trim(input));
			break;

		case "trimToEmpty":
			transform = new JsonPrimitive(StringUtils.trimToEmpty(input));
			break;

		case "substringAfterLast":
			String separator = jsonLever.getStringFromJson(turn.get("separator"), "");
			transform = new JsonPrimitive(StringUtils.substringAfterLast(input, separator));
			break;

		default:
			break;
		}
		return transform;
	}
}
