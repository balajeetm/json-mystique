package com.futuresight.util.mystique;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class TransformFunction implements MystFunction {

	@Autowired
	private JsonLever jsonLever;

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public JsonElement execute(JsonElement source, JsonObject turn) {
		turn = jsonLever.getAsJsonObject(turn, new JsonObject());
		String formatIn = jsonLever.getAsString(turn.get(MysCon.INFORMAT), MysCon.DEFAULT);
		Date inputDate = null;
		try {
			switch (formatIn) {
			case MysCon.DEFAULT: {
				Long time = jsonLever.getAsLong(source);
				time = time == null ? Long.valueOf(jsonLever.getAsString(source)) : time;
				inputDate = new Date(time);
				break;
			}

			case MysCon.LONG: {
				Long time = jsonLever.getAsLong(source);
				inputDate = new Date(time);
				break;
			}

			case MysCon.STRING: {
				String time = jsonLever.getAsString(source);
				inputDate = new Date(Long.valueOf(time));
				break;
			}

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
		String outFormat = jsonLever.getAsString(turn.get(MysCon.OUTFORMAT), MysCon.LONG);
		return jsonLever.getFormattedDate(inputDate, outFormat);
	}
}
