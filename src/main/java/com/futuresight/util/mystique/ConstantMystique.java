/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class ConstantMystique.
 *
 * @author balajeetm
 */
@Component
public class ConstantMystique extends AbstractMystique {

	/** The json parser. */
	private Gson gson = new Gson();

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		Object object = turn.get("value");
		return gson.toJsonTree(object);
	}

}
