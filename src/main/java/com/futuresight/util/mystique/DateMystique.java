/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 7 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class ConditionMystique.
 *
 * @author balajmoh
 */
@Component
public class DateMystique extends AbstractMystique {

	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		JsonElement elementSource = JsonNull.INSTANCE;
		if (CollectionUtils.isNotEmpty(source)) {
			elementSource = source.get(0);
		}
		turn = jsonLever.isNull(turn) ? new JsonObject() : turn;
		JsonElement granularSource = getGranularSource(elementSource, turn);

		MystFunction function = factory.getDateFunction(turn.get("action"));
		return function.execute(granularSource, turn);
	}
}
