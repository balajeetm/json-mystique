package com.futuresight.util.mystique;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class NowFunction implements MystFunction {

	@Autowired
	private JsonLever jsonLever;

	@Override
	public JsonElement execute(JsonElement source, JsonObject turn) {
		turn = jsonLever.getAsJsonObject(turn, new JsonObject());
		String outFormat = jsonLever.getAsString(turn.get(MysCon.OUTFORMAT), MysCon.LONG);
		return jsonLever.getFormattedDate(new Date(), outFormat);
	}

}
