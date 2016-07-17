/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.utl.mystique;

import java.util.List;

/**
 * The Class Turn.
 *
 * @author balajeetm
 */
public class Turn {

	/** The to. */
	private List<String> to;

	/** The from. */
	private List<List<String>> from;

	/** The convertor. */
	private String convertor;

	/**
	 * Gets the to.
	 *
	 * @return the to
	 */
	public List<String> getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 *
	 * @param to the new to
	 */
	public void setTo(List<String> to) {
		this.to = to;
	}

	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public List<List<String>> getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 *
	 * @param from the new from
	 */
	public void setFrom(List<List<String>> from) {
		this.from = from;
	}

	/**
	 * Gets the convertor.
	 *
	 * @return the convertor
	 */
	public String getConvertor() {
		return convertor;
	}

	/**
	 * Sets the convertor.
	 *
	 * @param convertor the new convertor
	 */
	public void setConvertor(String convertor) {
		this.convertor = convertor;
	}
}
