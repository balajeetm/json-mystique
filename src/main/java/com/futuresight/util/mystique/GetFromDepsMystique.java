package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.JsonJacksonConvertor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class GetFromDepsMystique extends AbstractMystique {

	@Autowired
	private JsonLever jsonLever;

	@Autowired
	private JsonJacksonConvertor convertor;

	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {

		JsonElement transform = null;
		if (CollectionUtils.isNotEmpty(source)) {
			//key1
			JsonElement elementSource = source.get(0);

			JsonElement key = turn.get("key");
			JsonElement value = turn.get("value");
			JsonArray keyPath = null == key ? new JsonArray() : key.getAsJsonArray();
			JsonArray valuePath = null == value ? new JsonArray() : value.getAsJsonArray();

			//keymap
			JsonElement field = jsonLever.getField(deps, keyPath);
			JsonObject map = (null == field) ? null : field.getAsJsonObject();

			JsonElement actualElement = map.get(elementSource.getAsString());
			transform = jsonLever.getField(actualElement, valuePath);
		}
		return transform;
	}
}
