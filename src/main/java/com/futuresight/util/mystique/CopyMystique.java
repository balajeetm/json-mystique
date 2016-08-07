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
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class CopyMystique.
 *
 * @author balajeetm
 */
@Component
public class CopyMystique extends AbstractMystique {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		JsonElement transform = null;
		if (CollectionUtils.isNotEmpty(source)) {
			if (source.size() > 1) {
				transform = new JsonArray();
				for (JsonElement jsonElement : source) {
					JsonElement granularSource = getGranularSource(jsonElement, turn);
					transform.getAsJsonArray().add(granularSource);
				}
			}
			else {
				transform = getGranularSource(source.get(0), turn);
			}
		}
		return transform;
	}

}
