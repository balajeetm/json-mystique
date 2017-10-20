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

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.balajeetm.mystique.util.gson.lever.JsonLever;
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

      JsonParser jsonParser = jsonLever.getJsonParser();
      JsonElement src1 = jsonParser.parse(new InputStreamReader(resource1.getInputStream()));
      JsonElement src2 = jsonParser.parse(new InputStreamReader(resource2.getInputStream()));
      JsonElement result = jsonParser.parse(new InputStreamReader(resultResource.getInputStream()));

      JsonElement merge = jsonLever.merge(src1, src2);
      Assert.assertTrue(merge.isJsonObject());

      Assert.assertEquals(result, merge);
    } catch (Exception e) {
      Assert.assertFalse(e.getMessage(), true);
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

      JsonParser jsonParser = jsonLever.getJsonParser();
      JsonElement src1 = jsonParser.parse(new InputStreamReader(resource1.getInputStream()));
      JsonElement src2 = jsonParser.parse(new InputStreamReader(resource2.getInputStream()));
      JsonElement result = jsonParser.parse(new InputStreamReader(resultResource.getInputStream()));

      JsonElement merge = jsonLever.merge(src1, src2, Boolean.TRUE);
      Assert.assertTrue(merge.isJsonObject());

      Assert.assertEquals(result, merge);
    } catch (Exception e) {
      Assert.assertFalse(e.getMessage(), true);
    }
  }
}
