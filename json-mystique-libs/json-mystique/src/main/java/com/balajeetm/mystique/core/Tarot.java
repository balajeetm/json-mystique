/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.Data;

/**
 * The Class Tarot.
 *
 * @author balajeetm
 */

/** Instantiates a new tarot. */
@Data
public class Tarot {

  /** The from. */
  private JsonElement from;

  /** The to. */
  private JsonElement to;

  /** The turn. */
  private JsonObject turn;

  /** The optional. */
  private Boolean optional = Boolean.FALSE;

  /** The deps. */
  private List<Tarot> deps;

  /**
   * The aces. Is a map of turns. More like pre-processing of data for efficiency Each field in the
   * json object corresponds to a turn, which is executed and saved in the ace
   */
  private JsonObject aces;
}
