package com.futuresight.util.mystique;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class MultiMystique implements Mystique {

	@Autowired
	private MystiqueFactory factory;

	@Override
	public JsonElement transform(List<JsonElement> source, JsonObject deps, JsonElement turn) {
		JsonArray jsonArray = turn.getAsJsonArray();
		JsonElement transform = null;
		for (JsonElement object : jsonArray) {
			Mystique mystique = factory.getMystique(object);
			transform = mystique.transform(source, deps, object);
			if (null != transform) {
				break;
			}
		}
		return transform;
	}

}
