/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The Class ConstantMystique.
 *
 * @author balajeetm
 */
@Component
public class ConstantMystique implements Mystique {

	/** The json parser. */
	private JsonParser jsonParser = new JsonParser();

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transform(List<JsonElement> source, JsonObject deps, String turn) {
		return jsonParser.parse(StringUtils.removeStartIgnoreCase(turn, "constant:"));
	}

}
