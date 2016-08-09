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
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class ConcatMystique.
 *
 * @author balajeetm
 */
@Component
public class ConcatMystique extends AbstractMystique {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		StringBuilder stringBuilder = new StringBuilder();
		if (CollectionUtils.isNotEmpty(source)) {
			JsonElement jsonSep = turn.get("separator");
			String separator = jsonLever.isNull(jsonSep) ? "" : StringUtils.trimToEmpty(jsonSep.getAsString());
			for (int count = 0; count < source.size(); count++) {
				JsonElement jsonElement = source.get(count);
				JsonElement granularSource = getGranularSource(jsonElement, turn);
				if (count != 0) {
					stringBuilder.append(separator);
				}
				stringBuilder.append(StringUtils.strip(granularSource.toString(), "\""));
			}
		}
		return new JsonPrimitive(stringBuilder.toString());
	}

}
