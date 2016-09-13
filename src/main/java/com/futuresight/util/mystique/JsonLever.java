/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import lombok.Getter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
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

	/** The ace pat. */
	private static String acePat = "^@ace\\(([a-zA-Z_0-9$#@!%^&*]+)\\)$";

	/** The value pat. */
	private static String valuePat = "^@value\\(([a-zA-Z_0-9, $#@!%^&*]+)\\)$";

	/** The index pattern. */
	private Pattern indexPattern;

	/** The loopy pattern. */
	private Pattern loopyPattern;

	/** The ace pattern. */
	private Pattern acePattern;

	/** The ace pattern. */
	private Pattern valuePattern;

	/** The instance. */
	private static JsonLever INSTANCE;

	/** The factory. */
	@Autowired
	private MystiqueFactory factory;

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
		acePattern = Pattern.compile(acePat);
		valuePattern = Pattern.compile(valuePat);
		jsonParser = new JsonParser();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setDateFormat(DateFormat.LONG, DateFormat.LONG);
		gson = gsonBuilder.create();
	}

	/**
	 * Inits the.
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
	private String getAce(String path) {
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
	 * @param source the source
	 * @param dependencies the dependencies
	 * @param aces the aces
	 * @param fields the fields
	 * @param path the path
	 * @return the field
	 */
	public Boolean updateFields(JsonElement source, JsonObject dependencies, JsonObject aces,
			List<JsonElement> fields, JsonArray path) {
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
							String ace = getAce(key);
							if (null != ace) {
								field = aces.get(ace);
								continue;
							}
							if (isLoopy(key)) {
								isLoopy = Boolean.TRUE;
								fields.clear();
								break;
							}
							else {
								key = getPathField(key, aces);
								field = getField(field, key);
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
	 * Gets the field.
	 *
	 * @param field the field
	 * @param key the key
	 * @return the field
	 */
	private JsonElement getField(JsonElement field, String key) {
		JsonElement output = null;
		Integer index = getIndex(key);
		/**
		 * This means, the path refers to a json object
		 * field
		 **/
		if (null == index) {
			output = field.getAsJsonObject().get(key);
		}
		else {
			/** Get the field from the array **/
			output = field.getAsJsonArray().get(index);
		}
		return output;
	}

	/**
	 * Gets the field.
	 *
	 * @param source the source
	 * @param path the path
	 * @param deps the deps
	 * @param aces the aces
	 * @return the field
	 */
	public JsonElement getField(JsonElement source, JsonArray path, JsonObject deps, JsonObject aces) {
		JsonElement field = null;
		try {
			field = source;
			if (null != path) {
				for (JsonElement jsonElement : path) {
					String key = getAsString(jsonElement);
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
	 * @param path the path
	 * @return the field
	 */
	public JsonElement getField(JsonElement source, JsonArray path) {
		return getField(source, path, new JsonObject(), new JsonObject());
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
			JsonArray valueArray = valueObject.getAsJsonArray();
			if (valueArray.size() == 0) {
				finalValue = getField(source, valueArray, deps, aces);
			}

			else {
				JsonElement first = getFirst(valueArray);
				if (isJsonArray(first)) {
					finalValue = new JsonObject();
					for (JsonElement jsonElement : valueArray) {
						JsonArray pathArray = getAsJsonArray(jsonElement);
						JsonElement subset = getField(source, pathArray, deps, aces);
						setField(finalValue.getAsJsonObject(), pathArray, subset, aces);
					}
					finalValue = finalValue.getAsJsonObject().get(MysCon.RESULT);
				}
				else {
					finalValue = getField(source, valueArray, deps, aces);
				}
			}
		}
		else if (isJsonObject(valueObject)) {
			// This is a turn
			JsonObject valueJson = valueObject.getAsJsonObject();
			MystTurn mystique = factory.getMystTurn(valueJson);
			finalValue = mystique.transform(Lists.newArrayList(source), deps, aces, valueJson, new JsonObject());
		}
		return finalValue;
	}

	/**
	 * Sets the field.
	 *
	 * @param resultWrapper the result wrapper
	 * @param to the to
	 * @param transform the transform
	 * @param aces the aces
	 * @param optional the optional
	 * @return the json element
	 */
	public JsonObject setField(JsonObject resultWrapper, JsonArray to, JsonElement transform, JsonObject aces,
			Boolean optional) {
		if (optional && isNull(transform)) {
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
					previous = getPathField(iterator.next().getAsString(), aces);
					prevIndex = getIndex(previous);
				}

				while (iterator.hasNext()) {
					current = getPathField(iterator.next().getAsString(), aces);

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
			resultWrapper.add(MysCon.RESULT, result);
		}
		return resultWrapper;
	}

	/**
	 * Gets the path field.
	 *
	 * @param field the field
	 * @param aces the aces
	 * @return the path field
	 */
	private String getPathField(String field, JsonObject aces) {
		String ace = getAceValue(field);
		String output = field;
		if (null != ace) {
			JsonArray newJsonArray = newJsonArray(ace.split(","));
			output = getAsString(getField(aces, newJsonArray));
		}
		return output;
	}

	/**
	 * Sets the field.
	 *
	 * @param resultWrapper the result wrapper
	 * @param to the to
	 * @param transform the transform
	 * @param aces the aces
	 * @return the json object
	 */
	public JsonObject setField(JsonObject resultWrapper, JsonArray to, JsonElement transform, JsonObject aces) {
		return setField(resultWrapper, to, transform, aces, Boolean.FALSE);
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
	protected JsonObject getUpdatedAces(JsonElement source, JsonObject aces, JsonObject dependencies,
			JsonObject updated) {
		if (isNotNull(aces)) {
			for (Entry<String, JsonElement> entry : aces.entrySet()) {
				JsonObject value = getAsJsonObject(entry.getValue());
				//Null check required, since for all other purposes, no turn means a default turn. In this case, turn needs to be executed only if it is explicitly specified
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

	/**
	 * Checks if is json primitive.
	 *
	 * @param element the element
	 * @return the boolean
	 */
	public Boolean isJsonPrimitive(JsonElement element) {
		return isNotNull(element) && element.isJsonPrimitive();
	}

	/**
	 * Checks if is string.
	 *
	 * @param element the element
	 * @return the boolean
	 */
	public Boolean isString(JsonElement element) {
		return isNotNull(element) && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
	}

	/**
	 * Checks if is boolean.
	 *
	 * @param element the element
	 * @return the boolean
	 */
	public Boolean isBoolean(JsonElement element) {
		return isNotNull(element) && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean();
	}

	/**
	 * Checks if is long.
	 *
	 * @param element the element
	 * @return the boolean
	 */
	public Boolean isLong(JsonElement element) {
		return isNotNull(element) && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber();
	}

	/**
	 * Checks if is json object.
	 *
	 * @param element the element
	 * @return the boolean
	 */
	public Boolean isJsonObject(JsonElement element) {
		return isNotNull(element) && element.isJsonObject();
	}

	/**
	 * Checks if is json array.
	 *
	 * @param element the element
	 * @return the boolean
	 */
	public Boolean isJsonArray(JsonElement element) {
		return isNotNull(element) && element.isJsonArray();
	}

	/**
	 * Gets the as json primitive.
	 *
	 * @param element the element
	 * @return the as json primitive
	 */
	public JsonPrimitive getAsJsonPrimitive(JsonElement element) {
		return isJsonPrimitive(element) ? element.getAsJsonPrimitive() : null;
	}

	/**
	 * Gets the as json object.
	 *
	 * @param element the element
	 * @return the as json object
	 */
	public JsonObject getAsJsonObject(JsonElement element) {
		return getAsJsonObject(element, null);
	}

	/**
	 * Gets the as json element.
	 *
	 * @param element the element
	 * @param defaultJson the default json
	 * @return the as json element
	 */
	public JsonElement getAsJsonElement(JsonElement element, JsonElement defaultJson) {
		return isNotNull(element) ? element : defaultJson;
	}

	/**
	 * Gets the as json object.
	 *
	 * @param element the element
	 * @param defaultJson the default json
	 * @return the as json object
	 */
	public JsonObject getAsJsonObject(JsonElement element, JsonObject defaultJson) {
		return isJsonObject(element) ? element.getAsJsonObject() : defaultJson;
	}

	/**
	 * Gets the as json array.
	 *
	 * @param element the element
	 * @return the as json array
	 */
	public JsonArray getAsJsonArray(JsonElement element) {
		return getAsJsonArray(element, null);
	}

	/**
	 * Gets the as json array.
	 *
	 * @param element the element
	 * @param defaultArray the default array
	 * @return the as json array
	 */
	public JsonArray getAsJsonArray(JsonElement element, JsonArray defaultArray) {
		return isJsonArray(element) ? element.getAsJsonArray() : defaultArray;
	}

	/**
	 * Gets the as string.
	 *
	 * @param element the element
	 * @param defaultStr the default str
	 * @return the as string
	 */
	public String getAsString(JsonElement element, String defaultStr) {
		return isString(element) ? element.getAsString() : defaultStr;
	}

	/**
	 * Gets the as string.
	 *
	 * @param element the element
	 * @return the as string
	 */
	public String getAsString(JsonElement element) {
		return getAsString(element, null);
	}

	/**
	 * Gets the as boolean.
	 *
	 * @param element the element
	 * @param defaultBool the default bool
	 * @return the as boolean
	 */
	public Boolean getAsBoolean(JsonElement element, Boolean defaultBool) {
		return isBoolean(element) ? element.getAsBoolean() : defaultBool;
	}

	/**
	 * Gets the as boolean.
	 *
	 * @param element the element
	 * @return the as boolean
	 */
	public Boolean getAsBoolean(JsonElement element) {
		return getAsBoolean(element, null);
	}

	/**
	 * Gets the as long.
	 *
	 * @param element the element
	 * @param defaultLong the default long
	 * @return the as long
	 */
	public Long getAsLong(JsonElement element, Long defaultLong) {
		return isLong(element) ? element.getAsLong() : defaultLong;
	}

	/**
	 * Gets the as long.
	 *
	 * @param element the element
	 * @return the as long
	 */
	public Long getAsLong(JsonElement element) {
		return getAsLong(element, null);
	}

	/**
	 * Gets the first.
	 *
	 * @param elements the elements
	 * @return the first
	 */
	public JsonElement getFirst(List<JsonElement> elements) {
		JsonElement first = null;
		if (CollectionUtils.isNotEmpty(elements)) {
			first = elements.get(0);
		}
		return first;
	}

	/**
	 * Gets the first.
	 *
	 * @param valueArray the value array
	 * @return the first
	 */
	public JsonElement getFirst(JsonArray valueArray) {
		JsonElement first = null;
		first = isNotNull(valueArray) && valueArray.size() > 0 ? valueArray.get(0) : first;
		return first;
	}

	/**
	 * New json array.
	 *
	 * @param path the path
	 * @return the json array
	 */
	public JsonArray newJsonArray(String... path) {
		JsonArray output = new JsonArray();
		for (String string : path) {
			output.add(new JsonPrimitive(StringUtils.trim(string)));
		}
		return output;
	}

	/**
	 * New json array.
	 *
	 * @param path the path
	 * @return the json array
	 */
	public JsonArray newJsonArray(JsonElement... path) {
		JsonArray output = new JsonArray();
		for (JsonElement element : path) {
			output.add(element);
		}
		return output;
	}

	/**
	 * Gets the formatted date.
	 *
	 * @param date the date
	 * @param outFormat the out format
	 * @return the formatted date
	 */
	public JsonElement getFormattedDate(Date date, String outFormat) {
		JsonElement output;
		switch (outFormat) {
		case MysCon.LONG:
			output = new JsonPrimitive(date.getTime());
			break;

		case MysCon.STRING:
			output = new JsonPrimitive(String.valueOf(date.getTime()));
			break;

		default:
			output = new JsonPrimitive(new SimpleDateFormat(outFormat).format(date));
			break;
		}
		return output;
	}

	/**
	 * Simple merge.
	 *
	 * @param from the from
	 * @param to the to
	 * @return the json object
	 */
	public JsonObject simpleMerge(JsonObject from, JsonObject to) {
		from = getAsJsonObject(from, new JsonObject());
		to = getAsJsonObject(to, new JsonObject());
		for (Entry<String, JsonElement> entry : from.entrySet()) {
			to.add(entry.getKey(), entry.getValue());
		}
		return to;
	}

	/**
	 * Merge.
	 *
	 * @param src1 the src1
	 * @param src2 the src2
	 * @return the json element
	 */
	public JsonElement merge(JsonElement src1, JsonElement src2) {
		return merge(src1, src2, Boolean.FALSE);
	}

	/**
	 * Merge.
	 *
	 * @param src1 the src1
	 * @param src2 the src2
	 * @param mergeArray the merge array
	 * @return the json element
	 */
	public JsonElement merge(JsonElement src1, JsonElement src2, Boolean mergeArray) {
		mergeArray = null == mergeArray ? Boolean.FALSE : mergeArray;
		JsonElement result = JsonNull.INSTANCE;
		src1 = getAsJsonElement(src1, JsonNull.INSTANCE);
		src2 = getAsJsonElement(src2, JsonNull.INSTANCE);
		if (src1.getClass().equals(src2.getClass())) {
			if (src1.isJsonObject()) {
				JsonObject obj1 = getAsJsonObject(src1);
				JsonObject obj2 = getAsJsonObject(src2);
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
			}
			else if (src1.isJsonArray()) {
				result = new JsonArray();
				JsonArray resultArray = result.getAsJsonArray();
				JsonArray array1 = getAsJsonArray(src1);
				JsonArray array2 = getAsJsonArray(src2);
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

			}
			else {
				result = src1 != null ? src1 : src2;
			}
		}
		else {
			result = isNotNull(src1) ? src1 : src2;
		}
		return result;
	}

}
