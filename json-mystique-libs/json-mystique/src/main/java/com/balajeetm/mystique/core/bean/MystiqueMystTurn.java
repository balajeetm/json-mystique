/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.balajeetm.mystique.core.bean;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.balajeetm.mystique.util.gson.bean.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class MystiqueMystTurn.
 *
 * @author balajeetm
 */
@Component
public class MystiqueMystTurn extends AbstractMystTurn {

	/** The json genie. */
	@Autowired
	private JsonMystique jsonMystique;

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
		JsonElement transform = null;
		JsonElement elementSource = mystiqueLever.getFirst(source);
		if (null != elementSource) {
			turn = mystiqueLever.getAsJsonObject(turn, new JsonObject());
			String value = mystiqueLever.getAsString(turn.get(MysCon.VALUE));
			if (null != value) {
				JsonElement granularSource = getGranularSource(elementSource, turn, deps, aces);
				transform = jsonMystique.transform(granularSource, value, deps);
			}
		}
		return transform;
	}
}
