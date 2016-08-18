/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 18 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique.lever;

import java.lang.reflect.Type;

import lombok.Data;

/**
 * The Class GsonTypeAdapter.
 *
 * @author balajmoh
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
