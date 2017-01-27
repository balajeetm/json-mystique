/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.balajeetm.mystique.util.gson.bean.lever;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MystResult.
 *
 * @author balajeetm
 */
public class MystResult {

	/** The result. */
	private Boolean result = Boolean.TRUE;

	/** The msgs. */
	private List<String> msgs = new ArrayList<String>();

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public Boolean getResult() {
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(Boolean result) {
		this.result = result;
	}

	/**
	 * Gets the msgs.
	 *
	 * @return the msgs
	 */
	public List<String> getMsgs() {
		return msgs;
	}

	/**
	 * Sets the msgs.
	 *
	 * @param msgs the new msgs
	 */
	public void setMsgs(List<String> msgs) {
		this.msgs = msgs;
	}

	/**
	 * Adds the msg.
	 *
	 * @param msg the msg
	 */
	public void addMsg(String msg) {
		this.msgs.add(msg);
	}
}
