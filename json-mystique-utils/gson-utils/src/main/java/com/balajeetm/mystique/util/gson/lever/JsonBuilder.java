package com.balajeetm.mystique.util.gson.lever;

import com.balajeetm.mystique.util.gson.convertor.GsonConvertor;
import com.balajeetm.mystique.util.json.convertor.JsonConvertor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

// TODO: Auto-generated Javadoc
/**
 * The Class JsonBuilder.
 *
 * @author Balajee Mohan
 *     <p>Non thread safe json builder
 * @param <T> the generic type
 */
/**
 * @author Balajee Mohan
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class JsonBuilder<T extends JsonElement> {

  /** The lever. */
  JsonLever lever;

  /** The convertor. */
  JsonConvertor convertor = GsonConvertor.getInstance();

  /** The source. */
  T source;

  /**
   * Instantiates a new json builder.
   *
   * @param source the source
   */
  public JsonBuilder(T source) {
    this.source = source;
    this.lever = JsonLever.getInstance();
  }

  /**
   * Json object.
   *
   * @return the json builder
   */
  public static JsonBuilder<JsonObject> jsonObject() {
    return new JsonBuilder<>(new JsonObject());
  }

  /**
   * Sets the.
   *
   * @param jpath the jpath
   * @param value the value
   * @return the json builder
   */
  public JsonBuilder<T> set(String jpath, Object value) {
    source = (T) lever.set(source, jpath, convertor.deserialize(value, JsonElement.class));
    return this;
  }

  /**
   * Sets the json element at the specified jpath.
   *
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a.b.c.1", 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Strings that can be
   *     casted to numerals are presumed to be array indexes
   * @param value the value
   * @return the json builder
   */
  public JsonBuilder<T> set(String jpath, JsonElement value) {
    source = (T) lever.set(source, jpath, value);
    return this;
  }

  /**
   * Sets the string at the specified jpath.
   *
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a.b.c.1", "5"]) is {'a': {'b': {'c': [1, "5", 3, 4]}}}. Strings that can be
   *     casted to numerals are presumed to be array indexes
   * @param value the value
   * @return the json builder
   */
  public JsonBuilder<T> set(String jpath, String value) {
    source = (T) lever.set(source, jpath, value);
    return this;
  }

  /**
   * Sets the string at the specified jpath.
   *
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a.b.c.1", true]) is {'a': {'b': {'c': [1, true, 3, 4]}}}. Strings that can be
   *     casted to numerals are presumed to be array indexes
   * @param value the value
   * @return the json builder
   */
  public JsonBuilder<T> set(String jpath, Boolean value) {
    source = (T) lever.set(source, jpath, value);
    return this;
  }

  /**
   * Sets the string at the specified jpath.
   *
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a.b.c.1", 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Strings that can be
   *     casted to numerals are presumed to be array indexes
   * @param value the value
   * @return the json builder
   */
  public JsonBuilder<T> set(String jpath, Number value) {
    source = (T) lever.set(source, jpath, value);
    return this;
  }

  /**
   * Sets the json element at the specified jpath.
   *
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @param value the value
   * @return the json builder
   */
  public JsonBuilder<T> set(JsonArray jpath, Object value) {
    source = (T) lever.set(source, jpath, convertor.deserialize(value, JsonElement.class));
    return this;
  }

  /**
   * Sets the json element at the specified jpath.
   *
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, ["a", "b" "c", 1, 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Array indexes need
   *     to be specified as numerals. Strings are always presumed to be field names.
   * @param value the value
   * @return the json builder
   */
  public JsonBuilder<T> set(JsonArray jpath, JsonElement value) {
    source = (T) lever.set(source, jpath, value);
    return this;
  }

  /**
   * Sets the string at the specified jpath.
   *
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a.b.c.1", "5"]) is {'a': {'b': {'c': [1, "5", 3, 4]}}}. Strings that can be
   *     casted to numerals are presumed to be array indexes
   * @param value the value
   * @return the json builder
   */
  public JsonBuilder<T> set(JsonArray jpath, String value) {
    source = (T) lever.set(source, jpath, value);
    return this;
  }

  /**
   * Sets the string at the specified jpath.
   *
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a.b.c.1", true]) is {'a': {'b': {'c': [1, true, 3, 4]}}}. Strings that can be
   *     casted to numerals are presumed to be array indexes
   * @param value the value
   * @return the json builder
   */
  public JsonBuilder<T> set(JsonArray jpath, Boolean value) {
    source = (T) lever.set(source, jpath, value);
    return this;
  }

  /**
   * Sets the string at the specified jpath.
   *
   * @param jpath the fully qualified json path to the field required. eg set({'a': {'b': {'c': [1,
   *     2, 3, 4]}}}, "a.b.c.1", 5]) is {'a': {'b': {'c': [1, 5, 3, 4]}}}. Strings that can be
   *     casted to numerals are presumed to be array indexes
   * @param value the value
   * @return the json builder
   */
  public JsonBuilder<T> set(JsonArray jpath, Number value) {
    source = (T) lever.set(source, jpath, value);
    return this;
  }

  /**
   * Gets the json object built by the json builder
   *
   * @return the json object
   */
  public T get() {
    return source;
  }
}
