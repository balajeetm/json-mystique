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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * The Class JsonLeverBDDTest.
 *
 * @author balajeetm
 */
public class JsonLeverBDDTest {

  /** The json lever. */
  private JsonLever jsonLever = JsonLever.getInstance();

  /** Merge. */
  @Test
  public void merge() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource resource1 = resourceResolver.getResource("classpath:jsonLever/src1.json");
      Resource resource2 = resourceResolver.getResource("classpath:jsonLever/src2.json");
      Resource resultResource = resourceResolver.getResource("classpath:jsonLever/result.json");

      JsonElement src1 = JsonParser.parseReader(new InputStreamReader(resource1.getInputStream()));
      JsonElement src2 = JsonParser.parseReader(new InputStreamReader(resource2.getInputStream()));
      JsonElement result =
          JsonParser.parseReader(new InputStreamReader(resultResource.getInputStream()));

      JsonElement merge = jsonLever.merge(src1, src2);
      Assertions.assertTrue(merge.isJsonObject());

      Assertions.assertEquals(result, merge);
    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /** Delete. */
  @Test
  public void delete() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource resource1 = resourceResolver.getResource("classpath:jsonLever/src1.json");
      Resource resultResource =
          resourceResolver.getResource("classpath:jsonLever/deleteResult.json");
      Resource deletedResource =
          resourceResolver.getResource("classpath:jsonLever/deletedPath.json");

      JsonElement src1 = JsonParser.parseReader(new InputStreamReader(resource1.getInputStream()));
      JsonElement result =
          JsonParser.parseReader(new InputStreamReader(resultResource.getInputStream()));
      JsonElement deleted =
          JsonParser.parseReader(new InputStreamReader(deletedResource.getInputStream()));

      JsonArray test = new JsonArray();
      test.add("content");
      test.add("lang");

      JsonElement delete = jsonLever.delete(src1, test);

      Assertions.assertEquals(result, src1);
      Assertions.assertEquals(deleted, delete);
    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /** Delete. */
  @Test
  public void deleteString() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource resource1 = resourceResolver.getResource("classpath:jsonLever/src1.json");
      Resource resultResource =
          resourceResolver.getResource("classpath:jsonLever/deleteResult.json");
      Resource deletedResource =
          resourceResolver.getResource("classpath:jsonLever/deletedPath.json");

      JsonElement src1 = JsonParser.parseReader(new InputStreamReader(resource1.getInputStream()));
      JsonElement result =
          JsonParser.parseReader(new InputStreamReader(resultResource.getInputStream()));
      JsonElement deleted =
          JsonParser.parseReader(new InputStreamReader(deletedResource.getInputStream()));

      JsonElement delete = jsonLever.remove(src1, "content.lang");

      Assertions.assertEquals(result, src1);
      Assertions.assertEquals(deleted, delete);
    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /** Delete. */
  @Test
  public void deletePath() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource resource1 = resourceResolver.getResource("classpath:jsonLever/src1.json");
      Resource resultResource =
          resourceResolver.getResource("classpath:jsonLever/deleteResult.json");
      Resource deletedResource =
          resourceResolver.getResource("classpath:jsonLever/deletedPath.json");

      JsonElement src1 = JsonParser.parseReader(new InputStreamReader(resource1.getInputStream()));
      JsonElement result =
          JsonParser.parseReader(new InputStreamReader(resultResource.getInputStream()));
      JsonElement deleted =
          JsonParser.parseReader(new InputStreamReader(deletedResource.getInputStream()));

      JsonElement delete = jsonLever.delete(src1, "content", "lang");

      Assertions.assertEquals(result, src1);
      Assertions.assertEquals(deleted, delete);
    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /** Delete. */
  @Test
  public void deleteNegative() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource resource1 = resourceResolver.getResource("classpath:jsonLever/src1.json");
      Resource resultResource = resourceResolver.getResource("classpath:jsonLever/src1.json");

      JsonElement src1 = JsonParser.parseReader(new InputStreamReader(resource1.getInputStream()));
      JsonElement result =
          JsonParser.parseReader(new InputStreamReader(resultResource.getInputStream()));

      JsonElement delete = jsonLever.remove(src1, "category");

      Assertions.assertEquals(result, src1);
      Assertions.assertEquals(null, delete);
    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }

  /** Merge array. */
  @Test
  public void mergeArray() {
    try {
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource resource1 = resourceResolver.getResource("classpath:jsonLever/src1.json");
      Resource resource2 = resourceResolver.getResource("classpath:jsonLever/src2.json");
      Resource resultResource =
          resourceResolver.getResource("classpath:jsonLever/resultArray.json");

      JsonElement src1 = JsonParser.parseReader(new InputStreamReader(resource1.getInputStream()));
      JsonElement src2 = JsonParser.parseReader(new InputStreamReader(resource2.getInputStream()));
      JsonElement result =
          JsonParser.parseReader(new InputStreamReader(resultResource.getInputStream()));

      JsonElement merge = jsonLever.merge(src1, src2, Boolean.TRUE);
      Assertions.assertTrue(merge.isJsonObject());

      Assertions.assertEquals(result, merge);
    } catch (Exception e) {
      Assertions.assertFalse(true, e.getMessage());
    }
  }
}
