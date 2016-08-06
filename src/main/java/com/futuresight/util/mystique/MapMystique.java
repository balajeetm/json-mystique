package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.JsonJacksonConvertor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class MapMystique extends AbstractMystique {

	@Autowired
	private JsonLever jsonLever;

	@Autowired
	private JsonJacksonConvertor convertor;

	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		JsonObject mapJson = new JsonObject();
		if (CollectionUtils.isNotEmpty(source)) {
			JsonArray jsonArray = source.get(0).getAsJsonArray();

			JsonElement keyObject = turn.get("key");
			if (null != keyObject) {
				JsonElement valueObject = turn.get("value");
				valueObject = (null == valueObject) ? new JsonArray() : valueObject;
				for (JsonElement jsonElement : jsonArray) {
					JsonElement keyField = jsonLever.getField(jsonElement, keyObject.getAsJsonArray());
					String key = (null != keyField && keyField.isJsonPrimitive()) ? StringUtils.trimToEmpty(keyField
							.getAsString()) : null;
					JsonElement valueField = jsonLever.getField(jsonElement, valueObject.getAsJsonArray());
					mapJson.add(key, valueField);
				}
			}
		}
		return mapJson;
	}
}
