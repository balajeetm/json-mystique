/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 13 Sep, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.test;

import java.io.InputStreamReader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.balajeetm.mystique.util.gson.lever.JsonQuery;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import reactor.core.publisher.Flux;

/**
 * The Class JsonQueryBDDTest.
 *
 * @author balajeetm
 */
public class JsonQueryBDDTest {

  /** The json lever. */
  private JsonLever jsonLever = JsonLever.getInstance();

  /** The jq. */
  private JsonQuery jq = JsonQuery.getInstance();

  /**
   * Gets the field equals.
   *
   * @return the field equals
   */
  @Test
  public void getFieldEquals() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
      Resource qres = resourceResolver.getResource("classpath:jsonQuery/query1.json");

      JsonElement sample = JsonParser.parseReader(new InputStreamReader(samres.getInputStream()));
      JsonElement query = JsonParser.parseReader(new InputStreamReader(qres.getInputStream()));
      jsonLever.asJsonObject(query).add("from", sample);

      JsonElement result = jq.query(query.getAsJsonObject());

      Assertions.assertTrue(jsonLever.isArray(result));
      Assertions.assertEquals(1, result.getAsJsonArray().size());

      Assertions.assertEquals("prajwal", jsonLever.asString(result.getAsJsonArray().get(0)));
    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /**
   * Gets the field equals or.
   *
   * @return the field equals or
   */
  @Test
  public void getFieldEqualsOr() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
      Resource qres = resourceResolver.getResource("classpath:jsonQuery/query5.json");

      JsonElement sample = JsonParser.parseReader(new InputStreamReader(samres.getInputStream()));
      JsonElement query = JsonParser.parseReader(new InputStreamReader(qres.getInputStream()));
      jsonLever.asJsonObject(query).add("from", sample);

      JsonElement result = jq.query(query.getAsJsonObject());

      Assertions.assertTrue(jsonLever.isArray(result));
      Assertions.assertEquals(2, result.getAsJsonArray().size());

      Assertions.assertEquals("prajwal", jsonLever.asString(result.getAsJsonArray().get(0)));
      Assertions.assertEquals("paneesh", jsonLever.asString(result.getAsJsonArray().get(1)));
    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /**
   * Gets the field equals and.
   *
   * @return the field equals and
   */
  @Test
  public void getFieldEqualsAnd() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
      Resource qres = resourceResolver.getResource("classpath:jsonQuery/query6.json");

      JsonElement sample = JsonParser.parseReader(new InputStreamReader(samres.getInputStream()));
      JsonElement query = JsonParser.parseReader(new InputStreamReader(qres.getInputStream()));
      jsonLever.asJsonObject(query).add("from", sample);

      JsonElement result = jq.query(query.getAsJsonObject());

      Assertions.assertTrue(jsonLever.isArray(result));
      Assertions.assertEquals(1, result.getAsJsonArray().size());

      Assertions.assertEquals("prajwal", jsonLever.asString(result.getAsJsonArray().get(0)));
    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /**
   * Gets the field equals and failure.
   *
   * @return the field equals and failure
   */
  @Test
  public void getFieldEqualsAndFailure() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
      Resource qres = resourceResolver.getResource("classpath:jsonQuery/query7.json");

      JsonElement sample = JsonParser.parseReader(new InputStreamReader(samres.getInputStream()));
      JsonElement query = JsonParser.parseReader(new InputStreamReader(qres.getInputStream()));
      jsonLever.asJsonObject(query).add("from", sample);

      JsonElement result = jq.query(query.getAsJsonObject());

      Assertions.assertTrue(jsonLever.isArray(result));
      Assertions.assertEquals(0, result.getAsJsonArray().size());

    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /**
   * Gets the field not equals.
   *
   * @return the field not equals
   */
  @Test
  public void getFieldNotEquals() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
      Resource qres = resourceResolver.getResource("classpath:jsonQuery/query2.json");

      JsonElement sample = JsonParser.parseReader(new InputStreamReader(samres.getInputStream()));
      JsonElement query = JsonParser.parseReader(new InputStreamReader(qres.getInputStream()));
      jsonLever.asJsonObject(query).add("from", sample);

      JsonElement result = jq.query(query.getAsJsonObject());

      Assertions.assertTrue(jsonLever.isArray(result));
      Assertions.assertEquals(2, result.getAsJsonArray().size());

    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /**
   * Gets the field not equals async.
   *
   * @return the field not equals async
   */
  @Test
  public void getFieldNotEqualsAsync() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
      Resource qres = resourceResolver.getResource("classpath:jsonQuery/query2.json");

      JsonElement sample = JsonParser.parseReader(new InputStreamReader(samres.getInputStream()));
      JsonElement query = JsonParser.parseReader(new InputStreamReader(qres.getInputStream()));
      jsonLever.asJsonObject(query).add("from", sample);

      Flux<JsonElement> queryAsync = jq.queryAsync(query.getAsJsonObject());
      queryAsync.subscribe(
          json -> {
            Assertions.assertTrue(jsonLever.isString(json));
            String str = jsonLever.asString(json);
            Assertions.assertNotEquals("prajwal", str);
          });
    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /**
   * Gets the field in.
   *
   * @return the field in
   */
  @Test
  public void getFieldIn() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
      Resource qres = resourceResolver.getResource("classpath:jsonQuery/query3.json");

      JsonElement sample = JsonParser.parseReader(new InputStreamReader(samres.getInputStream()));
      JsonElement query = JsonParser.parseReader(new InputStreamReader(qres.getInputStream()));
      jsonLever.asJsonObject(query).add("from", sample);

      JsonElement result = jq.query(query.getAsJsonObject());

      Assertions.assertTrue(jsonLever.isArray(result));
      Assertions.assertEquals(2, result.getAsJsonArray().size());

    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /**
   * Gets the field not in.
   *
   * @return the field not in
   */
  @Test
  public void getFieldNotIn() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
      Resource qres = resourceResolver.getResource("classpath:jsonQuery/query4.json");

      JsonElement sample = JsonParser.parseReader(new InputStreamReader(samres.getInputStream()));
      JsonElement query = JsonParser.parseReader(new InputStreamReader(qres.getInputStream()));
      jsonLever.asJsonObject(query).add("from", sample);

      JsonElement result = jq.query(query.getAsJsonObject());

      Assertions.assertTrue(jsonLever.isArray(result));
      Assertions.assertEquals(3, result.getAsJsonArray().size());

    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /**
   * Gets the field not in limit.
   *
   * @return the field not in limit
   */
  @Test
  public void getFieldNotInLimit() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
      Resource qres = resourceResolver.getResource("classpath:jsonQuery/query4.json");

      JsonElement sample = JsonParser.parseReader(new InputStreamReader(samres.getInputStream()));
      JsonElement query = JsonParser.parseReader(new InputStreamReader(qres.getInputStream()));
      jsonLever.asJsonObject(query).add("from", sample);
      jsonLever.asJsonObject(query).add("limit", new JsonPrimitive(2));

      JsonElement result = jq.query(query.getAsJsonObject());

      Assertions.assertTrue(jsonLever.isArray(result));
      Assertions.assertEquals(2, result.getAsJsonArray().size());

    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }
}
