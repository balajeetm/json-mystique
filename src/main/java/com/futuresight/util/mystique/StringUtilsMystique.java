/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 7 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonElement;
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
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
		JsonElement elementSource = jsonLever.getFirst(source);
		turn = jsonLever.getAsJsonObject(turn, new JsonObject());
		JsonElement granularSource = getGranularSource(elementSource, turn);
		String sourceString = jsonLever.getAsString(granularSource, MysCon.EMPTY);
		return execute(sourceString, turn);
	}

	private JsonPrimitive execute(String input, JsonObject turn) {
		JsonPrimitive transform = null;
		String action = jsonLever.getAsString(turn.get(MysCon.ACTION), MysCon.TRIM);
		switch (action) {
		case MysCon.TRIM:
			transform = new JsonPrimitive(StringUtils.trim(input));
			break;

		case MysCon.TRIM_TO_EMPTY:
			transform = new JsonPrimitive(StringUtils.trimToEmpty(input));
			break;

		case MysCon.SUBSTRING_AFTER_LAST:
			String separator = jsonLever.getAsString(turn.get(MysCon.SEPARATOR), MysCon.EMPTY);
			transform = new JsonPrimitive(StringUtils.substringAfterLast(input, separator));
			break;

		default:
			break;
		}
		return transform;
	}
}
