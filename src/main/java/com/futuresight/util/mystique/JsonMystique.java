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
import org.apache.commons.lang.StringUtils;
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
public class JsonMystique implements Mystique {

	/** The json genie. */
	@Autowired
	private JsonGenie jsonGenie;

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transform(List<JsonElement> source, JsonObject deps, String turn) {
		JsonElement transform = null;
		if (CollectionUtils.isNotEmpty(source)) {
			JsonElement jsonElement = source.get(0);
			transform = jsonGenie.transform(jsonElement, StringUtils.removeStartIgnoreCase(turn, "mys:"));
		}
		return transform;
	}

}
