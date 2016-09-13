/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class DateMystTurn.
 *
 * @author balajeetm
 */
@Component
public class DateMystTurn extends AbstractMystTurn {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.AbstractMystTurn#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject, com.google.gson.JsonObject)
	 */
	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
		JsonElement elementSource = jsonLever.getFirst(source);
		turn = jsonLever.getAsJsonObject(turn, new JsonObject());
		JsonElement granularSource = getGranularSource(elementSource, turn, deps, aces);

		String action = StringUtils.trimToEmpty(jsonLever.getAsString(turn.get(MysCon.ACTION), MysCon.NOW))
				.toLowerCase();
		MystFunction function = factory.getDateFunction(action);
		return function.execute(granularSource, turn);
	}
}
