/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.util.List;

import lombok.Data;

import com.google.gson.JsonElement;

/**
 * The Class Tarot.
 *
 * @author balajeetm
 */

/**
 * Instantiates a new tarot.
 */
@Data
public class TestTarot {

	/** The from. */
	private List<List<String>> from;

	/** The to. */
	private List<String> to;

	/** The turn. */
	private JsonElement turn;

	/** The deps. */
	private List<TestTarot> deps;

}
