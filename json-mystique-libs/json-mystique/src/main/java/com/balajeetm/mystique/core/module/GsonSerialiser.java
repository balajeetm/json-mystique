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

import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map.Entry;

/**
 * The Class GsonSerialiser.
 *
 * @author balajeetm
 */
public class GsonSerialiser extends StdSerializer<JsonElement> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The json lever. */
  private JsonLever jsonLever;

  /** Instantiates a new gson serialiser. */
  public GsonSerialiser() {
    this(JsonElement.class);
    jsonLever = JsonLever.getInstance();
  }

  /**
   * Instantiates a new gson serialiser.
   *
   * @param t the t
   */
  public GsonSerialiser(Class<JsonElement> t) {
    super(t, true);
  }

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
   */
  @Override
  public void serialize(JsonElement value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    if (jsonLever.isNull(value)) {
      gen.writeNull();
    } else if (jsonLever.isObject(value)) {
      gen.writeStartObject();
      JsonObject jsonObject = value.getAsJsonObject();
      for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
        gen.writeFieldName(entry.getKey());
        serialize(entry.getValue(), gen, provider);
      }
      gen.writeEndObject();
    } else if (jsonLever.isArray(value)) {
      gen.writeStartArray();
      JsonArray jsonArray = value.getAsJsonArray();
      for (JsonElement jsonElement : jsonArray) {
        serialize(jsonElement, gen, provider);
      }
      gen.writeEndArray();
    } else if (jsonLever.isPrimitive(value)) {
      JsonPrimitive jsonPrimitive = value.getAsJsonPrimitive();
      if (jsonPrimitive.isBoolean()) {
        gen.writeBoolean(jsonPrimitive.getAsBoolean());
      } else if (jsonPrimitive.isNumber()) {
        Number nnode = jsonPrimitive.getAsNumber();
        if (nnode instanceof LazilyParsedNumber) {
          gen.writeNumber(nnode.toString());
        } else if (nnode instanceof Integer) {
          gen.writeNumber(nnode.intValue());
        } else if (nnode instanceof Short) {
          gen.writeNumber(nnode.shortValue());
        } else if (nnode instanceof BigInteger || nnode instanceof Long) {
          gen.writeNumber(nnode.longValue());
        } else if (nnode instanceof Float) {
          gen.writeNumber(nnode.floatValue());
        } else if (nnode instanceof Double || nnode instanceof BigDecimal) {
          gen.writeNumber(nnode.doubleValue());
        }
      } else if (jsonPrimitive.isString()) {
        gen.writeString(jsonPrimitive.getAsString());
      }
    }
  }
}
