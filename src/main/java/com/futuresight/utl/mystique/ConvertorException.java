/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.utl.mystique;

/**
 * The Class ConvertorException.
 *
 * @author balajeetm
 */
public class ConvertorException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new convertor exception.
	 * 
	 * @param cause the cause
	 */
	public ConvertorException(Throwable cause) {
		super(String.format("Error during conversion : %s", cause.getMessage()));
		this.initCause(cause);
	}

	/**
	 * Instantiates a new convertor exception.
	 * 
	 * @param message the message
	 */
	public ConvertorException(String message) {
		super(String.format("Error during conversion : %s", message));
	}
}
