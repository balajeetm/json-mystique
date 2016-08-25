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

import lombok.Data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * The Class Tarot.
 *
 * @author balajeetm
 */

/**
 * Instantiates a new tarot.
 */

/**
 * Instantiates a new tarot.
 */
@Data
public class Tarot {

	/** The from. */
	private JsonArray from;

	/** The to. */
	private JsonArray to;

	/** The turn. */
	private JsonObject turn;

	/** The optional. */
	private Boolean optional = Boolean.FALSE;

	/** The deps. */
	private List<Tarot> deps;

	/**
	 * The aces.
	 * Is a map of turns. More like pre-processing of data for efficiency
	 * Each field in the json object corresponds to a turn, which is executed
	 * and saved in the ace
	 */
	private JsonObject aces;

}
