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

/**
 * The Interface Spell.
 *
 * @author balajeetm
 */
public interface Spell {

	/**
	 * Cast.
	 *
	 * @param mystique the mystique
	 * @return the json element
	 */
	JsonElement cast(MystTurn mystique);

}
