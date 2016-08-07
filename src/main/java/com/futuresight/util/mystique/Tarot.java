/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import lombok.Data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * The Class Tarot.
 *
 * @author balajeetm
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
	private JsonElement turn;

	/** The deps. */
	private List<Tarot> deps;

	/** The optional. */
	private Boolean optional = Boolean.FALSE;

}
