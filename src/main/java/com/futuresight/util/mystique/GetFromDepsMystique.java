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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class GetFromDepsMystique.
 *
 * @author balajmoh
 */
@Component
public class GetFromDepsMystique extends AbstractMystique {

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
		if (CollectionUtils.isNotEmpty(source)) {
			//key1
			JsonElement elementSource = source.get(0);
			JsonElement granularSource = getGranularSource(elementSource, turn);

			JsonElement key = turn.get("key");
			JsonElement value = turn.get("value");
			JsonArray keyPath = null == key ? new JsonArray() : key.getAsJsonArray();
			JsonArray valuePath = null == value ? new JsonArray() : value.getAsJsonArray();

			//keymap
			JsonElement field = jsonLever.getField(deps, keyPath);
			JsonObject map = (null == field) ? null : field.getAsJsonObject();

			JsonElement actualElement = map.get(granularSource.getAsString());
			transform = jsonLever.getField(actualElement, valuePath);
		}
		return transform;
	}
}
