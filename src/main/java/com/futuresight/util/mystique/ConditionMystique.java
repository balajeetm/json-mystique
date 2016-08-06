package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.JsonJacksonConvertor;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Component
public class ConditionMystique extends AbstractMystique {

	@Autowired
	private JsonLever jsonLever;

	@Autowired
	private JsonJacksonConvertor convertor;

	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		JsonElement transform = null;
		JsonElement elementSource = null;
		if (CollectionUtils.isNotEmpty(source)) {
			//key1
			elementSource = source.get(0);
		}
		JsonElement value = turn.get("value");
		Boolean equals = isEquals(elementSource, value);
		JsonElement jsonElement = turn.get(String.valueOf(equals));
		if (null == jsonElement) {
			jsonElement = new JsonPrimitive(equals);
		}
		transform = jsonElement;
		return transform;
	}

	private Boolean isEquals(JsonElement var1, JsonElement var2) {
		Boolean isEqual = Boolean.FALSE;

		if (var1 == null && var2 == null) {
			isEqual = Boolean.TRUE;
		}
		else if (var1 != null) {
			isEqual = var1.equals(var2);
		}

		return isEqual;
	}
}
