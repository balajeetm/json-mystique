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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class AbstractMystique.
 *
 * @author balajmoh
 */
public abstract class AbstractMystique implements SimpleTurnMystique {

	/** The json lever. */
	@Autowired
	protected JsonLever jsonLever;

	/** The factory. */
	@Autowired
	protected MystiqueFactory factory;

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.SimpleTurnMystique#transform(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject, com.google.gson.JsonElement)
	 */
	public JsonElement transform(List<JsonElement> source, JsonObject deps, JsonObject turn, JsonObject resultWrapper) {
		JsonElement transform = transmute(source, deps, turn);
		Boolean isTurn = jsonLever.isNotNull(turn);
		if (jsonLever.isNull(transform) && isTurn) {
			JsonElement defaultJson = turn.get("default");
			transform = transformOnCondition(defaultJson, source, deps, resultWrapper);
		}

		//set the result
		JsonArray to = null;
		Boolean optional = Boolean.FALSE;

		if (isTurn) {
			JsonElement toJson = turn.get("to");
			to = jsonLever.isNull(toJson) ? null : toJson.getAsJsonArray();
			JsonElement optionalElement = turn.get("optional");
			if (null != optionalElement) {
				optional = optionalElement.getAsBoolean();
			}
		}

		jsonLever.setField(resultWrapper, to, transform, optional);
		return transform;
	}

	/**
	 * Transform on condition.
	 *
	 * @param conditionalJson the conditional json
	 * @param source the source
	 * @param deps the deps
	 * @param result the result
	 * @return the json element
	 */
	protected JsonElement transformOnCondition(JsonElement conditionalJson, List<JsonElement> source, JsonObject deps,
			JsonObject resultWrapper) {
		JsonElement transform = JsonNull.INSTANCE;
		if (jsonLever.isNotNull(conditionalJson)) {
			JsonObject defaultObj = conditionalJson.getAsJsonObject();
			//Should not be null, can be json null
			if (null != defaultObj.get("value")) {
				transform = defaultObj.get("value");
			}
			else {
				JsonElement defaultTurn = defaultObj.get("turn");
				if (jsonLever.isNotNull(defaultTurn)) {
					Mystique mystique = factory.getMystique(defaultTurn);
					transform = mystique.transform(source, deps, defaultTurn, resultWrapper);
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
	 * @return the json element
	 */
	protected JsonElement transformOnCondition(JsonElement conditionalJson, List<JsonElement> source, JsonObject deps) {
		// Do not update the eventual result
		return transformOnCondition(conditionalJson, source, deps, new JsonObject());
	}

	/**
	 * Transmute.
	 *
	 * @param source the source
	 * @param deps the deps
	 * @param turn the turn
	 * @return the json element
	 */
	protected abstract JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn);

	/**
	 * Gets the granular source.
	 *
	 * @param source the source
	 * @param turn the turn
	 * @return the granular source
	 */
	protected JsonElement getGranularSource(JsonElement source, JsonObject turn) {
		JsonElement from = jsonLever.isNotNull(turn) ? turn.get("from") : JsonNull.INSTANCE;
		JsonElement conditionSource = JsonNull.INSTANCE;
		if (jsonLever.isNull(from)) {
			conditionSource = source;
		}
		else {
			conditionSource = jsonLever.getField(source, from.getAsJsonArray());
		}

		return conditionSource;
	}
}
