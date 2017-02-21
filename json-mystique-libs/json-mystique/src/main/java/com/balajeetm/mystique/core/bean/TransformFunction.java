/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.balajeetm.mystique.core.bean.lever.MystiqueLever;
import com.balajeetm.mystique.util.gson.bean.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

/**
 * The Class TransformFunction.
 *
 * @author balajeetm
 */
@Component
public class TransformFunction implements MystFunction {

	/** The json lever. */
	@Autowired
	private MystiqueLever jsonLever;

	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.MystFunction#execute(com.google.gson.JsonElement, com.google.gson.JsonObject)
	 */
	@Override
	public JsonElement execute(JsonElement source, JsonObject turn) {
		JsonElement result = JsonNull.INSTANCE;
		if (jsonLever.isNotNull(source)) {
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
					inputDate = new SimpleDateFormat(formatIn).parse(jsonLever.getAsString(source));
					break;

				}
			} catch (ParseException e) {
				logger.warn(
						String.format("Error while parsing input date %s for format %s : %s", source, formatIn,
								e.getMessage()),
						e);
			}
			String outFormat = jsonLever.getAsString(turn.get(MysCon.OUTFORMAT), MysCon.LONG);
			result = jsonLever.getFormattedDate(inputDate, outFormat);
		}
		return result;
	}
}
