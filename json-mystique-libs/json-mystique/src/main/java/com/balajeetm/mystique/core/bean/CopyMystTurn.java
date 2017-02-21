/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core.bean;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.balajeetm.mystique.util.gson.bean.lever.MysCon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class CopyMystTurn.
 *
 * @author balajeetm
 */
@Component
public class CopyMystTurn extends AbstractMystTurn {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
		JsonElement transform = null;
		if (CollectionUtils.isNotEmpty(source)) {
			if (source.size() > 1) {
				transform = new JsonArray();
				for (JsonElement jsonElement : source) {
					JsonElement granularSource = getGranularSource(jsonElement, turn, deps, aces);
					granularSource = getSubset(granularSource, deps, aces, turn);
					transform.getAsJsonArray()
							.add(granularSource);
				}
			} else {
				JsonElement granularSource = getGranularSource(source.get(0), turn, deps, aces);
				transform = getSubset(granularSource, deps, aces, turn);
			}
		}
		return transform;
	}

	/**
	 * Gets the subset.
	 *
	 * @param source the source
	 * @param deps the deps
	 * @param aces the aces
	 * @param turn the turn
	 * @return the subset
	 */
	private JsonElement getSubset(JsonElement source, JsonObject deps, JsonObject aces, JsonObject turn) {
		JsonElement value = null != turn ? turn.get(MysCon.VALUE) : null;
		value = mystiqueLever.isNull(value) ? null : value;
		return null != value ? mystiqueLever.getSubset(source, deps, aces, value) : source;
	}

}
