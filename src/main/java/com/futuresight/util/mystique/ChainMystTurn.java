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

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class ChainMystTurn.
 *
 * @author balajmoh
 */
@Component
public class ChainMystTurn implements MystTurn {

	/** The factory. */
	@Autowired
	private MystiqueFactory factory;

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonElement, com.google.gson.JsonElement)
	 */
	@Override
	public JsonElement transform(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn,
			JsonObject resultWrapper) {

		JsonElement transform = JsonNull.INSTANCE;
		turn = jsonLever.getAsJsonObject(turn, new JsonObject());
		JsonArray turnArray = jsonLever.getAsJsonArray(turn.get(MysCon.TURNS), new JsonArray());

		for (JsonElement turnObject : turnArray) {
			if (jsonLever.isJsonObject(turnObject)) {
				JsonObject asJsonObject = turnObject.getAsJsonObject();
				MystTurn mystique = factory.getMystTurn(asJsonObject);
				if (null != mystique) {
					mystique.transform(source, deps, aces, asJsonObject, resultWrapper);
				}
			}
		}
		return transform;
	}

}
