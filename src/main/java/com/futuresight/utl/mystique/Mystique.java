/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.utl.mystique;

import java.util.List;

import com.google.gson.JsonElement;

/**
 * The Interface Mystique.
 *
 * @author balajeetm
 */
public interface Mystique {

	/**
	 * Transform.
	 *
	 * @param from the from
	 * @return the json element
	 */
	JsonElement transform(List<JsonElement> from);

	/**
	 * The Enum MystiqueType.
	 */
	public enum MystiqueType {

		/** The copy. */
		COPY,
		/** The concat. */
		CONCAT
	}

}
