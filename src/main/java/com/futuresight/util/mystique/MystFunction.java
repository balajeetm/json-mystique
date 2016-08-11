package com.futuresight.util.mystique;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface MystFunction {
	JsonElement execute(JsonElement source, JsonObject turn);
}
