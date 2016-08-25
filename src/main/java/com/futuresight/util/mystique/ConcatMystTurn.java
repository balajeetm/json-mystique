/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class ConcatMystTurn.
 *
 * @author balajmoh
 */
@Component
public class ConcatMystTurn extends AbstractMystTurn {

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.Mystique#transform(java.util.List, com.google.gson.JsonObject, java.lang.String)
	 */
	@Override
	public JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn) {
		StringBuilder stringBuilder = new StringBuilder();
		if (CollectionUtils.isNotEmpty(source)) {
			String separator = jsonLever.getAsString(turn.get(MysCon.SEPARATOR), MysCon.EMPTY);
			for (int count = 0; count < source.size(); count++) {
				JsonElement granularSource = getGranularSource(source.get(count), turn, deps, aces);
				if (count != 0) {
					stringBuilder.append(separator);
				}
				stringBuilder.append(StringUtils.strip(granularSource.toString(), MysCon.DOUBLE_QUOTES));
			}
		}
		return new JsonPrimitive(stringBuilder.toString());
	}

}
