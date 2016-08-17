/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class JsonMystique.
 *
 * @author balajeetm
 */
@Component
public class JsonMystique extends AbstractMystique {

	/** The json genie. */
	@Autowired
	private JsonGenie jsonGenie;

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
		JsonElement transform = null;
		JsonElement elementSource = jsonLever.getFirst(source);
		if (null != elementSource) {
			turn = jsonLever.getAsJsonObject(turn, new JsonObject());
			String value = jsonLever.getAsString(turn.get(MysCon.VALUE));
			if (null != value) {
				JsonElement granularSource = getGranularSource(elementSource, turn, aces);
				transform = jsonGenie.transform(granularSource, value, deps);
			}
		}
		return transform;
	}
}
