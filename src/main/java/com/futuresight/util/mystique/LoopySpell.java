/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.futuresight.util.mystique.lever.MysCon;
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

	protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private JsonLever jsonLever;

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
		jsonLever = JsonLever.getInstance();
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Spell#cast(com.futuresight.util.mystique.Mystique)
	 */
	@Override
	public JsonElement cast(MystTurn mystique) {
		JsonElement transform = JsonNull.INSTANCE;
		if (CollectionUtils.isNotEmpty(source)) {
			transform = new JsonArray();
			List<CompletableFuture<JsonElement>> cfs = new ArrayList<>();
			JsonArray jsonArray = source.get(0).getAsJsonArray();
			for (JsonElement jsonElement : jsonArray) {

				CompletableFuture<JsonElement> transformElement = CompletableFuture.supplyAsync(
						() -> mystique.transform(Lists.newArrayList(jsonElement), dependencies, aces, turn,
								resultWrapper)).exceptionally(
						e -> {
							String msg = String.format(
									"Error transforming one of the elements in the array for %s - %s", turn,
									e.getMessage());
							logger.info(msg, e);
							return JsonNull.INSTANCE;
						});
				cfs.add(transformElement);
			}

			for (CompletableFuture<JsonElement> completableFuture : cfs) {
				JsonElement element = null;
				try {
					element = completableFuture.get();
				}
				catch (InterruptedException | ExecutionException e) {
					String msg = String.format(
							"Error getting transformed element for one of the elements in the array for %s - %s",
							turn, e.getMessage());
					logger.info(msg, e);
					element = JsonNull.INSTANCE;
				}
				Boolean optional = jsonLever.getAsBoolean(turn.get(MysCon.OPTIONAL), Boolean.FALSE);
				if (jsonLever.isNotNull(element) || !optional) {
					transform.getAsJsonArray().add(element);
				}
			}
		}
		return transform;
	}
}
