/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * The Class JsonGenie.
 *
 * @author balajeetm
 */
@Component
public class JsonGenie {

	/** The logger. */
	private Logger logger = Logger.getLogger(this.getClass());

	/** The factory. */
	@Autowired
	private MystiqueFactory factory;

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

	/** The tarots. */
	private Map<String, List<Tarot>> tarots;

	/**
	 * Instantiates a new json genie.
	 */
	public JsonGenie() {
		tarots = new HashMap<String, List<Tarot>>();
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		String locationPattern = "classpath:jsonmystique/*.mys";
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		try {
			resources = resourceResolver.getResources(locationPattern);
		}
		catch (IOException e) {
			logger.error(String.format("Error loading mystiques from %s - %s", locationPattern, e.getMessage()), e);
			return;
		}

		if (!ArrayUtils.isEmpty(resources)) {
			for (Resource resource : resources) {
				if (resource.exists()) {
					String specName = FilenameUtils.removeExtension(resource.getFilename());
					try {
						List<Tarot> fromJson = jsonLever.getGson().fromJson(
								IOUtils.toString(resource.getInputStream()), new TypeToken<List<Tarot>>() {
								}.getType());
						tarots.put(specName, fromJson);
					}
					catch (JsonSyntaxException | IOException exception) {
						logger.error(String.format(
								"Unable to load mystiques %s from %s - %s. Trying to load other mystiques if any",
								specName, locationPattern, exception.getMessage()), exception);
						continue;
					}
				}
			}
		}
		else {
			logger.warn(String.format("No mystiques found @ %s for transformation", locationPattern));
		}
	}

	/**
	 * Transform.
	 *
	 * @param inputJson the input json
	 * @param specName the spec name
	 * @return the json element
	 */
	public JsonElement transform(String inputJson, String specName) {
		return transform(inputJson, specName, new JsonObject());
	}

	/**
	 * Transform.
	 *
	 * @param source the source
	 * @param specName the spec name
	 * @return the json element
	 */
	public JsonElement transform(JsonElement source, String specName) {
		return transform(source, specName, new JsonObject());
	}

	/**
	 * Transform to string.
	 *
	 * @param inputJson the input json
	 * @param specName the spec name
	 * @return the string
	 */
	public String transformToString(String inputJson, String specName) {
		JsonElement transform = transform(inputJson, specName, new JsonObject());
		return String.valueOf(transform);
	}

	/**
	 * Transform to string.
	 *
	 * @param source the source
	 * @param specName the spec name
	 * @return the string
	 */
	public String transformToString(JsonElement source, String specName) {
		JsonElement transform = transform(source, specName, new JsonObject());
		return String.valueOf(transform);
	}

	/**
	 * Transform.
	 *
	 * @param inputJson the input json
	 * @param specName the spec name
	 * @param deps the deps
	 * @return the json element
	 */
	public JsonElement transform(String inputJson, String specName, JsonObject deps) {
		JsonElement source = jsonLever.getJsonParser().parse(inputJson);
		return transform(source, specName, deps);
	}

	/**
	 * Transform.
	 *
	 * @param source the source
	 * @param specName the spec name
	 * @param deps the deps
	 * @return the json element
	 */
	public JsonElement transform(JsonElement source, String specName, JsonObject deps) {
		List<Tarot> tarotList = tarots.get(specName);
		JsonObject dependencies = null == deps ? new JsonObject() : deps;
		JsonElement transform = transform(source, tarotList, dependencies);
		if (null == transform) {
			logger.error(String.format("Invalid spec %s. No tarots for spec %s", specName, specName));
		}
		return transform;
	}

	/**
	 * Transform.
	 *
	 * @param source the source
	 * @param tarotList the tarot list
	 * @param dependencies the dependencies
	 * @return the json element
	 */
	private JsonElement transform(JsonElement source, List<Tarot> tarotList, JsonObject dependencies) {
		JsonObject resultWrapper = new JsonObject();
		resultWrapper.add("result", null);
		if (CollectionUtils.isNotEmpty(tarotList)) {
			for (Tarot tarot : tarotList) {
				JsonElement turn = tarot.getTurn();
				updateDependencies(source, tarot.getDeps(), dependencies);
				Mystique mystique = factory.getMystique(turn);
				Spell trick = getSpell(source, tarot.getFrom(), dependencies, turn, resultWrapper);
				JsonElement transform = trick.cast(mystique);
				//JsonElement transform = mystique.transform(source, tarot.getFrom(), dependencies, turn);
				JsonArray to = tarot.getTo();
				Boolean optional = tarot.getOptional();
				try {
					resultWrapper = jsonLever.setField(resultWrapper, to, transform, optional);
				}
				catch (RuntimeException e) {
					logger.error(String.format(
							"Error transforming input with specification for field %s with convertor %s - %s", to,
							mystique, e.getMessage()), e);
					continue;
				}
			}
		}
		else {
			logger.error(String.format("Invalid tarots. Tarots cannot be empty"));
		}
		return resultWrapper.get("result");
	}

	/**
	 * Gets the spell.
	 *
	 * @param source the source
	 * @param from the from
	 * @param dependencies the dependencies
	 * @param turn the turn
	 * @param result the result
	 * @return the spell
	 */
	private Spell getSpell(JsonElement source, JsonArray from, JsonObject dependencies, JsonElement turn,
			JsonObject resultWrapper) {
		List<JsonElement> fields = new ArrayList<>();
		Spell spell = null;
		if (null != from && from.size() > 0) {
			for (JsonElement jsonElement : from) {
				if (jsonElement.isJsonArray()) {
					JsonArray fromArray = jsonElement.getAsJsonArray();
					Boolean isLoopy = jsonLever.getField(source, fields, fromArray);
					if (isLoopy) {
						spell = new LoopySpell(fields, dependencies, turn, resultWrapper);
						break;
					}
				}
				else {
					Boolean isLoopy = jsonLever.getField(source, fields, from);
					if (isLoopy) {
						spell = new LoopySpell(fields, dependencies, turn, resultWrapper);
					}
					break;
				}
			}
		}
		if (null == spell) {
			spell = new SimpleSpell(fields, dependencies, turn, resultWrapper);
		}
		return spell;
	}

	/**
	 * Update dependencies.
	 *
	 * @param source the source
	 * @param deps the deps
	 * @param dependencies the dependencies
	 */
	private void updateDependencies(JsonElement source, List<Tarot> deps, JsonObject dependencies) {
		if (CollectionUtils.isNotEmpty(deps)) {
			JsonElement transform = transform(source, deps, dependencies);
			if (jsonLever.isNotNull(transform)) {
				Set<Entry<String, JsonElement>> entrySet = transform.getAsJsonObject().entrySet();
				for (Entry<String, JsonElement> entry : entrySet) {
					dependencies.add(entry.getKey(), entry.getValue());
				}
			}
		}
	}
}
