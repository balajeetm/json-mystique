/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.utl.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * The Class ConcatMystique.
 *
 * @author balajeetm
 */
@Component
public class ConcatMystique implements Mystique {

	/* (non-Javadoc)
	 * @see com.futuresight.utl.mystique.Mystique#transform(java.util.List)
	 */
	@Override
	public JsonElement transform(List<JsonElement> from) {
		// Need not be thread safe. Only one thread access at a time
		StringBuilder stringBuilder = new StringBuilder();
		if (CollectionUtils.isNotEmpty(from)) {
			for (JsonElement jsonElement : from) {
				stringBuilder.append(StringUtils.strip(jsonElement.toString(), "\""));
			}
		}
		return new JsonPrimitive(stringBuilder.toString());
	}
}
