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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.JsonJacksonConvertor;
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

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

	/** The convertor. */
	@Autowired
	private JsonJacksonConvertor convertor;

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
		JsonElement value = turn.get("value");
		JsonElement granularSource = getGranularSource(elementSource, turn);
		Boolean equals = isEquals(granularSource, value);
		JsonElement jsonElement = turn.get(String.valueOf(equals));
		if (null == jsonElement) {
			transform = new JsonPrimitive(equals);
		}
		else {
			transform = transformOnCondition(jsonElement, source, deps);
		}
		return transform;
	}

	/**
	 * Checks if is equals.
	 *
	 * @param var1 the var1
	 * @param var2 the var2
	 * @return the boolean
	 */
	private Boolean isEquals(JsonElement var1, JsonElement var2) {
		Boolean isEqual = Boolean.FALSE;

		if (var1 == null && var2 == null) {
			isEqual = Boolean.TRUE;
		}
		else if (var1 != null) {
			isEqual = var1.equals(var2);
		}

		return isEqual;
	}
}
