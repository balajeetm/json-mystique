package com.futuresight.util.mystique;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Component
public class TransformFunction implements MystFunction {

	@Autowired
	private JsonLever jsonLever;

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public JsonElement execute(JsonElement source, JsonObject turn) {
		turn = jsonLever.isNotNull(turn) && turn.isJsonObject() ? turn : new JsonObject();
		JsonElement inFormat = turn.get("informat");
		String formatIn = jsonLever.isNotNull(inFormat) && inFormat.isJsonPrimitive() ? StringUtils.trim(inFormat
				.getAsString()) : "default";
		Date inputDate = null;
		try {
			switch (formatIn) {
			case "default":
				JsonPrimitive asJsonPrimitive = source.getAsJsonPrimitive();
				Long time = asJsonPrimitive.isNumber() ? asJsonPrimitive.getAsLong() : Long.valueOf(asJsonPrimitive
						.getAsString());
				inputDate = new Date(time);
				break;

			default:
				inputDate = new SimpleDateFormat(formatIn).parse(source.getAsString());
				break;

			}
		}
		catch (ParseException e) {
			logger.warn(
					String.format("Error while parsing input date %s for format %s : %s", source, formatIn,
							e.getMessage()), e);
		}
		JsonElement outFormat = turn.get("outformat");
		return jsonLever.getFormattedDate(inputDate, outFormat);
	}
}
