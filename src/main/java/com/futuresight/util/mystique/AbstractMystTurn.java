/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class AbstractMystTurn.
 *
 * @author balajeetm
 */
public abstract class AbstractMystTurn implements MystTurn {

	/** The json lever. */
	@Autowired
	protected JsonLever jsonLever;

	/** The factory. */
	@Autowired
	protected MystiqueFactory factory;

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.SimpleTurnMystique#transform(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject, com.google.gson.JsonElement)
	 */
	public JsonElement transform(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn,
			JsonObject resultWrapper) {

		Boolean turnExists = jsonLever.isNotNull(turn);
		JsonObject updatedAces = new JsonObject();
		if (turnExists) {
			JsonObject localAces = jsonLever.getAsJsonObject(turn.get(MysCon.ACES), null);
			if (jsonLever.isNotNull(localAces)) {
				// Aces will only work on first source
				JsonElement first = jsonLever.getFirst(source);
				updatedAces = jsonLever.getUpdatedAces(first, localAces, deps, updatedAces);
			}
		}

		jsonLever.simpleMerge(jsonLever.getAsJsonObject(aces, new JsonObject()), updatedAces);

		JsonElement transform = transmute(source, deps, updatedAces, turn);

		if (turnExists) {
			if (jsonLever.isNull(transform)) {
				JsonObject defaultJson = jsonLever.getAsJsonObject(turn.get(MysCon.DEFAULT));
				transform = transformToDefault(defaultJson, source, deps, updatedAces);
			}

			//set the result
			JsonArray to = jsonLever.getAsJsonArray(turn.get(MysCon.TO));
			Boolean optional = jsonLever.getAsBoolean(turn.get(MysCon.OPTIONAL), Boolean.FALSE);

			jsonLever.setField(resultWrapper, to, transform, updatedAces, optional);
		}

		return transform;
	}

	/**
	 * Transform on condition.
	 *
	 * @param conditionalJson the conditional json
	 * @param source the source
	 * @param deps the deps
	 * @param aces the aces
	 * @param resultWrapper the result wrapper
	 * @return the json element
	 */
	protected JsonElement transformToDefault(JsonObject conditionalJson, List<JsonElement> source, JsonObject deps,
			JsonObject aces, JsonObject resultWrapper) {
		JsonElement transform = JsonNull.INSTANCE;
		if (jsonLever.isNotNull(conditionalJson)) {
			//Should not be null, can be json null
			JsonElement value = conditionalJson.get(MysCon.VALUE);
			if (null != value) {
				transform = value;
			}
			else {
				JsonObject defaultTurn = jsonLever.getAsJsonObject(conditionalJson.get(MysCon.TURN));
				if (jsonLever.isNotNull(defaultTurn)) {
					MystTurn mystique = factory.getMystTurn(defaultTurn);
					transform = mystique.transform(source, deps, aces, defaultTurn, resultWrapper);
				}
			}
		}
		return transform;
	}

	/**
	 * Transform on condition.
	 *
	 * @param conditionalJson the conditional json
	 * @param source the source
	 * @param deps the deps
	 * @param aces the aces
	 * @return the json element
	 */
	protected JsonElement transformToDefault(JsonObject conditionalJson, List<JsonElement> source, JsonObject deps,
			JsonObject aces) {
		// Do not update the eventual result
		return transformToDefault(conditionalJson, source, deps, aces, new JsonObject());
	}

	/**
	 * Transmute.
	 *
	 * @param source the source
	 * @param deps the deps
	 * @param aces the aces
	 * @param turn the turn
	 * @return the json element
	 */
	protected abstract JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces,
			JsonObject turn);

	/**
	 * Gets the granular source.
	 *
	 * @param source the source
	 * @param turn the turn
	 * @param deps the deps
	 * @param aces the aces
	 * @return the granular source
	 */
	protected JsonElement getGranularSource(JsonElement source, JsonObject turn, JsonObject deps, JsonObject aces) {
		JsonArray from = jsonLever.isNotNull(turn) ? jsonLever.getAsJsonArray(turn.get(MysCon.FROM)) : null;
		JsonElement conditionSource = jsonLever.isNull(from) ? source : jsonLever.getField(source, from, deps, aces);
		return conditionSource;
	}
}
