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

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
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

	/** The aces. */
	private JsonObject aces;

	/** The turn. */
	private JsonObject turn;

	/** The result. */
	private JsonObject resultWrapper;

	/**
	 * Instantiates a new loopy spell.
	 *
	 * @param source the source
	 * @param dependencies the dependencies
	 * @param aces the aces
	 * @param turn the turn
	 * @param resultWrapper the result wrapper
	 */
	public LoopySpell(List<JsonElement> source, JsonObject dependencies, JsonObject aces, JsonObject turn,
			JsonObject resultWrapper) {
		this.source = source;
		this.dependencies = dependencies;
		this.aces = aces;
		this.turn = turn;
		this.resultWrapper = resultWrapper;
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Spell#cast(com.futuresight.util.mystique.Mystique)
	 */
	@Override
	public JsonElement cast(MystTurn mystique) {
		JsonElement transform = JsonNull.INSTANCE;
		if (CollectionUtils.isNotEmpty(source)) {
			transform = new JsonArray();
			JsonArray jsonArray = source.get(0).getAsJsonArray();
			for (JsonElement jsonElement : jsonArray) {
				transform.getAsJsonArray().add(
						mystique.transform(Lists.newArrayList(jsonElement), dependencies, aces, turn, resultWrapper));
			}
		}
		return transform;
	}

}
