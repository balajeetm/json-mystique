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
		resultWrapper.add("result", JsonNull.INSTANCE);
		if (CollectionUtils.isNotEmpty(tarotList)) {
			for (Tarot tarot : tarotList) {
				updateDependencies(source, tarot.getDeps(), dependencies);
				JsonElement turn = tarot.getTurn();
				try {
					Mystique mystique = factory.getMystique(turn);
					Spell spell = getSpell(source, tarot.getFrom(), dependencies, turn, resultWrapper);
					JsonElement transform = spell.cast(mystique);
					JsonArray to = tarot.getTo();
					Boolean optional = tarot.getOptional();

					resultWrapper = jsonLever.setField(resultWrapper, to, transform, optional);
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
		return resultWrapper.get("result");
	}

	/**
	 * Gets the spell.
	 *
	 * @param source the source
	 * @param from the from
	 * @param dependencies the dependencies
	 * @param turn the turn
	 * @param resultWrapper the result wrapper
	 * @return the spell
	 */
	private Spell getSpell(JsonElement source, JsonArray from, JsonObject dependencies, JsonElement turn,
			JsonObject resultWrapper) {
		List<JsonElement> fields = new ArrayList<>();
		Spell spell = null;
		Boolean isFromLoopy = getFields(source, dependencies, from, fields);
		//Ideally isDeps should never be loopy
		if (isFromLoopy) {
			spell = new LoopySpell(fields, dependencies, turn, resultWrapper);
		}
		else {
			spell = new SimpleSpell(fields, dependencies, turn, resultWrapper);
		}
		return spell;
	}

	/**
	 * Gets the fields.
	 *
	 * @param source the source
	 * @param dependencies the dependencies
	 * @param path the path
	 * @param fields the fields
	 * @return the fields
	 */
	private Boolean getFields(JsonElement source, JsonObject dependencies, JsonArray path, List<JsonElement> fields) {
		Boolean isLoopy = Boolean.FALSE;
		if (null != path) {
			if (path.size() > 0) {
				for (JsonElement jsonElement : path) {
					if (jsonElement.isJsonArray()) {
						JsonArray fromArray = jsonElement.getAsJsonArray();
						isLoopy = isLoopy || jsonLever.getField(source, dependencies, fields, fromArray);
						//Once isloopy, the loop doesn't execute anymore
					}
					else {
						isLoopy = isLoopy || jsonLever.getField(source, dependencies, fields, path);
						break;
					}
				}
			}
			else {
				isLoopy = isLoopy || jsonLever.getField(source, dependencies, fields, path);
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
				JsonElement transform = transform(source, deps, dependencies);
				if (jsonLever.isNotNull(transform)) {
					Set<Entry<String, JsonElement>> entrySet = transform.getAsJsonObject().entrySet();
					for (Entry<String, JsonElement> entry : entrySet) {
						dependencies.add(entry.getKey(), entry.getValue());
					}
				}
			}
			catch (RuntimeException e) {
				logger.info(String.format("Could not update dependencies : %s", e.getMessage()));
			}
		}
	}
}
