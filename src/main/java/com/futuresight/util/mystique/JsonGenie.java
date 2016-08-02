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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The Class JsonGenie.
 *
 * @author balajeetm
 */
@Component
public class JsonGenie {

	/** The logger. */
	private Logger logger = Logger.getLogger(this.getClass());

	/** The json convertor. */
	@Autowired
	private JsonJacksonConvertor jsonConvertor;

	/** The factory. */
	@Autowired
	private MystiqueFactory factory;

	/** The json parser. */
	private JsonParser jsonParser;

	/** The tarots. */
	private Map<String, List<Tarot>> tarots;

	/** The index pat. */
	private static String indexPat = "^\\[(\\d+)\\]$";

	/** The loopy pat. */
	private static String loopyPat = "^\\[\\*\\]$";

	/** The pattern. */
	private Pattern indexPattern;

	/** The pattern. */
	private Pattern loopyPattern;

	/**
	 * Instantiates a new json genie.
	 */
	public JsonGenie() {
		tarots = new HashMap<String, List<Tarot>>();
		jsonParser = new JsonParser();
		indexPattern = Pattern.compile(indexPat);
		loopyPattern = Pattern.compile(loopyPat);
	}

	/**
	 * The Enum JsonType.
	 */
	private enum JsonType {

		/** The Array. */
		Array,
		/** The Object. */
		Object,
		/** The Null. */
		Null;
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
						tarots.put(specName, jsonConvertor.deserializeList(resource.getInputStream(), Tarot.class));
					}
					catch (ConvertorException | IOException exception) {
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
		JsonElement source = jsonParser.parse(inputJson);
		return transform(source, specName);
	}

	/**
	 * Transform to string.
	 *
	 * @param inputJson the input json
	 * @param specName the spec name
	 * @return the string
	 */
	public String transformToString(String inputJson, String specName) {
		JsonElement transform = transform(inputJson, specName);
		return String.valueOf(transform);
	}

	/**
	 * Transform.
	 *
	 * @param source the source
	 * @param specName the spec name
	 * @return the json element
	 */
	public JsonElement transform(JsonElement source, String specName) {
		List<Tarot> tarotList = tarots.get(specName);
		JsonObject dependencies = new JsonObject();
		JsonElement transform = transform(source, tarotList, dependencies);
		if (null == transform) {
			logger.error(String.format("Invalid spec %s. No tarots for spec %s", specName, specName));
		}
		return transform;
	}

	/**
	 * Transform to string.
	 *
	 * @param source the source
	 * @param specName the spec name
	 * @return the string
	 */
	public String transformToString(JsonElement source, String specName) {
		JsonElement transform = transform(source, specName);
		return String.valueOf(transform);
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
		JsonElement result = null;
		if (CollectionUtils.isNotEmpty(tarotList)) {
			for (Tarot tarot : tarotList) {
				String turn = tarot.getTurn();
				updateDependencies(source, tarot.getDeps(), dependencies);
				Mystique mystique = factory.getMystique(turn);
				Spell trick = getSpell(source, tarot.getFrom(), dependencies, turn);
				JsonElement transform = trick.cast(mystique);
				//JsonElement transform = mystique.transform(source, tarot.getFrom(), dependencies, turn);
				List<String> to = tarot.getTo();
				try {
					result = setField(result, to, transform);
				}
				catch (RuntimeException e) {
					logger.error(String.format(
							"Error transforming input with specification for field %s with convertor %s - %s",
							Arrays.toString(to.toArray()), mystique, e.getMessage()), e);
					continue;
				}
			}
		}
		else {
			logger.error(String.format("Invalid tarots. Tarots cannot be empty"));
		}
		return result;
	}

	/**
	 * Sets the field.
	 *
	 * @param result the result
	 * @param to the to
	 * @param transform the transform
	 * @return the json element
	 */
	private JsonElement setField(JsonElement result, List<String> to, JsonElement transform) {
		JsonElement field = result;
		if (CollectionUtils.isNotEmpty(to)) {

			String previous = null;
			String current = null;
			Integer prevIndex = null;
			Integer currentIndex = null;
			Iterator<String> iterator = to.iterator();
			if (iterator.hasNext()) {
				previous = iterator.next();
				prevIndex = getIndex(previous);
			}

			while (iterator.hasNext()) {
				current = iterator.next();

				prevIndex = null != prevIndex ? prevIndex : getIndex(previous);
				currentIndex = null != currentIndex ? currentIndex : getIndex(current);

				if (null == prevIndex) {
					field = getRequestedField(field, JsonType.Object);
					result = updateResult(result, field);

					if (null == currentIndex) {
						field = updateFieldValue(field.getAsJsonObject(), previous, JsonType.Object);
					}
					else {
						field = updateFieldValue(field.getAsJsonObject(), previous, JsonType.Array);
					}
				}
				else {
					field = getRequestedField(field, JsonType.Array);
					result = updateResult(result, field);

					if (null == currentIndex) {
						field = updateFieldValue(field.getAsJsonArray(), prevIndex, JsonType.Object);
					}
					else {
						field = updateFieldValue(field.getAsJsonArray(), prevIndex, JsonType.Array);
					}
				}
				previous = current;
				prevIndex = currentIndex;
				current = null;
				currentIndex = null;
			}

			if (null == prevIndex) {
				field = getRequestedField(field, JsonType.Object);
				result = updateResult(result, field);
				field.getAsJsonObject().add(previous, transform);
			}
			else {
				field = getRequestedField(field, JsonType.Array);
				result = updateResult(result, field);
				updateFieldValue(field.getAsJsonArray(), prevIndex, JsonType.Null);
				field.getAsJsonArray().set(prevIndex, transform);
			}
		}
		else {
			result = transform;
		}
		return result;
	}

	/**
	 * Gets the requested field.
	 *
	 * @param field the field
	 * @param type the type
	 * @return the requested field
	 */
	private JsonElement getRequestedField(JsonElement field, JsonType type) {
		if (field == null) {
			field = getNewElement(type);
		}
		return field;
	}

	/**
	 * Gets the new element.
	 *
	 * @param type the type
	 * @return the new element
	 */
	private JsonElement getNewElement(JsonType type) {
		JsonElement element = null;
		switch (type) {
		case Array:
			element = new JsonArray();
			break;

		case Object:
			element = new JsonObject();
			break;

		case Null:
			element = null;
			break;

		default:
			break;
		}
		return element;
	}

	/**
	 * Update result.
	 *
	 * @param result the result
	 * @param field the field
	 * @return the json element
	 */
	private JsonElement updateResult(JsonElement result, JsonElement field) {
		if (null == result) {
			result = field;
		}
		return result;
	}

	/**
	 * Update field value.
	 *
	 * @param field the field
	 * @param index the index
	 * @param type the type
	 * @return the json element
	 */
	private JsonElement updateFieldValue(JsonArray field, Integer index, JsonType type) {
		for (int i = 0; i <= index; i++) {
			try {
				field.get(i);
			}
			catch (IndexOutOfBoundsException e) {
				field.add(getNewElement(type));
			}
		}
		return field.get(index);
	}

	/**
	 * Update field value.
	 *
	 * @param field the field
	 * @param key the key
	 * @param type the type
	 * @return the json element
	 */
	private JsonElement updateFieldValue(JsonObject field, String key, JsonType type) {
		JsonElement value = field.get(key);
		if (null == value) {
			value = getNewElement(type);
			field.add(key, value);
		}
		return value;
	}

	/**
	 * Gets the spell.
	 *
	 * @param source the source
	 * @param from the from
	 * @param dependencies the dependencies
	 * @param turn the turn
	 * @return the spell
	 */
	private Spell getSpell(JsonElement source, List<List<String>> from, JsonObject dependencies, String turn) {
		List<JsonElement> fields = new ArrayList<>();
		Spell spell = null;
		if (CollectionUtils.isNotEmpty(from)) {
			for (List<String> path : from) {
				Boolean isLoopy = getField(source, from, fields, path);
				if (isLoopy) {
					spell = new LoopySpell(fields, dependencies, turn);
					break;
				}
			}
		}
		if (null == spell) {
			spell = new SimpleSpell(fields, dependencies, turn);
		}
		return spell;
	}

	/**
	 * Gets the field.
	 *
	 * @param source the source
	 * @param from the from
	 * @param fields the fields
	 * @param path the path
	 * @return the field
	 */
	private Boolean getField(JsonElement source, List<List<String>> from, List<JsonElement> fields, List<String> path) {
		Boolean isLoopy = Boolean.FALSE;
		try {
			JsonElement field = source;
			if (CollectionUtils.isNotEmpty(path)) {
				for (String key : path) {
					if (isLoopy(key)) {
						isLoopy = Boolean.TRUE;
						fields.clear();
					}
					else {
						Integer index = getIndex(key);
						/**
						 * This means, the path refers to a json object
						 * field
						 **/
						if (null == index) {
							field = field.getAsJsonObject().get(key);
						}
						else {
							/** Get the field from the array **/
							field = field.getAsJsonArray().get(index);
						}
					}
				}
				fields.add(field);
			}
		}
		/**
		 * Would throw an exception for any invalid path, which is
		 * logged and ignored
		 **/
		catch (RuntimeException e) {
			logger.warn(
					String.format("Error getting field from source for %s - %s. Skipping the same",
							Arrays.deepToString(from.toArray()), e.getMessage()), e);
		}
		return isLoopy;
	}

	/**
	 * Gets the index.
	 *
	 * @param path the path
	 * @return the index
	 */
	private Integer getIndex(String path) {
		Integer index = null;
		Matcher matcher = indexPattern.matcher(path);
		while (matcher.find()) {
			index = Integer.valueOf(matcher.group(1));
		}
		return index;
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
	 * Update dependencies.
	 *
	 * @param source the source
	 * @param deps the deps
	 * @param dependencies the dependencies
	 */
	private void updateDependencies(JsonElement source, List<Tarot> deps, JsonObject dependencies) {
		if (CollectionUtils.isNotEmpty(deps)) {
			JsonElement transform = transform(source, deps, dependencies);
			if (null != transform) {
				Set<Entry<String, JsonElement>> entrySet = transform.getAsJsonObject().entrySet();
				for (Entry<String, JsonElement> entry : entrySet) {
					dependencies.add(entry.getKey(), entry.getValue());
				}
			}
		}
	}
}
