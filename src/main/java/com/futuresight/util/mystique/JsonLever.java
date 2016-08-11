/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 4 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * The Class JsonLever.
 *
 * @author balajeetm
 */
@Component
public class JsonLever {

	/** The logger. */
	private Logger logger = Logger.getLogger(getClass());

	/** The index pat. */
	private static String indexPat = "^\\[(\\d+)\\]$";

	/** The loopy pat. */
	private static String loopyPat = "^\\[\\*\\]$";

	/** The pattern. */
	private Pattern indexPattern;

	/** The pattern. */
	private Pattern loopyPattern;

	/**
	 * Gets the json parser.
	 *
	 * @return the json parser
	 */

	/**
	 * Gets the json parser.
	 *
	 * @return the json parser
	 */
	@Getter
	private JsonParser jsonParser;

	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */
	@Getter
	private Gson gson;

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
	 * Instantiates a new json lever.
	 */
	private JsonLever() {
		indexPattern = Pattern.compile(indexPat);
		loopyPattern = Pattern.compile(loopyPat);
		jsonParser = new JsonParser();
		GsonBuilder builder = new GsonBuilder();
		builder.setDateFormat(DateFormat.LONG, DateFormat.LONG);
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

			@Override
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				Date date = null;
				if (null != json && json.isJsonPrimitive()) {
					date = new Date(json.getAsJsonPrimitive().getAsLong());
				}
				return date;
			}

		});
		gson = builder.create();
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
	 * Gets the field.
	 *
	 * @param source the source
	 * @param path the path
	 * @return the field
	 */
	public JsonElement getField(JsonElement source, JsonArray path) {
		JsonElement field = null;
		try {
			field = source;
			if (null != path) {
				for (JsonElement jsonElement : path) {
					String key = jsonElement.isJsonPrimitive() ? jsonElement.getAsString() : null;
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
		}
		/**
		 * Would throw an exception for any invalid path, which is
		 * logged and ignored
		 **/
		catch (RuntimeException e) {
			logger.warn(
					String.format("Error getting field from source for %s - %s. Skipping the same", path,
							e.getMessage()), e);
			field = null;
		}
		return field;
	}

	/**
	 * Gets the field.
	 *
	 * @param source the source
	 * @param fields the fields
	 * @param path the path
	 * @return the field
	 */
	public Boolean getField(JsonElement source, JsonObject dependencies, List<JsonElement> fields, JsonArray path) {
		Boolean isLoopy = Boolean.FALSE;
		try {
			JsonElement field = source;
			if (null != path) {
				if (path.size() > 0) {
					try {
						for (int count = 0; count < path.size(); count++) {
							String key = path.get(count).getAsString();
							if (count == 0 && "@deps".equals(key)) {
								field = dependencies;
								continue;
							}
							if (isLoopy(key)) {
								isLoopy = Boolean.TRUE;
								fields.clear();
								break;
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
					}
					catch (IllegalStateException e) {
						logger.info(String.format("Invalid json path %s for %s : %s", path, source, e.getMessage()), e);
						field = JsonNull.INSTANCE;
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
					String.format("Error getting field from source for %s - %s. Skipping the same", path,
							e.getMessage()), e);
		}
		return isLoopy;
	}

	/**
	 * Sets the field.
	 *
	 * @param result the result
	 * @param to the to
	 * @param transform the transform
	 * @param optional the optional
	 * @return the json element
	 */
	public JsonObject setField(JsonObject resultWrapper, JsonArray to, JsonElement transform, Boolean optional) {
		if (optional) {
			if (isNull(transform)) {
				return resultWrapper;
			}
		}
		if (isNotNull(to)) {
			JsonElement result = resultWrapper.get("result");
			JsonElement field = result;
			if (to.size() > 0) {
				String previous = null;
				String current = null;
				Integer prevIndex = null;
				Integer currentIndex = null;
				Iterator<JsonElement> iterator = to.iterator();
				if (iterator.hasNext()) {
					previous = iterator.next().getAsString();
					prevIndex = getIndex(previous);
				}

				while (iterator.hasNext()) {
					current = iterator.next().getAsString();

					prevIndex = null != prevIndex ? prevIndex : getIndex(previous);
					currentIndex = null != currentIndex ? currentIndex : getIndex(current);

					if (null == prevIndex) {
						field = getRepleteField(field, JsonType.Object);
						result = updateResult(result, field);

						if (null == currentIndex) {
							field = updateFieldValue(field.getAsJsonObject(), previous, JsonType.Object);
						}
						else {
							field = updateFieldValue(field.getAsJsonObject(), previous, JsonType.Array);
						}
					}
					else {
						field = getRepleteField(field, JsonType.Array);
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
					field = getRepleteField(field, JsonType.Object);
					result = updateResult(result, field);
					field.getAsJsonObject().add(previous, transform);
				}
				else {
					field = getRepleteField(field, JsonType.Array);
					result = updateResult(result, field);
					updateFieldValue(field.getAsJsonArray(), prevIndex, JsonType.Null);
					field.getAsJsonArray().set(prevIndex, transform);
				}
			}
			else {
				result = transform;
			}
			resultWrapper.add("result", result);
		}
		return resultWrapper;
	}

	/**
	 * Gets the replete field.
	 *
	 * @param field the field
	 * @param type the type
	 * @return the replete field
	 */
	private JsonElement getRepleteField(JsonElement field, JsonType type) {
		if (isNull(field)) {
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
		if (isNull(result)) {
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
				JsonType newType = JsonType.Null;
				if (i == index) {
					newType = type;
				}
				field.add(getNewElement(newType));
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
	 * Checks if is null.
	 *
	 * @param element the element
	 * @return the boolean
	 */
	public Boolean isNull(JsonElement element) {
		return null == element || element.isJsonNull();
	}

	/**
	 * Checks if is not null.
	 *
	 * @param element the element
	 * @return the boolean
	 */
	public Boolean isNotNull(JsonElement element) {
		return !isNull(element);
	}

	public JsonElement getFormattedDate(Date date, JsonElement outFormat) {
		JsonElement output;
		String formatString = isNotNull(outFormat) && outFormat.isJsonPrimitive() ? StringUtils.trimToEmpty(
				outFormat.getAsString()).toLowerCase() : "long";
		switch (formatString) {
		case "long":
			output = new JsonPrimitive(date.getTime());
			break;

		case "string":
			output = new JsonPrimitive(String.valueOf(date.getTime()));
			break;

		default:
			output = new JsonPrimitive(new SimpleDateFormat(formatString).format(date));
			break;
		}
		return output;
	}

}
