package com.futuresight.util.mystique;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class AbstractMystique implements SimpleMystique {

	protected static Gson gson = new Gson();

	public JsonElement transform(List<JsonElement> source, JsonObject deps, JsonObject turn) {
		JsonElement transform = transmute(source, deps, turn);
		if (null == transform && null != turn) {
			transform = turn.get("default");
		}
		return transform;
	}

	protected abstract JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject turn);
}
