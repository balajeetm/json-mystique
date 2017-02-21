/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 19 Sep, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core.test.bean;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.balajeetm.mystique.core.bean.AbstractMystTurn;
import com.balajeetm.mystique.util.gson.bean.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class CustomMystTurn.
 *
 * @author balajeetm
 */
@Component
public class CustomMystTurn extends AbstractMystTurn {

	public CustomMystTurn() {
		System.out.println("A");
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.AbstractMystTurn#transmute(java.util.List, com.google.gson.JsonObject, com.google.gson.JsonObject, com.google.gson.JsonObject)
	 */
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
				stringBuilder.append(mystiqueLever.getAsString(granularSource, MysCon.EMPTY));
			}
		}
		return new JsonPrimitive(stringBuilder.toString());
	}

}
