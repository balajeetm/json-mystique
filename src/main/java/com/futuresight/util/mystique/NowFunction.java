/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 18 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class NowFunction.
 *
 * @author balajmoh
 */
@Component
public class NowFunction implements MystFunction {

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.MystFunction#execute(com.google.gson.JsonElement, com.google.gson.JsonObject)
	 */
	@Override
	public JsonElement execute(JsonElement source, JsonObject turn) {
		turn = jsonLever.getAsJsonObject(turn, new JsonObject());
		String outFormat = jsonLever.getAsString(turn.get(MysCon.OUTFORMAT), MysCon.LONG);
		return jsonLever.getFormattedDate(new Date(), outFormat);
	}

}
