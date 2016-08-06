/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class LoopySpell.
 *
 * @author balajeetm
 */
public class LoopySpell implements Spell {

	/** The source. */
	private List<JsonElement> source;

	/** The dependencies. */
	private JsonObject dependencies;

	/** The turn. */
	private JsonElement turn;

	/**
	 * Instantiates a new loopy spell.
	 *
	 * @param source the source
	 * @param dependencies the dependencies
	 * @param turn the turn
	 */
	public LoopySpell(List<JsonElement> source, JsonObject dependencies, JsonElement turn) {
		this.source = source;
		this.dependencies = dependencies;
		this.turn = turn;
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Spell#cast(com.futuresight.util.mystique.Mystique)
	 */
	@Override
	public JsonElement cast(Mystique mystique) {
		JsonElement transform = null;
		if (CollectionUtils.isNotEmpty(source)) {
			transform = new JsonArray();
			JsonArray jsonArray = source.get(0).getAsJsonArray();
			for (JsonElement jsonElement : jsonArray) {
				transform.getAsJsonArray()
						.add(mystique.transform(Lists.newArrayList(jsonElement), dependencies, turn));
			}
		}
		return transform;
	}

}
