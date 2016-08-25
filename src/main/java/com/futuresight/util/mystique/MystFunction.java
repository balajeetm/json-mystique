/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Interface MystFunction.
 *
 * @author balajmoh
 */
public interface MystFunction {

	/**
	 * Execute.
	 *
	 * @param source the source
	 * @param turn the turn
	 * @return the json element
	 */
	JsonElement execute(JsonElement source, JsonObject turn);
}
