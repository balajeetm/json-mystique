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

import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class ConstantMystTurn.
 *
 * @author balajeetm
 */
@Component
public class ConstantMystTurn extends AbstractMystTurn {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
		turn = jsonLever.getAsJsonObject(turn, new JsonObject());
		return turn.get(MysCon.VALUE);
	}

}
