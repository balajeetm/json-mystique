/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import com.futuresight.util.mystique.lever.MysCon;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
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
	private JsonGenie() {
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
						@SuppressWarnings("serial")
						Type type = new TypeToken<List<Tarot>>() {
						}.getType();
						List<Tarot> fromJson = jsonLever.getGson().fromJson(
								IOUtils.toString(resource.getInputStream()), type);
						tarots.put(specName, fromJson);
					}
					catch (JsonSyntaxException | IllegalArgumentException | IOException exception) {
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
		JsonElement source = jsonLever.getJsonParser().parse(inputJson);
		return transform(source, specName);
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
		if (jsonLever.isNull(transform)) {
			logger.info(String.format("Transformed value for spec %s is null", specName));
		}
		return transform;
	}

	/**
	 * Transform to string.
	 *
	 * @param inputJson the input json
	 * @param specName the spec name
	 * @return the string
	 */
	public String transformToString(String inputJson, String specName) {
		return String.valueOf(transform(inputJson, specName));
	}

	/**
	 * Transform to string.
	 *
	 * @param inputJson the input json
	 * @param specName the spec name
	 * @param deps the deps
	 * @return the string
	 */
	public String transformToString(String inputJson, String specName, JsonObject deps) {
		return String.valueOf(transform(inputJson, specName, deps));
	}

	/**
	 * Transform to string.
	 *
	 * @param source the source
	 * @param specName the spec name
	 * @return the string
	 */
	public String transformToString(JsonElement source, String specName) {
		return String.valueOf(transform(source, specName));
	}

	/**
	 * Transform to string.
	 *
	 * @param source the source
	 * @param specName the spec name
	 * @param deps the deps
	 * @return the string
	 */
	public String transformToString(JsonElement source, String specName, JsonObject deps) {
		return String.valueOf(transform(source, specName, deps));
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
		resultWrapper.add(MysCon.RESULT, JsonNull.INSTANCE);
		if (CollectionUtils.isNotEmpty(tarotList)) {
			for (Tarot tarot : tarotList) {
				updateDependencies(source, tarot.getDeps(), dependencies);
				JsonObject aces = tarot.getAces();
				updateAces(source, aces, dependencies);
				JsonObject turn = tarot.getTurn();
				try {
					Mystique mystique = factory.getMystique(turn);
					Spell spell = getSpell(source, tarot.getFrom(), dependencies, aces, turn, resultWrapper);
					JsonElement transform = spell.cast(mystique);
					resultWrapper = jsonLever.setField(resultWrapper, tarot.getTo(), transform, aces,
							tarot.getOptional());
				}
				catch (RuntimeException e) {
					logger.info(
							String.format("Error transforming input with specification for turn %s - %s", turn,
									e.getMessage()), e);
					continue;
				}
			}
		}
		else {
			logger.info(String.format("Invalid tarots. Tarots cannot be empty"));
		}
		return resultWrapper.get(MysCon.RESULT);
	}

	/**
	 * Gets the spell.
	 *
	 * @param source the source
	 * @param from the from
	 * @param dependencies the dependencies
	 * @param aces the aces
	 * @param turn the turn
	 * @param resultWrapper the result wrapper
	 * @return the spell
	 */
	private Spell getSpell(JsonElement source, JsonArray from, JsonObject dependencies, JsonObject aces,
			JsonObject turn, JsonObject resultWrapper) {
		List<JsonElement> fields = new ArrayList<>();
		Boolean isFromLoopy = getFields(source, dependencies, aces, from, fields);
		//Ideally isDeps should never be loopy
		Spell spell = isFromLoopy ? new LoopySpell(fields, dependencies, aces, turn, resultWrapper) : new SimpleSpell(
				fields, dependencies, aces, turn, resultWrapper);
		return spell;
	}

	/**
	 * Gets the fields.
	 *
	 * @param source the source
	 * @param dependencies the dependencies
	 * @param aces the aces
	 * @param path the path
	 * @param fields the fields
	 * @return the fields
	 */
	private Boolean getFields(JsonElement source, JsonObject dependencies, JsonObject aces, JsonArray path,
			List<JsonElement> fields) {
		Boolean isLoopy = Boolean.FALSE;
		if (null != path) {
			if (path.size() > 0) {
				for (JsonElement jsonElement : path) {
					if (jsonElement.isJsonArray()) {
						JsonArray fromArray = jsonElement.getAsJsonArray();
						isLoopy = isLoopy || jsonLever.updateFields(source, dependencies, aces, fields, fromArray);
						//Once isloopy, the loop doesn't execute anymore
					}
					else {
						isLoopy = isLoopy || jsonLever.updateFields(source, dependencies, aces, fields, path);
						break;
					}
				}
			}
			else {
				isLoopy = isLoopy || jsonLever.updateFields(source, dependencies, aces, fields, path);
			}
		}
		return isLoopy;
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
			try {
				JsonObject transformJson = jsonLever.getAsJsonObject(transform(source, deps, dependencies));
				if (null != transformJson) {
					for (Entry<String, JsonElement> entry : transformJson.entrySet()) {
						dependencies.add(entry.getKey(), entry.getValue());
					}
				}
			}
			catch (RuntimeException e) {
				logger.info(String.format("Could not update dependencies : %s", e.getMessage()));
			}
		}
	}

	/**
	 * Update aces.
	 *
	 * @param source the source
	 * @param aces the aces
	 * @param dependencies the dependencies
	 */
	private void updateAces(JsonElement source, JsonObject aces, JsonObject dependencies) {
		if (jsonLever.isNotNull(aces)) {
			for (Entry<String, JsonElement> entry : aces.entrySet()) {
				JsonObject value = jsonLever.getAsJsonObject(entry.getValue());
				//Null check required, since for all other purposes, no turn means a default turn. In this case, turn needs to be executed only if it is explicitly specified
				Mystique mystique = null != value ? factory.getMystique(value) : null;
				if (null != mystique) {
					entry.setValue(mystique.transform(Lists.newArrayList(source), dependencies, aces, value,
							new JsonObject()));
				}
			}
		}
	}
}
