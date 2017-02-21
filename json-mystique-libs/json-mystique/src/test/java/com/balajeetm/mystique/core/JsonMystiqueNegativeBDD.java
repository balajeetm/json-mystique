/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 17 Nov, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The Class JsonMystiqueNegativeBDD.
 *
 * @author balajeetm
 */
@Ignore
public class JsonMystiqueNegativeBDD extends JsonMystiquePositiveBDD {

	/**
	 * Instantiates a new json mystique negative bdd test.
	 */
	public JsonMystiqueNegativeBDD() {
	}

	/**
	 * Inits the.
	 */
	@Before
	public void init() {
	}

	/**
	 * Null date transform17.
	 */
	@Test
	public void NullDateTransform17() {
		testEqual("17_-ve_NullDateTransform");
	}
}
