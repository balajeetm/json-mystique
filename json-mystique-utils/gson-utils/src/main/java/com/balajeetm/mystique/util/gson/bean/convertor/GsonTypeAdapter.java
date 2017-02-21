/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.bean.convertor;

import java.lang.reflect.Type;

import lombok.Data;

/**
 * The Class GsonTypeAdapter.
 *
 * @author balajeetm
 */

/**
 * Instantiates a new gson type adapter.
 */
@Data
public class GsonTypeAdapter {

	/** The type. */
	private Type type;

	/** The adapter. */
	private Object adapter;

}
