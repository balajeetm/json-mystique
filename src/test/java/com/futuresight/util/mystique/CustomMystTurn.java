package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Component
public class CustomMystTurn extends AbstractMystTurn {

	@Override
	protected JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
		StringBuilder stringBuilder = new StringBuilder();
		if (CollectionUtils.isNotEmpty(source)) {
			String separator = "~";
			for (int count = 0; count < source.size(); count++) {
				JsonElement granularSource = getGranularSource(source.get(count), turn, deps, aces);
				if (count != 0) {
					stringBuilder.append(separator);
				}
				stringBuilder.append(jsonLever.getAsString(granularSource, MysCon.EMPTY));
			}
		}
		return new JsonPrimitive(stringBuilder.toString());
	}

}
