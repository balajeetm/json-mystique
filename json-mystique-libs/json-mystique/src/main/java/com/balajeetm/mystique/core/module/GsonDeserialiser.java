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

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The Class GsonDeserialiser.
 *
 * @author balajeetm
 * @param <T> the generic type
 */
public class GsonDeserialiser<T extends JsonElement> extends StdDeserializer<T> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** Instantiates a new gson deserialiser. */
  public GsonDeserialiser() {
    this(JsonElement.class);
  }

  /**
   * Instantiates a new gson deserialiser.
   *
   * @param vc the vc
   */
  public GsonDeserialiser(Class<?> vc) {
    super(vc);
  }

  /* (non-Javadoc)
   * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
   */
  @Override
  public T deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = p.getCodec().readTree(p);
    return deserialize(node);
  }

  /**
   * Deserialize.
   *
   * @param node the node
   * @return the json element
   */
  @SuppressWarnings("unchecked")
  private T deserialize(JsonNode node) {
    JsonElement result = JsonNull.INSTANCE;
    if (null != node && !node.isNull()) {
      if (node.isObject()) {
        ObjectNode onode = (ObjectNode) node;
        JsonObject jsonObject = new JsonObject();
        Iterator<Entry<String, JsonNode>> fields = onode.fields();
        while (fields.hasNext()) {
          Entry<String, JsonNode> next = fields.next();
          jsonObject.add(next.getKey(), deserialize(next.getValue()));
        }
        result = jsonObject;
      } else if (node.isArray()) {
        ArrayNode anode = (ArrayNode) node;
        JsonArray jsonArray = new JsonArray();
        Iterator<JsonNode> elements = anode.elements();
        while (elements.hasNext()) {
          jsonArray.add(deserialize(elements.next()));
        }
        result = jsonArray;
      } else if (node.isBoolean()) {
        result = new JsonPrimitive(node.asBoolean());
      } else if (node.isNumber()) {
        NumericNode nnode = (NumericNode) node;
        result = new JsonPrimitive(nnode.numberValue());
      } else if (node.isTextual()) {
        TextNode tnode = (TextNode) node;
        result = new JsonPrimitive(tnode.textValue());
      }
    }

    return (T) result;
  }
}
