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
 * The Class SimpleSpell.
 *
 * @author balajeetm
 */
public class SimpleSpell implements Spell {

	/** The source. */
	private List<JsonElement> source;

	/** The dependencies. */
	private JsonObject dependencies;

	/** The turn. */
	private JsonElement turn;

	/**
	 * Instantiates a new simple spell.
	 *
	 * @param source the source
	 * @param dependencies the dependencies
	 * @param turn the turn
	 */
	public SimpleSpell(List<JsonElement> source, JsonObject dependencies, JsonElement turn) {
		this.source = source;
		this.dependencies = dependencies;
		this.turn = turn;
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Spell#cast(com.futuresight.util.mystique.Mystique)
	 */
	@Override
	public JsonElement cast(Mystique mystique) {
		return mystique.transform(source, dependencies, turn);
	}

}
