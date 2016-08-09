/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 7 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Interface SimpleTurnMystique.
 *
 * @author balajmoh
 */
public interface SimpleTurnMystique extends Mystique {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonElement, com.google.gson.JsonElement)
	 */
	@Override
	default JsonElement transform(List<JsonElement> source, JsonObject deps, JsonElement turn, JsonObject resultWrapper) {
		JsonObject turnObject = (null == turn || turn.isJsonNull()) ? null : turn.getAsJsonObject();
		return transform(source, deps, turnObject, resultWrapper);
	}

	/**
	 * Transform.
	 *
	 * @param source the source
	 * @param deps the deps
	 * @param turn the turn
	 * @param result the result
	 * @return the json element
	 */
	JsonElement transform(List<JsonElement> source, JsonObject deps, JsonObject turn, JsonObject resultWrapper);

}
