/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Interface Mystique.
 *
 * @author balajeetm
 */
public interface Mystique {

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
