/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique.lever;

import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.JsonLever;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The Class JsonComparator.
 */
/**
 * @author balajeetm
 */
@Component
public class JsonComparator {

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

	/**
	 * Gets the single instance of JsonComparator.
	 *
	 * @return single instance of JsonComparator
	 */
	public static JsonComparator getInstance() {
		return Creator.INSTANCE;
	}

	// Efficient Thread safe Lazy Initialization
	// works only if the singleton constructor is non parameterized
	// Works non spring as well
	/**
	 * The Class Creator.
	 */
	private static class Creator {

		/** The instance. */
		public static JsonComparator INSTANCE = new JsonComparator();
	}

	/**
	 * Instantiates a new json comparator.
	 */
	private JsonComparator() {
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	private void init() {
		Creator.INSTANCE = this;
	}

	/**
	 * Checks if is subset.
	 *
	 * @param subsetStr the subset str
	 * @param actualStr the actual str
	 * @return the myst result
	 */
	public MystResult isSubset(String subsetStr, String actualStr) {
		MystResult result = new MystResult();
		JsonParser jsonParser = jsonLever.getJsonParser();
		try {
			JsonElement actual = jsonParser.parse(actualStr);
			JsonElement subset = jsonParser.parse(subsetStr);
			isSubset("root", subset, actual, result);
		}
		catch (RuntimeException e) {
			result.setResult(Boolean.FALSE);
			result.addMsg(String.format("Exception % s occured", e.getMessage()));
		}
		return result;
	}

	/**
	 * Checks if is subset.
	 *
	 * @param subset the subset
	 * @param actual the actual
	 * @return the myst result
	 */
	public MystResult isSubset(JsonElement subset, JsonElement actual) {
		MystResult result = new MystResult();
		try {
			isSubset("root", subset, actual, result);
		}
		catch (RuntimeException e) {
			result.setResult(Boolean.FALSE);
			result.addMsg(String.format("Exception % s occured", e.getMessage()));
		}
		return result;
	}

	/**
	 * Checks if is subset.
	 *
	 * @param tag the tag
	 * @param subset the subset
	 * @param actual the actual
	 * @param result the result
	 * @return the myst result
	 */
	private MystResult isSubset(String tag, JsonElement subset, JsonElement actual, MystResult result) {
		subset = jsonLever.getAsJsonElement(subset, JsonNull.INSTANCE);
		actual = jsonLever.getAsJsonElement(actual, JsonNull.INSTANCE);
		if (jsonLever.isNotNull(subset) && jsonLever.isNull(actual)) {
			result.setResult(Boolean.FALSE);
			result.addMsg(String.format("The field %s of actual is null", tag));
		}

		else if (!subset.getClass().getCanonicalName().equals(actual.getClass().getCanonicalName())) {
			result.setResult(Boolean.FALSE);
			result.addMsg(String.format("The field %s of expected and actual are not of the same type", tag));
		}
		else {
			if (subset.isJsonObject()) {
				JsonObject subJson = jsonLever.getAsJsonObject(subset);
				JsonObject actJson = jsonLever.getAsJsonObject(actual);
				Set<Entry<String, JsonElement>> entrySet = subJson.entrySet();
				for (Entry<String, JsonElement> entry : entrySet) {
					String key = entry.getKey();
					JsonElement value = entry.getValue();
					JsonElement actualValue = actJson.get(key);
					isSubset(key, value, actualValue, result);
				}
			}
			else if (subset.isJsonArray()) {
				JsonArray subJson = jsonLever.getAsJsonArray(subset);
				JsonArray actJson = jsonLever.getAsJsonArray(actual);
				if (subJson.size() != actJson.size()) {
					result.setResult(Boolean.FALSE);
					result.addMsg(String.format("The field %s of expected and actual are not of same size", tag));

				}
				else {
					for (int i = 0; i < subJson.size(); i++) {
						isSubset(tag, subJson.get(i), actJson.get(i), result);
					}
				}

			}
			else {
				if (!subset.equals(actual)) {
					result.setResult(Boolean.FALSE);
					result.addMsg(String.format("The field %s of expected and actual are not same", tag));
				}
			}
		}

		return result;
	}
}
