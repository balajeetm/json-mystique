package com.futuresight.util.mystique;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface SimpleMystique extends Mystique {

	@Override
	default JsonElement transform(List<JsonElement> source, JsonObject deps, JsonElement turn) {
		JsonObject turnObject = (null == turn) ? null : turn.getAsJsonObject();
		return transform(source, deps, turnObject);
	}

	JsonElement transform(List<JsonElement> source, JsonObject deps, JsonObject turn);

}
