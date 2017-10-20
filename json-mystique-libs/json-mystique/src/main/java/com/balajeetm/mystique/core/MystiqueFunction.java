/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Interface MystFunction.
 *
 * @author balajeetm
 */
public interface MystiqueFunction {

	/**
	 * Execute.
	 *
	 * @param source the source
	 * @param turn the turn
	 * @return the json element
	 */
	JsonElement execute(JsonElement source, JsonObject turn);
}
