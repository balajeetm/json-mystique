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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Interface MystTurn.
 *
 * @author balajeetm
 */
public interface MystTurn {

	/**
	 * Transform.
	 *
	 * @param source the source
	 * @param deps the deps
	 * @param aces the aces
	 * @param turn the turn
	 * @param resultWrapper the result wrapper
	 * @return the json element
	 */
	JsonElement transform(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn,
			JsonObject resultWrapper);

}
