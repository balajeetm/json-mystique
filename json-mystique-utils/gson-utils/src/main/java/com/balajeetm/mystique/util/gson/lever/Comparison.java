/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.lever;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * The Class MystResult.
 *
 * @author balajeetm
 */

/** Instantiates a new comparison. */
@Data
public class Comparison {

  /** The result. */
  private Boolean result = Boolean.TRUE;

  /** The msgs. */
  private List<String> msgs = new ArrayList<String>();

  /**
   * Adds the msg.
   *
   * @param msg the msg
   */
  public void addMsg(String msg) {
    this.msgs.add(msg);
  }
}
