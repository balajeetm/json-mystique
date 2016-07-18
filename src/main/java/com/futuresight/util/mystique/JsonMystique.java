/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import com.google.gson.JsonPrimitive;

/**
 * The Class JsonMystique.
 *
 * @author balajeetm
 */
@Component
public class JsonMystique {

	/** The logger. */
	private Logger logger = Logger.getLogger(this.getClass());

	/** The mystiques. */
	private Map<String, List<Turn>> mystiques;

	/** The json convertor. */
	@Autowired
	private JsonJacksonConvertor jsonConvertor;

	/** The json parser. */
	private JsonParser jsonParser;

	/** The factory. */
	@Autowired
	private MystiqueFactory factory;

	/**
	 * The index pattern - Starts with '[', followed by any number of digits,
	 * ends with ']'.
	 */
	private static String indexPattern = "^\\[(\\d+)\\]$";

	/** The pattern. */
	private Pattern pattern;

	/**
	 * Instantiates a new json mystique.
	 */
	public JsonMystique() {
		mystiques = new HashMap<String, List<Turn>>();
		jsonParser = new JsonParser();
		pattern = Pattern.compile(indexPattern);
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
						mystiques.put(specName, jsonConvertor.deserializeList(resource.getInputStream(), Turn.class));
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
	 * Transform to pojo.
	 *
	 * @param inputJson the input json
	 * @param specName the spec name
	 * @return the json element
	 */
	public JsonElement transformToPojo(String inputJson, String specName) {
		JsonElement result = null;
		List<Turn> spec = mystiques.get(specName);
		if (CollectionUtils.isNotEmpty(spec)) {
			JsonElement source = jsonParser.parse(inputJson);
			for (Turn turn : spec) {
				String convertor = factory.getConvertor(turn.getConvertor());
				List<String> to = turn.getTo();
				try {
					JsonElement transform = null;
					String constant = turn.getConstant();
					if (null != constant) {
						transform = new JsonPrimitive(constant);
					}
					else {
						Mystique mystique = factory.getMystique(convertor);
						List<JsonElement> from = getFields(source, turn.getFrom());
						transform = mystique.transform(from);
					}
					result = setField(result, to, transform);
				}
				catch (RuntimeException e) {
					logger.error(String.format(
							"Error transforming input with specification %s for field %s with convertor %s - %s",
							specName, Arrays.toString(to.toArray()), convertor, e.getMessage()), e);
					continue;
				}
			}
		}
		else {
			logger.error(String.format("Invalid mystique. No specification exists in system for %s", specName));
		}
		return result;
	}

	/**
	 * Transform.
	 *
	 * @param inputJson the input json
	 * @param specName the spec name
	 * @return the string
	 */
	public String transform(String inputJson, String specName) {
		JsonElement result = transformToPojo(inputJson, specName);
		return null == result ? null : result.toString();
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
	 * Gets the fields.
	 *
	 * @param source the source
	 * @param from the from
	 * @return the fields
	 */
	private List<JsonElement> getFields(JsonElement source, List<List<String>> from) {
		List<JsonElement> fields = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(from)) {
			for (List<String> list : from) {
				try {
					fields.add(getField(source, list));
				}
				/**
				 * Would throw an exception for any invalid path, which is
				 * logged and ignored
				 **/
				catch (RuntimeException e) {
					logger.warn(
							String.format("Error getting field from source for %s - %s. Skipping the same",
									Arrays.deepToString(from.toArray()), e.getMessage()), e);
					continue;
				}
			}
		}
		return fields;
	}

	/**
	 * Gets the field.
	 *
	 * @param source the source
	 * @param path the path
	 * @return the field
	 * @throws RuntimeException the runtime exception
	 */
	private JsonElement getField(JsonElement source, List<String> path) throws RuntimeException {
		JsonElement field = null;
		if (CollectionUtils.isNotEmpty(path)) {
			field = source;

			for (String key : path) {
				Integer index = getIndex(key);
				/** This means, the path refers to a json object field **/
				if (null == index) {
					field = field.getAsJsonObject().get(key);
				}
				else {
					/** Get the field from the array **/
					field = field.getAsJsonArray().get(index);
				}
			}
		}
		return field;
	}

	/**
	 * Gets the index.
	 *
	 * @param path the path
	 * @return the index
	 */
	private Integer getIndex(String path) {
		Integer index = null;
		Matcher matcher = pattern.matcher(path);
		while (matcher.find()) {
			index = Integer.valueOf(matcher.group(1));
		}
		return index;
	}
}
