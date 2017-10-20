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

import java.util.Date;

import com.balajeetm.mystique.core.util.MystiqueConstants;
import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class NowFunction.
 *
 * @author balajeetm
 */
public class NowFunction implements MystiqueFunction {

  /** The json lever. */
  private JsonLever jsonLever;

  /**
   * Gets the single instance of NowFunction.
   *
   * @return single instance of NowFunction
   */
  public static NowFunction getInstance() {
    return Creator.INSTANCE;
  }

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized
  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static NowFunction INSTANCE = new NowFunction();
  }

  /** Instantiates a new now function. */
  private NowFunction() {
    jsonLever = JsonLever.getInstance();
  }

  /* (non-Javadoc)
   * @see com.futuresight.util.mystique.MystFunction#execute(com.google.gson.JsonElement, com.google.gson.JsonObject)
   */
  @Override
  public JsonElement execute(JsonElement source, JsonObject turn) {
    turn = jsonLever.asJsonObject(turn, new JsonObject());
    String outFormat =
        jsonLever.asString(turn.get(MystiqueConstants.OUTFORMAT), MystiqueConstants.LONG);
    return TransformFunction.getInstance().getFormattedDate(new Date(), outFormat);
  }
}
