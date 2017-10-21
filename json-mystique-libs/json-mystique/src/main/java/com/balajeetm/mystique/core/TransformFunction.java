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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.balajeetm.mystique.core.util.MystiqueConstants;
import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class TransformFunction.
 *
 * @author balajeetm
 */
public class TransformFunction implements MystiqueFunction {

  /** The json lever. */
  private JsonLever jsonLever;

  /** Instantiates a new transform function. */
  private TransformFunction() {
    jsonLever = JsonLever.getInstance();
  }

  /**
   * Gets the single instance of TransformFunction.
   *
   * @return single instance of TransformFunction
   */
  public static TransformFunction getInstance() {
    return Creator.INSTANCE;
  }

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized
  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static TransformFunction INSTANCE = new TransformFunction();
  }

  /** The logger. */
  protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  /* (non-Javadoc)
   * @see com.futuresight.util.mystique.MystFunction#execute(com.google.gson.JsonElement, com.google.gson.JsonObject)
   */
  @Override
  public JsonElement execute(JsonElement source, JsonObject turn) {
    JsonElement result = JsonNull.INSTANCE;
    if (jsonLever.isNotNull(source)) {
      turn = jsonLever.asJsonObject(turn, new JsonObject());
      String formatIn =
          jsonLever.asString(turn.get(MystiqueConstants.INFORMAT), MystiqueConstants.DEFAULT);
      Date inputDate = null;
      try {
        switch (formatIn) {
          case MystiqueConstants.DEFAULT:
            {
              Long time = jsonLever.asLong(source);
              time = time == null ? Long.valueOf(jsonLever.asString(source)) : time;
              inputDate = new Date(time);
              break;
            }

          case MystiqueConstants.LONG:
            {
              Long time = jsonLever.asLong(source);
              inputDate = new Date(time);
              break;
            }

          case MystiqueConstants.STRING:
            {
              String time = jsonLever.asString(source);
              inputDate = new Date(Long.valueOf(time));
              break;
            }

          default:
            inputDate = new SimpleDateFormat(formatIn).parse(jsonLever.asString(source));
            break;
        }
      } catch (ParseException e) {
        logger.warn(
            String.format(
                "Error while parsing input date %s for format %s : %s",
                source, formatIn, e.getMessage()),
            e);
      }
      String outFormat =
          jsonLever.asString(turn.get(MystiqueConstants.OUTFORMAT), MystiqueConstants.LONG);
      result = getFormattedDate(inputDate, outFormat);
    }
    return result;
  }

  /**
   * Gets the formatted date.
   *
   * @param date the date
   * @param dateFormat the date format
   * @return the formatted date
   */
  public JsonElement getFormattedDate(Date date, String dateFormat) {
    JsonElement output;
    switch (dateFormat) {
      case MystiqueConstants.LONG:
        output = new JsonPrimitive(date.getTime());
        break;

      case MystiqueConstants.STRING:
        output = new JsonPrimitive(String.valueOf(date.getTime()));
        break;

      default:
        output = new JsonPrimitive(new SimpleDateFormat(dateFormat).format(date));
        break;
    }
    return output;
  }
}
