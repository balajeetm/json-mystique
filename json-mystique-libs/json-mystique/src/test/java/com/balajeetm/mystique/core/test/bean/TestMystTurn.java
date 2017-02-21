/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core.test.bean;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.balajeetm.mystique.core.bean.MystTurn;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class TestMystique.
 *
 * @author balajeetm
 */
@Component
public class TestMystTurn implements MystTurn {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transform(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn,
			JsonObject resultWrapper) {
		JsonElement transform = null;
		if (CollectionUtils.isNotEmpty(source)) {
			JsonElement jsonElement = source.get(0);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(StringUtils.strip(jsonElement.toString(), "\""))
					.append(
							deps.get("bala")
									.getAsString());
			transform = new JsonPrimitive(stringBuilder.toString());
		}
		return transform;
	}

}
