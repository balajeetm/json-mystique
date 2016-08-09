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
import com.google.gson.JsonNull;
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

	/** The result. */
	private JsonObject resultWrapper;

	/**
	 * Instantiates a new simple spell.
	 *
	 * @param source the source
	 * @param dependencies the dependencies
	 * @param turn the turn
	 * @param result the result
	 */
	public SimpleSpell(List<JsonElement> source, JsonObject dependencies, JsonElement turn, JsonObject resultWrapper) {
		this.source = source;
		this.dependencies = dependencies;
		this.turn = turn;
		this.resultWrapper = resultWrapper;
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Spell#cast(com.futuresight.util.mystique.Mystique)
	 */
	@Override
	public JsonElement cast(Mystique mystique) {
		return null != mystique ? mystique.transform(source, dependencies, turn, resultWrapper) : JsonNull.INSTANCE;
	}

}
