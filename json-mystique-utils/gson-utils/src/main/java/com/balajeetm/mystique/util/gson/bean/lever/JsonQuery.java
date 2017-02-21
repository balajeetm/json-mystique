/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 12 Oct, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.bean.lever;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import rx.Observable;
import rx.exceptions.Exceptions;
import rx.schedulers.Schedulers;

/**
 * The Class JsonQuery.
 *
 * @author balajeetm
 */
@Component
public class JsonQuery {

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

	/** The query types. */
	private Map<String, BiFunction<JsonElement, JsonObject, Boolean>> queryTypes;

	/**
	 * Instantiates a new json query.
	 */
	private JsonQuery() {
		queryTypes = new HashMap<>();
		queryTypes.put("=", this::equalsFilter);
		queryTypes.put("!=", this::notEqualsFilter);
		queryTypes.put("in", this::inFilter);
		queryTypes.put("notin", this::notinFilter);
	}

	/**
	 * Query.
	 *
	 * @param query the query
	 * @return the json element
	 */
	public JsonElement query(JsonObject query) {

		JsonElement result = null;
		try {
			result = queryAsync(query).reduce(new JsonArray(), (resultarray, json) -> {
				resultarray.add(json);
				return resultarray;
			})
					.toBlocking()
					.first();
		} catch (RuntimeException e) {
			result = new JsonPrimitive(e.getMessage());
		}

		return result;
	}

	/**
	 * Query async.
	 *
	 * @param query the query
	 * @return the observable
	 */
	public Observable<JsonElement> queryAsync(JsonObject query) {
		return Observable.just(query)
				.map(q -> validate(q))
				.flatMap(s -> {
					Observable<JsonElement> obs = null;
					if (null == s) {
						JsonElement from = query.get("from");
						obs = jsonLever.isJsonArray(from) ? queryAsync(from.getAsJsonArray(), query)
								: queryAsync(from.getAsJsonObject(), query);
					} else {
						Exceptions.propagate(new Throwable(s));
					}
					return obs;
				});
	}

	/**
	 * Query async.
	 *
	 * @param array the array
	 * @param query the query
	 * @return the observable
	 */
	private Observable<JsonElement> queryAsync(JsonArray array, JsonObject query) {
		return Observable.from(array)
				.observeOn(Schedulers.computation())
				.filter(json -> filter(json, query.get("where")))
				.compose(obs -> {
					Long limit = jsonLever.getAsLong(query.get("limit"), 0L);
					return limit > 0 ? obs.limit(limit.intValue()) : obs;
				})
				.observeOn(Schedulers.computation())
				.compose(obs -> StringUtils.equals(jsonLever.getFieldAsString(query, "select"), "count")
						? obs.count()
								.map(count -> new JsonPrimitive(count))
						: obs.map(json -> select(json, query.get("select"))));
	}

	/**
	 * Query async.
	 *
	 * @param obj the obj
	 * @param query the query
	 * @return the observable
	 */
	private Observable<JsonElement> queryAsync(JsonElement obj, JsonObject query) {
		return Observable.just(obj)
				.observeOn(Schedulers.computation())
				.filter(json -> filter(json, query.get("where")))
				.compose(obs -> {
					Long limit = jsonLever.getAsLong(query.get("limit"), 0L);
					return limit > 0 ? obs.limit(limit.intValue()) : obs;
				})
				.observeOn(Schedulers.computation())
				.compose(obs -> StringUtils.equals(jsonLever.getFieldAsString(query, "select"), "count")
						? obs.count()
								.map(count -> new JsonPrimitive(count))
						: obs.map(json -> select(json, query.get("select"))));
	}

	/**
	 * Select.
	 *
	 * @param json the json
	 * @param selectElement the select element
	 * @return the json element
	 */
	private JsonElement select(JsonElement json, JsonElement selectElement) {
		JsonElement result = null;
		String selectStr = jsonLever.getAsString(selectElement);
		if (null != selectStr) {
			if (equalsAny(selectStr, "*"))
				result = json;
		} else {
			if (jsonLever.isJsonArray(selectElement)) {
				result = jsonLever.getSubset(json, selectElement.getAsJsonArray());
			}
		}
		return result;
	}

	/**
	 * Filter.
	 *
	 * @param json the json
	 * @param whereElement the where element
	 * @return the boolean
	 */
	private Boolean filter(JsonElement json, JsonElement whereElement) {
		Boolean filter = Boolean.TRUE;
		JsonArray where = jsonLever.getAsJsonArray(whereElement, new JsonArray());
		for (JsonElement condition : where) {
			if (jsonLever.isString(condition)) {
				String operator = StringUtils.lowerCase(jsonLever.getAsString(condition));
				if (equalsAny(operator, "&", "&&", "and")) {
					if (filter) {
						continue;
					} else {
						return filter;
					}
				}
				if (equalsAny(operator, "|", "||", "or")) {
					if (!filter) {
						continue;
					} else {
						return filter;
					}
				}
			} else if (jsonLever.isJsonObject(condition)) {
				String type = jsonLever.getFieldAsString(condition, "type");
				type = null != type && queryTypes.containsKey(type) ? type : "=";
				filter = queryTypes.get(type)
						.apply(json, condition.getAsJsonObject());
			}
		}
		return filter;
	}

	/**
	 * Equals filter.
	 *
	 * @param json the json
	 * @param condition the condition
	 * @return the boolean
	 */
	private Boolean equalsFilter(JsonElement json, JsonObject condition) {
		return jsonLever.getAsJsonElement(condition.get("value"), JsonNull.INSTANCE)
				.equals(jsonLever.getField(json, jsonLever.getAsJsonArray(condition.get("field"), new JsonArray())));
	}

	/**
	 * Not equals filter.
	 *
	 * @param json the json
	 * @param condition the condition
	 * @return the boolean
	 */
	private Boolean notEqualsFilter(JsonElement json, JsonObject condition) {
		return !equalsFilter(json, condition);
	}

	/**
	 * In filter.
	 *
	 * @param json the json
	 * @param condition the condition
	 * @return the boolean
	 */
	private Boolean inFilter(JsonElement json, JsonObject condition) {
		return jsonLever.getAsJsonArray(condition.get("value"), new JsonArray())
				.contains(jsonLever.getField(json, jsonLever.getAsJsonArray(condition.get("field"), new JsonArray())));
	}

	/**
	 * Notin filter.
	 *
	 * @param json the json
	 * @param condition the condition
	 * @return the boolean
	 */
	private Boolean notinFilter(JsonElement json, JsonObject condition) {
		return !inFilter(json, condition);
	}

	private Boolean equalsAll(String str, String... matches) {
		Boolean equals = Boolean.TRUE;
		for (String string : matches) {
			equals = equals && StringUtils.equals(str, string);
			if (!equals) {
				break;
			}
		}
		return equals;
	}

	private Boolean equalsAny(String str, String... matches) {
		Boolean equals = Boolean.FALSE;
		for (String string : matches) {
			equals = equals || StringUtils.equals(str, string);
			if (equals) {
				break;
			}
		}
		return equals;
	}

	/**
	 * Validate.
	 *
	 * @param query the query
	 * @return the string
	 */
	private String validate(JsonObject query) {
		if (jsonLever.isNull(jsonLever.getField(query, "select"))) {
			return "select field of the query cannot be null";
		}
		JsonElement from = query.get("from");
		if (!(jsonLever.isJsonObject(from) || jsonLever.isJsonArray(from))) {
			return "from field must be a valid json object or array";
		}
		JsonElement where = jsonLever.getField(query, "where");
		if (jsonLever.isNotNull(where) && !jsonLever.isJsonArray(where)) {
			return "where field must be a valid json array";
		}
		JsonElement limit = jsonLever.getField(query, "limit");
		if (jsonLever.isNotNull(limit) && !jsonLever.isLong(limit)) {
			return "limit must be a valid number";
		}
		return null;
	}

}
