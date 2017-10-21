/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 20 Oct, 2017 by balajeemohan
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * A factory for creating Gson objects.
 *
 * @author balajeemohan
 */
public class GsonFactory {

  /** The json parser. */
  private JsonParser jsonParser;

  /** The gson. */
  private Gson gson;

  /** The gson builder. */
  private GsonBuilder gsonBuilder;

  /**
   * Gets the single instance of GsonFactory.
   *
   * @return single instance of GsonFactory
   */
  public static GsonFactory getInstance() {
    return Creator.INSTANCE;
  }

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized
  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static GsonFactory INSTANCE = new GsonFactory();
  }

  /** Instantiates a new gson factory. */
  private GsonFactory() {}

  /**
   * Gets the json parser.
   *
   * @return the json parser
   */
  public JsonParser getJsonParser() {
    if (null == jsonParser) {
      jsonParser = new JsonParser();
    }
    return jsonParser;
  }

  /**
   * Gets the gson.
   *
   * @return the gson
   */
  public Gson getGson() {
    if (null == gson) {
      gson = getGsonBuilder().create();
    }
    return gson;
  }

  /**
   * Gets the gson builder.
   *
   * @return the gson builder
   */
  public GsonBuilder getGsonBuilder() {
    if (null == gsonBuilder) {
      gsonBuilder = new GsonBuilder();
      gsonBuilder.setDateFormat(DateFormat.LONG, DateFormat.LONG);
      gsonBuilder.registerTypeAdapter(
          Date.class,
          new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(
                JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
              Date date = null;
              if (null != json && json.isJsonPrimitive()) {
                date = new Date(json.getAsJsonPrimitive().getAsLong());
              }
              return date;
            }
          });
      gsonBuilder.registerTypeAdapter(
          XMLGregorianCalendar.class,
          new JsonSerializer<XMLGregorianCalendar>() {

            @Override
            public JsonElement serialize(
                XMLGregorianCalendar src, Type typeOfSrc, JsonSerializationContext context) {
              Date date = null;
              if (null != src) {
                date = src.toGregorianCalendar().getTime();
              }
              return new JsonPrimitive(
                  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date));
            }
          });
    }
    return gsonBuilder;
  }
}
