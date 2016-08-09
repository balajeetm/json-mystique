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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class MultiTurnMystique.
 *
 * @author balajmoh
 */
@Component
public class MultiTurnMystique implements Mystique {

	/** The factory. */
	@Autowired
	private MystiqueFactory factory;

	@Autowired
	private JsonLever jsonLever;

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonElement, com.google.gson.JsonElement)
	 */
	@Override
	public JsonElement transform(List<JsonElement> source, JsonObject deps, JsonElement turn, JsonObject resultWrapper) {
		JsonArray turnArray = jsonLever.isNotNull(turn) && turn.isJsonArray() ? turn.getAsJsonArray()
				: new JsonArray();
		JsonElement transform = JsonNull.INSTANCE;
		for (JsonElement turnObject : turnArray) {
			Mystique mystique = factory.getMystique(turnObject);
			if (null != mystique) {
				transform = mystique.transform(source, deps, turnObject, resultWrapper);
			}
			if (jsonLever.isNotNull(transform)) {
				break;
			}
		}
		return transform;
	}

}
