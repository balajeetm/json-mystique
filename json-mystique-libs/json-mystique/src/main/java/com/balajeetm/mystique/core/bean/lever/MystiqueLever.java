/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.balajeetm.mystique.core.bean.lever;

import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.balajeetm.mystique.core.bean.MystTurn;
import com.balajeetm.mystique.core.bean.MystiqueFactory;
import com.balajeetm.mystique.util.gson.bean.lever.JsonLever;
import com.balajeetm.mystique.util.gson.bean.lever.MysCon;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class JsonLever.
 *
 * @author balajeetm
 */
@Component
public class MystiqueLever extends JsonLever {

	/** The loopy pat. */
	private static String loopyPat = "^\\[\\*\\]$";

	/** The loopy pattern. */
	private Pattern loopyPattern;

	/** The instance. */
	private static MystiqueLever INSTANCE;

	/** The factory. */
	@Autowired
	private MystiqueFactory factory;

	/**
	 * Instantiates a new mystique lever.
	 */
	private MystiqueLever() {
		loopyPattern = Pattern.compile(loopyPat);
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	private void init() {
		INSTANCE = this;
	}

	/**
	 * Gets the single instance of MystiqueLever.
	 *
	 * @return single instance of MystiqueLever
	 */
	public static MystiqueLever getInstance() {
		return INSTANCE;
	}

	/**
	 * Checks if is loopy.
	 *
	 * @param path the path
	 * @return the boolean
	 */
	private Boolean isLoopy(String path) {
		Boolean loopy = Boolean.FALSE;
		Matcher matcher = loopyPattern.matcher(path);
		while (matcher.find()) {
			loopy = Boolean.TRUE;
		}
		return loopy;
	}

	/**
	 * Update fields.
	 *
	 * @param source the source
	 * @param dependencies the dependencies
	 * @param aces the aces
	 * @param fields the fields
	 * @param path the path
	 * @return the boolean
	 */
	public Boolean updateFields(JsonElement source, JsonObject dependencies, JsonObject aces, List<JsonElement> fields,
			JsonArray path) {
		Boolean isLoopy = Boolean.FALSE;
		try {
			JsonElement field = source;
			if (null != path) {
				if (path.size() > 0) {
					try {
						for (int count = 0; count < path.size(); count++) {
							String key = getAsString(path.get(count));
							if (count == 0 && MysCon.AT_DEPS.equals(key)) {
								field = dependencies;
								continue;
							}
							String ace = super.getAce(key);
							if (null != ace) {
								field = aces.get(ace);
								continue;
							}
							if (isLoopy(key)) {
								isLoopy = Boolean.TRUE;
								fields.clear();
								break;
							} else {
								key = super.getPathField(key, aces);
								field = super.getField(field, key);
							}
						}
					} catch (IllegalStateException e) {
						logger.info(String.format("Invalid json path %s for %s : %s", path, source, e.getMessage()), e);
						field = JsonNull.INSTANCE;
					}
				}
				fields.add(field);
			}
		}
		/**
		 * Would throw an exception for any invalid path, which is logged and
		 * ignored
		 **/
		catch (RuntimeException e) {
			logger.warn(String.format("Error getting field from source for %s - %s. Skipping the same", path,
					e.getMessage()), e);
		}
		return isLoopy;
	}

	/**
	 * Gets the subset.
	 *
	 * @param source the source
	 * @param deps the deps
	 * @param aces the aces
	 * @param valueObject the value object
	 * @return the subset
	 */
	public JsonElement getSubset(JsonElement source, JsonObject deps, JsonObject aces, JsonElement valueObject) {
		JsonElement finalValue = null;
		if (valueObject.isJsonArray()) {
			finalValue = super.getSubset(source, deps, aces, valueObject.getAsJsonArray());
		} else if (isJsonObject(valueObject)) {
			// This is a turn
			JsonObject valueJson = valueObject.getAsJsonObject();
			MystTurn mystique = factory.getMystTurn(valueJson);
			finalValue = mystique.transform(Lists.newArrayList(source), deps, aces, valueJson, new JsonObject());
		}
		return finalValue;
	}

	/**
	 * Gets the updated aces.
	 *
	 * @param source the source
	 * @param aces the aces
	 * @param dependencies the dependencies
	 * @param updated the updated
	 * @return the updated aces
	 */
	public JsonObject getUpdatedAces(JsonElement source, JsonObject aces, JsonObject dependencies, JsonObject updated) {
		if (isNotNull(aces)) {
			for (Entry<String, JsonElement> entry : aces.entrySet()) {
				JsonObject value = getAsJsonObject(entry.getValue());
				// Null check required, since for all other purposes, no turn
				// means a default turn. In this case, turn needs to be executed
				// only if it is explicitly specified
				MystTurn mystique = null != value ? factory.getMystTurn(value) : null;
				if (null != mystique) {
					JsonElement transform = mystique.transform(Lists.newArrayList(source), dependencies, aces, value,
							new JsonObject());
					updated.add(entry.getKey(), transform);
				}
			}
		}
		return updated;
	}

	/**
	 * Gets the updated aces.
	 *
	 * @param source the source
	 * @param aces the aces
	 * @param dependencies the dependencies
	 * @return the updated aces
	 */
	protected JsonObject getUpdatedAces(JsonElement source, JsonObject aces, JsonObject dependencies) {
		return getUpdatedAces(source, aces, dependencies, aces);
	}
}
