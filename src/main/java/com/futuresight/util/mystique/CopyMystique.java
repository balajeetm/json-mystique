/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;

/**
 * The Class CopyMystique.
 *
 * @author balajeetm
 */
@Component
public class CopyMystique implements Mystique {

	/* (non-Javadoc)
	 * @see com.futuresight.utl.mystique.Mystique#transform(java.util.List)
	 */
	@Override
	public JsonElement transform(List<JsonElement> from) {
		JsonElement element = null;
		if (CollectionUtils.isNotEmpty(from)) {
			element = from.get(0);
		}
		return element;
	}
}
