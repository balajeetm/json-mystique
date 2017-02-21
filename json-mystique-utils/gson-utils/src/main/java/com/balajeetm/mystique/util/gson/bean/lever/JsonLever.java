/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.bean.lever;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import lombok.Getter;

/**
 * The Class JsonLever.
 *
 * @author balajeetm
 */
@Component
public class JsonLever {

	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());

	/** The index pat. */
	private static String indexPat = "^\\[(\\d+)\\]$";

	/** The ace pat. */
	private static String acePat = "^@ace\\(([a-zA-Z_0-9$#@!%^&*]+)\\)$";

	/** The value pat. */
	private static String valuePat = "^@value\\(([a-zA-Z_0-9, $#@!%^&*]+)\\)$";

	/** The index pattern. */
	private Pattern indexPattern;

	/** The ace pattern. */
	private Pattern acePattern;

	/** The ace pattern. */
	private Pattern valuePattern;

	/** The instance. */
	private static JsonLever INSTANCE;

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

	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */

	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */

	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */

	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */

	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */

	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */

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

		/** The Json Array. */
		Array,
		/** The Json Object. */
		Object,
		/** The Json Null. */
		Null;
	}

	/**
	 * Instantiates a new json lever.
	 */
	protected JsonLever() {
		indexPattern = Pattern.compile(indexPat);
		acePattern = Pattern.compile(acePat);
		valuePattern = Pattern.compile(valuePat);
		jsonParser = new JsonParser();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat(DateFormat.LONG, DateFormat.LONG);
		gson = gsonBuilder.create();
	}

	/**
	 * Initialises the Json Lever with a singleton instance.
	 */
	@PostConstruct
	private void init() {
		INSTANCE = this;
	}

	/**
	 * Gets the single instance of JsonJacksonConvertor.
	 *
	 * @return single instance of JsonJacksonConvertor
	 */
	public static JsonLever getInstance() {
		return INSTANCE;
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
	 * Gets the ace.
	 *
	 * @param path the path
	 * @return the ace
	 */
	protected String getAce(String path) {
		String index = null;
		Matcher matcher = acePattern.matcher(path);
		while (matcher.find()) {
			index = matcher.group(1);
		}
		return index;
	}

	/**
	 * Gets the ace value.
	 *
	 * @param path the path
	 * @return the ace value
	 */
	private String getAceValue(String path) {
		String index = null;
		Matcher matcher = valuePattern.matcher(path);
		while (matcher.find()) {
			index = matcher.group(1);
		}
		return index;
	}

	/**
	 * Gets the field.
	 *
	 * @param source the json source which can be Object, Array or a Primitive
	 * @param key the qualifier key to fetch the field from the json
	 * @return the json field of interest
	 */
	public JsonElement getField(JsonElement source, String key) {
		JsonElement output = null;
		Integer index = getIndex(key);
		/**
		 * This means, the path refers to a json object field
		 **/
		if (null == index) {
			output = source.getAsJsonObject()
					.get(key);
		} else {
			/** Get the field from the array **/
			output = source.getAsJsonArray()
					.get(index);
		}
		return output;
	}

	/**
	 * Gets the field.
	 *
	 * @param source the json source which can be Object, Array or a Primitive
	 * @param jPath the json array defining the full qualified json path to the
	 *            field required
	 * @param deps the dependency list in the form of key value pair (json)
	 * @param aces the pre-processed dependency list in the form of key value
	 *            pair (json)
	 * @return the json field of interest
	 */
	public JsonElement getField(JsonElement source, JsonArray jPath, JsonObject deps, JsonObject aces) {
		JsonElement field = null;
		try {
			field = source;
			if (null != jPath) {
				for (int count = 0; count < jPath.size(); count++) {
					String key = getAsString(jPath.get(count));
					if (count == 0 && MysCon.AT_DEPS.equals(key)) {
						field = deps;
						continue;
					}
					String ace = getAce(key);
					if (null != ace) {
						field = aces.get(ace);
						continue;
					}
					key = getPathField(key, aces);
					field = getField(field, key);
				}
			}
		}
		/**
		 * Would throw an exception for any invalid path, which is logged and
		 * ignored
		 **/
		catch (RuntimeException e) {
			logger.warn(String.format("Error getting field from source for %s - %s. Skipping the same", jPath,
					e.getMessage()), e);
			field = null;
		}
		return field;
	}

	/**
	 * Gets the field of interest for a fully qualified json path.
	 *
	 * @param source the json source which can be Object, Array or a Primitive
	 * @param jPath the json array defining the full qualified json path to the
	 *            field required
	 * @return the json field of interest
	 */
	public JsonElement getField(JsonElement source, JsonArray jPath) {
		return getField(source, jPath, new JsonObject(), new JsonObject());
	}

	/**
	 * Gets the field of interest for a fully qualified json path represented as
	 * a string array.
	 *
	 * @param source the json source which can be Object, Array or a Primitive
	 * @param jPath the string array defining the full qualified json path to
	 *            the field required
	 * @return the json field of interest
	 */
	public JsonElement getField(JsonElement source, String... jPath) {
		return getField(source, newJsonArray(jPath));
	}

	/**
	 * Gets the field of interest for a fully qualified json path.
	 *
	 * @param source the json string which can be Object, Array or a Primitive
	 * @param jPath the json array defining the full qualified json path to the
	 *            field required
	 * @return the json field of interest
	 */
	public JsonElement getField(String source, JsonArray jPath) {
		return getField(jsonParser.parse(source), jPath);
	}

	/**
	 * Gets the field of interest for a fully qualified json path represented as
	 * a string array.
	 *
	 * @param source the json string which can be Object, Array or a Primitive
	 * @param jPath the string array defining the full qualified json path to
	 *            the field required
	 * @return the json field of interest
	 */
	public JsonElement getField(String source, String... jPath) {
		return getField(source, newJsonArray(jPath));
	}

	/**
	 * Gets the subset json from a source json.
	 *
	 * @param source the json source which can be Object, Array or a Primitive
	 * @param deps the dependency list in the form of key value pair (json)
	 * @param aces the pre-processed dependency list in the form of key value
	 *            pair (json)
	 * @param jPathArray the array of jPaths defining the full qualified json
	 *            paths to the required fields
	 * @return the json subset
	 */
	public JsonElement getSubset(JsonElement source, JsonObject deps, JsonObject aces, JsonArray jPathArray) {
		JsonElement finalValue = null;
		if (jPathArray.size() == 0) {
			finalValue = getField(source, jPathArray, deps, aces);
		}

		else {
			JsonElement first = getFirst(jPathArray);
			if (isJsonArray(first)) {
				finalValue = new JsonObject();
				for (JsonElement jsonElement : jPathArray) {
					JsonArray pathArray = getAsJsonArray(jsonElement);
					JsonElement subset = getField(source, pathArray, deps, aces);
					setField(finalValue.getAsJsonObject(), pathArray, subset, aces);
				}
				finalValue = finalValue.getAsJsonObject()
						.get(MysCon.RESULT);
			} else {
				finalValue = getField(source, jPathArray, deps, aces);
			}
		}
		return finalValue;
	}

	/**
	 * Gets the subset json from a source json.
	 *
	 * @param source the json source which can be Object, Array or a Primitive
	 * @param jPathArray the array of jPaths defining the full qualified json
	 *            paths to the required fields
	 * @return the json subset
	 */
	public JsonElement getSubset(JsonElement source, JsonArray jPathArray) {
		return getSubset(source, null, null, jPathArray);
	}

	/**
	 * Sets the field of a json source.
	 *
	 * @param resultWrapper the json object that wraps the result json. The
	 *            result is wrapped to ensure it passed across by reference and
	 *            fields are updated appropriately
	 * @param to the jPath json array defining the full qualified json path to
	 *            the destination field where the value must be set
	 * @param value the json value that needs to be set to the destination. This
	 *            can be an Object, Array or a Primitive
	 * @param aces the pre-processed dependency list in the form of key value
	 *            pair (json)
	 * @param optional the flag that determines if a null value must be set in
	 *            the destination. If optional is TRUE, null values are not set
	 *            in the destination
	 * @return the json result wraper object which contains the result in the
	 *         field called "result"
	 */
	public JsonObject setField(JsonObject resultWrapper, JsonArray to, JsonElement value, JsonObject aces,
			Boolean optional) {

		/**
		 * Holding a mutex on result wrapper and not making the method
		 * synchronized because, when multiple unrelated threads might be
		 * calling mystique for transformation. The resource of contention is
		 * only the result wrapper
		 **/
		synchronized (resultWrapper) {
			if (optional && isNull(value)) {
				// Do Not update result wrapper
				return resultWrapper;
			}
			if (isNotNull(to)) {
				JsonElement result = resultWrapper.get(MysCon.RESULT);
				JsonElement field = result;
				if (to.size() > 0) {
					String previous = null;
					String current = null;
					Integer prevIndex = null;
					Integer currentIndex = null;
					Iterator<JsonElement> iterator = to.iterator();
					if (iterator.hasNext()) {
						previous = getPathField(iterator.next()
								.getAsString(), aces);
						prevIndex = getIndex(previous);
					}

					while (iterator.hasNext()) {
						current = getPathField(iterator.next()
								.getAsString(), aces);

						prevIndex = null != prevIndex ? prevIndex : getIndex(previous);
						currentIndex = null != currentIndex ? currentIndex : getIndex(current);

						if (null == prevIndex) {
							field = getRepleteField(field, JsonType.Object);
							result = updateResult(result, field);

							if (null == currentIndex) {
								field = updateFieldValue(field.getAsJsonObject(), previous, JsonType.Object);
							} else {
								field = updateFieldValue(field.getAsJsonObject(), previous, JsonType.Array);
							}
						} else {
							field = getRepleteField(field, JsonType.Array);
							result = updateResult(result, field);

							if (null == currentIndex) {
								field = updateFieldValue(field.getAsJsonArray(), prevIndex, JsonType.Object);
							} else {
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
						field.getAsJsonObject()
								.add(previous, value);
					} else {
						field = getRepleteField(field, JsonType.Array);
						result = updateResult(result, field);
						updateFieldValue(field.getAsJsonArray(), prevIndex, JsonType.Null);
						field.getAsJsonArray()
								.set(prevIndex, value);
					}
				} else {
					result = merge(result, value);
				}
				resultWrapper.add(MysCon.RESULT, result);
			}
			return resultWrapper;
		}
	}

	/**
	 * Gets the processed path field from an ace.
	 *
	 * @param field the string representing part of the json path which may
	 *            include processing of an ace via @value notations
	 * @param aces the pre-processed dependency list in the form of key value
	 *            pair (json)
	 * @return the processed json path field string
	 */
	protected String getPathField(String field, JsonObject aces) {
		String ace = getAceValue(field);
		String output = field;
		if (null != ace) {
			JsonArray newJsonArray = newJsonArray(ace.split(","));
			output = getAsString(getField(aces, newJsonArray));
		}
		return output;
	}

	/**
	 * Sets the field of a json source.
	 *
	 * @param resultWrapper the json object that wraps the result json. The
	 *            result is wrapped to ensure it passed across by reference and
	 *            fields are updated appropriately
	 * @param to the jPath json array defining the full qualified json path to
	 *            the destination field where the value must be set
	 * @param value the json value that needs to be set to the destination. This
	 *            can be an Object, Array or a Primitive
	 * @param aces the pre-processed dependency list in the form of key value
	 *            pair (json)
	 * @return the json result wraper object which contains the result in the
	 *         field called "result"
	 */
	public JsonObject setField(JsonObject resultWrapper, JsonArray to, JsonElement value, JsonObject aces) {
		return setField(resultWrapper, to, value, aces, Boolean.FALSE);
	}

	/**
	 * Creates a new JsonElement (Either JsonObject, JsonArray based on the json
	 * path) and sets the field as per the json path provided.
	 *
	 * @param value the json value that needs to be set to the destination. This
	 *            can be an Object, Array or a Primitive
	 * @param to the jPath json array defining the full qualified json path to
	 *            the destination field where the value must be set
	 * @return the json element created and the field set
	 */
	public JsonElement setField(JsonElement value, JsonArray to) {
		JsonObject resultWrapper = new JsonObject();
		resultWrapper.add(MysCon.RESULT, JsonNull.INSTANCE);
		setField(resultWrapper, to, (JsonElement) null, null);
		return resultWrapper.get(MysCon.RESULT);
	}

	/**
	 * Creates a new JsonElement (Either JsonObject, JsonArray based on the json
	 * path) and sets the field as per the json path provided.
	 *
	 * @param value the json value that needs to be set to the destination. This
	 *            can be an Object, Array or a Primitive
	 * @param to jPath the string array defining the full qualified json path to
	 *            the field required
	 * @return the json element created and the field set
	 */
	public JsonElement setField(JsonElement value, String... to) {
		JsonObject resultWrapper = new JsonObject();
		resultWrapper.add(MysCon.RESULT, JsonNull.INSTANCE);
		setField(resultWrapper, newJsonArray(to), (JsonElement) null, null);
		return resultWrapper.get(MysCon.RESULT);
	}

	/**
	 * Sets the field of a json source.
	 * 
	 * If the json source is null, a new json element is created and the value
	 * is then set to the destination. Do not use this method, if multiple
	 * threads are setting different fields of the json source. Use the other
	 * "setField" methods that use result wrapper instead to be thread safe.
	 *
	 * @param result the json resource whose field is to be set. Can be null, in
	 *            which case a new json element is created as per the jPath
	 * @param value the json value that needs to be set to the destination. This
	 *            can be an Object, Array or a Primitive
	 * @param to the jPath json array defining the full qualified json path to
	 *            the destination field where the value must be set
	 * @return the json source
	 */
	public JsonElement setField(JsonElement result, JsonElement value, JsonArray to) {
		JsonObject resultWrapper = new JsonObject();
		resultWrapper.add(MysCon.RESULT, result);
		setField(resultWrapper, to, (JsonElement) null, null);
		return resultWrapper.get(MysCon.RESULT);
	}

	/**
	 * Sets the field of a json source.
	 * 
	 * If the json source is null, a new json element is created and the value
	 * is then set to the destination. Do not use this method, if multiple
	 * threads are setting different fields of the json source. Use the other
	 * "setField" methods that use result wrapper instead to be thread safe.
	 *
	 * @param result the json resource whose field is to be set. Can be null, in
	 *            which case a new json element is created as per the jPath
	 * @param value the json value that needs to be set to the destination. This
	 *            can be an Object, Array or a Primitive
	 * @param optional the flag that determines if a null value must be set in
	 *            the destination. If optional is TRUE, null values are not set
	 *            in the destination
	 * @param to the jPath json array defining the full qualified json path to
	 *            the destination field where the value must be set
	 * @return the json source
	 */
	public JsonElement setField(JsonElement result, JsonElement value, Boolean optional, JsonArray to) {
		JsonObject resultWrapper = new JsonObject();
		resultWrapper.add(MysCon.RESULT, result);
		setField(resultWrapper, to, (JsonElement) null, null, optional);
		return resultWrapper.get(MysCon.RESULT);
	}

	/**
	 * Sets the field of a json source.
	 * 
	 * If the json source is null, a new json element is created and the value
	 * is then set to the destination. Do not use this method, if multiple
	 * threads are setting different fields of the json source. Use the other
	 * "setField" methods that use result wrapper instead to be thread safe.
	 *
	 * @param result the json resource whose field is to be set. Can be null, in
	 *            which case a new json element is created as per the jPath
	 * @param value the json value that needs to be set to the destination. This
	 *            can be an Object, Array or a Primitive
	 * @param to jPath the string array defining the full qualified json path to
	 *            the field required
	 * @return the json source
	 */
	public JsonElement setField(JsonElement result, JsonElement value, String... to) {
		JsonObject resultWrapper = new JsonObject();
		resultWrapper.add(MysCon.RESULT, result);
		setField(resultWrapper, newJsonArray(to), (JsonElement) null, null);
		return resultWrapper.get(MysCon.RESULT);
	}

	/**
	 * Gets a non null json element of a specified type.
	 *
	 * @param field the field
	 * @param type the json type
	 * @return the non null replete field
	 */
	private JsonElement getRepleteField(JsonElement field, JsonType type) {
		if (isNull(field)) {
			field = getNewElement(type);
		}
		return field;
	}

	/**
	 * Gets the new json element.
	 *
	 * @param type the json type
	 * @return the new json element
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
			} catch (IndexOutOfBoundsException e) {
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
	 * Checks if the json element is null.
	 *
	 * @param source the source json element
	 * @return the boolean denoting if the source json is null
	 */
	public Boolean isNull(JsonElement source) {
		return null == source || source.isJsonNull();
	}

	/**
	 * Checks if the json element is not null.
	 *
	 * @param source the source json element
	 * @return the boolean denoting if the source json is not null
	 */
	public Boolean isNotNull(JsonElement source) {
		return !isNull(source);
	}

	/**
	 * Checks if the source json is a json primitive.
	 *
	 * @param source the source json element
	 * @return the boolean denoting if the source json is a json primitive
	 */
	public Boolean isJsonPrimitive(JsonElement source) {
		return isNotNull(source) && source.isJsonPrimitive();
	}

	/**
	 * Checks if the source json is a string.
	 *
	 * @param source the source json element
	 * @return the boolean denoting if the source json is a string
	 */
	public Boolean isString(JsonElement source) {
		return isNotNull(source) && source.isJsonPrimitive() && source.getAsJsonPrimitive()
				.isString();
	}

	/**
	 * Checks if the source json is a boolean.
	 *
	 * @param source the source json element
	 * @return the boolean denoting if the source json is a boolean
	 */
	public Boolean isBoolean(JsonElement source) {
		return isNotNull(source) && source.isJsonPrimitive() && source.getAsJsonPrimitive()
				.isBoolean();
	}

	/**
	 * Checks if the source json is a long.
	 *
	 * @param source the source json element
	 * @return the boolean denoting if the source json is a long
	 */
	public Boolean isLong(JsonElement source) {
		return isNotNull(source) && source.isJsonPrimitive() && source.getAsJsonPrimitive()
				.isNumber();
	}

	/**
	 * Checks if the source json is a json object.
	 *
	 * @param source the source json element
	 * @return the boolean denoting if the source json is a json object
	 */
	public Boolean isJsonObject(JsonElement source) {
		return isNotNull(source) && source.isJsonObject();
	}

	/**
	 * Checks if the source json is a json array.
	 *
	 * @param source the source json element
	 * @return the boolean denoting if the source json is a json array
	 */
	public Boolean isJsonArray(JsonElement source) {
		return isNotNull(source) && source.isJsonArray();
	}

	/**
	 * Returns the source json as a json primitive if possible. Else returns
	 * null
	 *
	 * @param source the source json element
	 * @return the source json as a json primitive
	 */
	public JsonPrimitive getAsJsonPrimitive(JsonElement source) {
		return isJsonPrimitive(source) ? source.getAsJsonPrimitive() : null;
	}

	/**
	 * Returns the source json as a json object if possible. Else returns null
	 *
	 * @param element the element
	 * @return the source json as a json object
	 */
	public JsonObject getAsJsonObject(JsonElement element) {
		return getAsJsonObject(element, (JsonObject) null);
	}

	/**
	 * Returns the source json as a json element if not null. Else returns the
	 * default json
	 *
	 * @param source the source json element
	 * @param defaultJson the default json
	 * @return the source json as a non null json element
	 */
	public JsonElement getAsJsonElement(JsonElement source, JsonElement defaultJson) {
		return isNotNull(source) ? source : defaultJson;
	}

	/**
	 * Returns the source json as a json object if possible. Else returns the
	 * default json
	 *
	 * @param source the source json element
	 * @param defaultJson the default json
	 * @return the source json as a json object
	 */
	public JsonObject getAsJsonObject(JsonElement source, JsonObject defaultJson) {
		return isJsonObject(source) ? source.getAsJsonObject() : defaultJson;
	}

	/**
	 * Returns the field identified by the fully qualified json path as a json
	 * object if possible. Else returns null
	 *
	 * @param source the json source element
	 * @param jPath the string array defining the full qualified json path to
	 *            the field required
	 * @return the field of interest as json object
	 */
	public JsonObject getFieldAsJsonObject(JsonElement source, String... jPath) {
		JsonElement field = getField(source, jPath);
		return getAsJsonObject(field);
	}

	/**
	 * Returns the field identified by the fully qualified json path as a json
	 * array if possible. Else returns null
	 *
	 * @param source the json source element
	 * @param jPath the string array defining the full qualified json path to
	 *            the field required
	 * @return the field of interest as json array
	 */
	public JsonArray getFieldAsJsonArray(JsonElement source, String... jPath) {
		JsonElement field = getField(source, jPath);
		return getAsJsonArray(field);
	}

	/**
	 * Returns the field identified by the fully qualified json path as a json
	 * string if possible. Else returns null
	 *
	 * @param source the json source element
	 * @param jPath the string array defining the full qualified json path to
	 *            the field required
	 * @return the field of interest as string
	 */
	public String getFieldAsString(JsonElement source, String... jPath) {
		JsonElement field = getField(source, jPath);
		return getAsString(field);
	}

	/**
	 * Returns the field identified by the fully qualified json path as a json
	 * object if possible. Else returns null
	 *
	 * @param source the json source element
	 * @param jPath the string array defining the full qualified json path to
	 *            the field required
	 * @return the field of interest as long
	 */
	public Long getFieldAsLong(JsonElement source, String... jPath) {
		JsonElement field = getField(source, jPath);
		return getAsLong(field);
	}

	/**
	 * Returns the field identified by the fully qualified json path as a
	 * boolean if possible. Else returns null
	 *
	 * @param source the json source element
	 * @param jPath the string array defining the full qualified json path to
	 *            the field required
	 * @return the field of interest as boolean
	 */
	public Boolean getFieldAsBoolean(JsonElement source, String... jPath) {
		JsonElement field = getField(source, jPath);
		return getAsBoolean(field);
	}

	/**
	 * Returns the field identified by the fully qualified json path as a json
	 * primitive if possible. Else returns null
	 *
	 * @param source the json source element
	 * @param jPath the string array defining the full qualified json path to
	 *            the field required
	 * @return the field of interest as json primitive
	 */
	public JsonPrimitive getFieldAsJsonPrimitive(JsonElement source, String... jPath) {
		JsonElement field = getField(source, jPath);
		return getAsJsonPrimitive(field);
	}

	/**
	 * Returns the source json as a json array if possible. Else returns null
	 *
	 * @param source the source json element
	 * @return the source json as a json array
	 */
	public JsonArray getAsJsonArray(JsonElement source) {
		return getAsJsonArray(source, (JsonArray) null);
	}

	/**
	 * Returns the source json as a json array if possible. Else returns the
	 * default json
	 *
	 * @param source the source json element
	 * @param defaultArray the default array
	 * @return the source json as a json array
	 */
	public JsonArray getAsJsonArray(JsonElement source, JsonArray defaultArray) {
		return isJsonArray(source) ? source.getAsJsonArray() : defaultArray;
	}

	/**
	 * Returns the source json as a string if possible. Else returns the default
	 * string
	 *
	 * @param source the source json element
	 * @param defaultStr the default string
	 * @return the source json as a string
	 */
	public String getAsString(JsonElement source, String defaultStr) {
		return isString(source) ? source.getAsString() : defaultStr;
	}

	/**
	 * Returns the source json as a string if possible. Else returns null
	 *
	 * @param source the source json element
	 * @return the source json as a string
	 */
	public String getAsString(JsonElement source) {
		return getAsString(source, (String) null);
	}

	/**
	 * Returns the source json as a boolean if possible. Else returns the
	 * default boolean
	 *
	 * @param source the source json element
	 * @param defaultBool the default boolean
	 * @return the source json as a boolean
	 */
	public Boolean getAsBoolean(JsonElement source, Boolean defaultBool) {
		return isBoolean(source) ? (Boolean) source.getAsBoolean() : defaultBool;
	}

	/**
	 * Returns the source json as a boolean if possible. Else returns null
	 *
	 * @param source the source json element
	 * @return the source json as a boolean
	 */
	public Boolean getAsBoolean(JsonElement source) {
		return getAsBoolean(source, (Boolean) null);
	}

	/**
	 * Returns the source json as a long if possible. Else returns the default
	 * long
	 *
	 * @param source the source json element
	 * @param defaultLong the default long
	 * @return the source json as a long
	 */
	public Long getAsLong(JsonElement source, Long defaultLong) {
		return isLong(source) ? (Long) source.getAsLong() : defaultLong;
	}

	/**
	 * Returns the source json as a long if possible. Else returns null
	 *
	 * @param source the source json element
	 * @return the source json as a long
	 */
	public Long getAsLong(JsonElement source) {
		return getAsLong(source, (Long) null);
	}

	/**
	 * Null safe toString method for a json element.
	 *
	 * @param source the element
	 * @return the json string
	 */
	public String toString(JsonElement source) {
		return null != source ? source.toString() : null;
	}

	/**
	 * Gets the first json element, if any, in the list. Else returns null
	 *
	 * @param elements the json elements
	 * @return the first json element in the list
	 */
	public JsonElement getFirst(List<JsonElement> elements) {
		JsonElement first = null;
		if (CollectionUtils.isNotEmpty(elements)) {
			first = elements.get(0);
		}
		return first;
	}

	/**
	 * Gets the first json element, if any, in the json array. Else returns null
	 *
	 * @param jsonArray the json array
	 * @return the first json element in the json array
	 */
	public JsonElement getFirst(JsonArray jsonArray) {
		JsonElement first = null;
		first = isNotNull(jsonArray) && jsonArray.size() > 0 ? jsonArray.get(0) : first;
		return first;
	}

	/**
	 * Returns a new json array of strings from the input strings.
	 *
	 * @param str the array of strings
	 * @return the json array of strings
	 */
	public JsonArray newJsonArray(String... str) {
		JsonArray output = new JsonArray();
		for (String string : str) {
			output.add(new JsonPrimitive(StringUtils.trim(string)));
		}
		return output;
	}

	/**
	 * Returns a new json array from the input jsons.
	 *
	 * @param elements the array of json elements
	 * @return the json array of elements
	 */
	public JsonArray newJsonArray(JsonElement... elements) {
		JsonArray output = new JsonArray();
		for (JsonElement element : elements) {
			output.add(element);
		}
		return output;
	}

	/**
	 * Formats the input date as a json element based on the format
	 * configuration.
	 *
	 * @param date the date
	 * @param dateFormat the out format
	 * @return the formatted date as a json element
	 */
	public JsonElement getFormattedDate(Date date, String dateFormat) {
		JsonElement output;
		switch (dateFormat) {
		case MysCon.LONG:
			output = new JsonPrimitive(date.getTime());
			break;

		case MysCon.STRING:
			output = new JsonPrimitive(String.valueOf(date.getTime()));
			break;

		default:
			output = new JsonPrimitive(new SimpleDateFormat(dateFormat).format(date));
			break;
		}
		return output;
	}

	/**
	 * Simple merge of two json objects. Json fields are not recursively merged
	 *
	 * @param to the json object to which the other json must be merged
	 * @param from the json object which should be merged
	 * @return the merged json object
	 */
	public JsonObject simpleMerge(JsonObject to, JsonObject from) {
		from = getAsJsonObject(from, new JsonObject());
		to = getAsJsonObject(to, new JsonObject());
		for (Entry<String, JsonElement> entry : from.entrySet()) {
			to.add(entry.getKey(), entry.getValue());
		}
		return to;
	}

	/**
	 * Simple merge of multiple json objects to a destination json.
	 *
	 * @param to the json object to which the other json must be merged
	 * @param from the json objects which should be merged
	 * @return the merged json object
	 */
	public JsonObject simpleMerge(JsonObject to, JsonObject... from) {
		to = getAsJsonObject(to, new JsonObject());
		JsonObject finalTo = to;
		for (JsonObject jsonObject : from) {
			finalTo = simpleMerge(finalTo, jsonObject);
		}
		return finalTo;
	}

	/**
	 * A recursive merge of two json elements.
	 *
	 * @param source1 the first json element
	 * @param source2 the second json element
	 * @return the recursively merged json element
	 */
	public JsonElement merge(JsonElement source1, JsonElement source2) {
		return merge(source1, source2, Boolean.FALSE);
	}

	/**
	 * A recursive merge of two json elements.
	 *
	 * @param source1 the first json element
	 * @param source2 the second json element
	 * @param mergeArray the flag to denote if arrays should be merged
	 * @return the recursively merged json element
	 */
	public JsonElement merge(JsonElement source1, JsonElement source2, Boolean mergeArray) {
		mergeArray = null == mergeArray ? Boolean.FALSE : mergeArray;
		JsonElement result = JsonNull.INSTANCE;
		source1 = getAsJsonElement(source1, JsonNull.INSTANCE);
		source2 = getAsJsonElement(source2, JsonNull.INSTANCE);
		if (source1.getClass()
				.equals(source2.getClass())) {
			if (source1.isJsonObject()) {
				JsonObject obj1 = getAsJsonObject(source1);
				JsonObject obj2 = getAsJsonObject(source2);
				result = obj1;
				JsonObject resultObj = result.getAsJsonObject();
				for (Entry<String, JsonElement> entry : obj1.entrySet()) {
					String key = entry.getKey();
					JsonElement value1 = entry.getValue();
					JsonElement value2 = obj2.get(key);
					JsonElement merge = merge(value1, value2, mergeArray);
					resultObj.add(key, merge);
				}
				for (Entry<String, JsonElement> entry : obj2.entrySet()) {
					String key = entry.getKey();
					if (!resultObj.has(key)) {
						resultObj.add(key, entry.getValue());
					}
				}
			} else if (source1.isJsonArray()) {
				result = new JsonArray();
				JsonArray resultArray = result.getAsJsonArray();
				JsonArray array1 = getAsJsonArray(source1);
				JsonArray array2 = getAsJsonArray(source2);
				int index = 0;
				int a1size = array1.size();
				int a2size = array2.size();

				if (!mergeArray) {
					for (; index < a1size && index < a2size; index++) {
						resultArray.add(merge(array1.get(index), array2.get(index), mergeArray));
					}
				}

				for (; index < a1size; index++) {
					resultArray.add(array1.get(index));
				}

				index = mergeArray ? 0 : index;

				for (; index < a2size; index++) {
					resultArray.add(array2.get(index));
				}

			} else {
				result = source1 != null ? source1 : source2;
			}
		} else {
			result = isNotNull(source1) ? source1 : source2;
		}
		return result;
	}

}
