/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 9 Oct, 2016 by balajeetm
 */
package com.futuresight.util.mystique.lever;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.futuresight.util.mystique.JsonLever;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LazilyParsedNumber;

/**
 * The Class GsonSerialiser.
 *
 * @author balajeetm
 */
@Component
public class GsonSerialiser extends StdSerializer<JsonElement> {

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

	/**
	 * Instantiates a new gson serialiser.
	 */
	public GsonSerialiser() {
		this(JsonElement.class);
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
	public void serialize(JsonElement value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (jsonLever.isNull(value)) {
			gen.writeNull();
		}
		else if (jsonLever.isJsonObject(value)) {
			gen.writeStartObject();
			JsonObject jsonObject = value.getAsJsonObject();
			for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
				gen.writeFieldName(entry.getKey());
				serialize(entry.getValue(), gen, provider);
			}
			gen.writeEndObject();
		}
		else if (jsonLever.isJsonArray(value)) {
			gen.writeStartArray();
			JsonArray jsonArray = value.getAsJsonArray();
			for (JsonElement jsonElement : jsonArray) {
				serialize(jsonElement, gen, provider);
			}
			gen.writeEndArray();
		}
		else if (jsonLever.isJsonPrimitive(value)) {
			JsonPrimitive jsonPrimitive = value.getAsJsonPrimitive();
			if (jsonPrimitive.isBoolean()) {
				gen.writeBoolean(jsonPrimitive.getAsBoolean());
			}
			if (jsonPrimitive.isNumber()) {
				Number nnode = jsonPrimitive.getAsNumber();
				if (nnode instanceof LazilyParsedNumber) {
					gen.writeNumber(nnode.toString());
				}
				else if (nnode instanceof Integer) {
					gen.writeNumber(nnode.intValue());
				}
				else if (nnode instanceof Short) {
					gen.writeNumber(nnode.shortValue());
				}
				else if (nnode instanceof BigInteger || nnode instanceof Long) {
					gen.writeNumber(nnode.longValue());
				}
				else if (nnode instanceof Float) {
					gen.writeNumber(nnode.floatValue());
				}
				else if (nnode instanceof Double || nnode instanceof BigDecimal) {
					gen.writeNumber(nnode.doubleValue());
				}
			}
			if (jsonPrimitive.isString()) {
				gen.writeString(jsonPrimitive.getAsString());
			}
		}
	}

}
