/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	public JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		JsonElement transform = null;
		if (CollectionUtils.isNotEmpty(source)) {
			String myst = turn.get("value").getAsString();
			JsonElement jsonElement = source.get(0);
			JsonElement granularSource = getGranularSource(jsonElement, turn);
			transform = jsonGenie.transform(granularSource, myst, deps);
		}
		return transform;
	}

}
