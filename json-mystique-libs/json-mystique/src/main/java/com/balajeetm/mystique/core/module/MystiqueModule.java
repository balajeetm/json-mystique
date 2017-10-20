/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 9 Oct, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core.module;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class MystiqueModule.
 *
 * @author balajeetm
 */
public class MystiqueModule extends SimpleModule {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The gson deserialiser. */
  @SuppressWarnings("rawtypes")
  private GsonDeserialiser gsonDeserialiser;

  /** The gson serialiser. */
  private GsonSerialiser gsonSerialiser;

  /** Instantiates a new mystique module. */
  public MystiqueModule() {
    gsonDeserialiser = new GsonDeserialiser<>();
    gsonSerialiser = new GsonSerialiser();
    init();
  }

  /** Inits the. */
  @SuppressWarnings("unchecked")
  private void init() {
    this.addDeserializer(JsonElement.class, gsonDeserialiser);
    this.addSerializer(JsonElement.class, gsonSerialiser);
    this.addDeserializer(JsonObject.class, gsonDeserialiser);
    this.addSerializer(JsonObject.class, gsonSerialiser);
    this.addDeserializer(JsonArray.class, gsonDeserialiser);
    this.addSerializer(JsonArray.class, gsonSerialiser);
    this.addDeserializer(JsonPrimitive.class, gsonDeserialiser);
    this.addSerializer(JsonPrimitive.class, gsonSerialiser);
    this.addDeserializer(JsonNull.class, gsonDeserialiser);
    this.addSerializer(JsonNull.class, gsonSerialiser);
  }
}
