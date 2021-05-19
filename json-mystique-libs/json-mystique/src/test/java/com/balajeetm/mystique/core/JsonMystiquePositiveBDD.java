/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 17 Nov, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core;

import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.balajeetm.mystique.core.lever.MystiqueLever;
import com.balajeetm.mystique.core.test.CustomMystTurn;
import com.balajeetm.mystique.core.test.TestMystTurn;
import com.balajeetm.mystique.util.gson.lever.Comparison;
import com.balajeetm.mystique.util.gson.lever.JsonComparator;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;

/**
 * The Class JsonMystiquePositiveBDD.
 *
 * @author balajeetm
 */
public class JsonMystiquePositiveBDD {

  /** The json mystique. */
  protected JsonMystique jsonMystique;

  /** The json lever. */
  protected MystiqueLever jsonLever;

  /** The json comparator. */
  protected JsonComparator jsonComparator;

  /** The input format. */
  protected String inputFormat = "classpath:jsonmystique/%s.input";

  /** The output format. */
  protected String outputFormat = "classpath:jsonmystique/%s.output";

  /** Instantiates a new json mystique bdd test. */
  public JsonMystiquePositiveBDD() {
    jsonMystique = JsonMystique.getInstance();
    jsonLever = MystiqueLever.getInstance();
    jsonComparator = JsonComparator.getInstance();
  }

  /** Inits the. */
  @BeforeEach
  public void init() {}

  /** Test. */
  @Test
  public void test() {
    try {
      jsonMystique.register(new TestMystTurn());
      String locationPattern = "classpath:jsonmystique/ptest.json";
      String outputPattern = "classpath:jsonmystique/ptest.output";
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource resource = resourceResolver.getResource(locationPattern);

      String string = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
      Resource outputRes = resourceResolver.getResource(outputPattern);
      JsonElement output =
          JsonParser.parseReader(new InputStreamReader(outputRes.getInputStream()));
      JsonElement transform = jsonMystique.transform(string, "ptest1");
      Boolean transformSuccess =
          transform != null && !transform.isJsonNull() && transform.isJsonObject();
      Assertions.assertTrue(transformSuccess);
      JsonElement jsonElement = transform.getAsJsonObject().get("ba14");
      transform.getAsJsonObject().remove("ba14");
      Assertions.assertTrue(null != jsonElement && !jsonElement.isJsonNull());
      Assertions.assertEquals(output, transform);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail(e.getMessage());
    }
  }

  /** Simple copy01. */
  @Test
  public void simpleCopy01() {
    testEqual("01SimpleCopy");
  }

  /** Simple copy 01 a. */
  @Test
  public void simpleCopy01a() {
    testEqual("01.a.SimpleCopy", "01SimpleCopy", "01SimpleCopy");
  }

  /** Simple copy02. */
  @Test
  public void simpleCopy02() {
    testEqual("02SimpleCopy");
  }

  /** Simple copy 02 a. */
  @Test
  public void simpleCopy02a() {
    testEqual("02.a.SimpleCopy", "02SimpleCopy", "02SimpleCopy");
  }

  /** For each03. */
  @Test
  public void forEach03() {
    testEqual("03ForEach");
  }

  /** For each 03 a. */
  @Test
  public void forEach03a() {
    testEqual("03.a.ForEach", "03ForEach", "03ForEach");
  }

  /** Default04. */
  @Test
  public void default04() {
    testEqual("04Default");
  }

  /** Default 04 a. */
  @Test
  public void default04a() {
    testEqual("04.a.Default", "04Default", "04Default");
  }

  /** Aces05. */
  @Test
  public void aces05() {
    testEqual("05Aces");
  }

  /** Aces 05 a. */
  @Test
  public void aces05a() {
    testEqual("05.a.Aces", "05Aces", "05Aces");
  }

  /** Concat06. */
  @Test
  public void concat06() {
    testEqual("06Concat");
  }

  /** Concat 06 a. */
  @Test
  public void concat06a() {
    testEqual("06.a.Concat", "06Concat", "06Concat");
  }

  /** Condition07. */
  @Test
  public void condition07() {
    testEqual("07Condition");
  }

  /** Condition 07 a. */
  @Test
  public void condition07a() {
    testEqual("07.a.Condition", "07Condition", "07Condition");
  }

  /** Constant08. */
  @Test
  public void constant08() {
    testEqual("08Constant", "07Condition");
  }

  /** Date09. */
  @Test
  public void date09() {
    testSubset("09Date", "07Condition");
  }

  /**
   * Gets the from deps10.
   *
   * @return the from deps10
   */
  @Test
  public void getFromDeps10() {
    testEqual("10GetFromDeps");
  }

  /**
   * Gets the from deps 10 a.
   *
   * @return the from deps 10 a
   */
  @Test
  public void getFromDeps10a() {
    testEqual("10.a.GetFromDeps", "10GetFromDeps");
  }

  /** Array to map11. */
  @Test
  public void arrayToMap11() {
    testEqual("11ArrayToMap");
  }

  /** Array to map 11 a. */
  @Test
  public void arrayToMap11a() {
    testEqual("11.a.ArrayToMap", "11ArrayToMap", "11ArrayToMap");
  }

  /** Mystique12. */
  @Test
  public void mystique12() {
    testEqual("12Mystique");
  }

  /** Mystique 12 a. */
  @Test
  public void mystique12a() {
    testEqual("12.a.Mystique", "12Mystique", "12Mystique");
  }

  /** String utils13. */
  @Test
  public void stringUtils13() {
    testEqual("13StringUtils");
  }

  /** String utils 13 a. */
  @Test
  public void stringUtils13a() {
    testEqual("13.a.StringUtils", "13StringUtils", "13StringUtils");
  }

  /** Chain14. */
  @Test
  public void chain14() {
    testEqual("14Chain");
  }

  /** Chain 14 a. */
  @Test
  public void chain14a() {
    testEqual("14.a.Chain", "14Chain", "14Chain");
  }

  /** Toggle15. */
  @Test
  public void toggle15() {
    testEqual("15Toggle");
  }

  /** Toggle 15 a. */
  @Test
  public void toggle15a() {
    testEqual("15.a.Toggle", "15Toggle", "15Toggle");
  }

  /** Custom16. */
  @Test
  public void custom16() {
    jsonMystique.register(new CustomMystTurn());
    testEqual("16Custom");
  }

  /**
   * Test equal.
   *
   * @param test the test
   */
  protected void testEqual(String test) {
    testEqual(test, null, null);
  }

  /**
   * Test equal.
   *
   * @param test the test
   * @param inputPattern the input pattern
   */
  protected void testEqual(String test, String inputPattern) {
    testEqual(test, inputPattern, null);
  }

  /**
   * Test equal.
   *
   * @param test the test
   * @param inputPattern the input pattern
   * @param outputPattern the output pattern
   */
  protected void testEqual(String test, String inputPattern, String outputPattern) {

    try {
      JsonElement transform = transform(test, inputPattern);
      outputPattern =
          null == outputPattern
              ? String.format(outputFormat, test)
              : String.format(outputFormat, outputPattern);
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource outputRes = resourceResolver.getResource(outputPattern);
      JsonElement output =
          JsonParser.parseReader(new InputStreamReader(outputRes.getInputStream()));
      Assertions.assertEquals(output, transform);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * Test subset.
   *
   * @param test the test
   * @param inputPattern the input pattern
   */
  protected void testSubset(String test, String inputPattern) {
    testSubset(test, inputPattern, null);
  }

  /**
   * Test subset.
   *
   * @param test the test
   * @param inputPattern the input pattern
   * @param outputPattern the output pattern
   */
  protected void testSubset(String test, String inputPattern, String outputPattern) {
    try {
      JsonElement transform = transform(test, inputPattern);
      outputPattern =
          null == outputPattern
              ? String.format(outputFormat, test)
              : String.format(outputFormat, outputPattern);
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource outputRes = resourceResolver.getResource(outputPattern);
      JsonElement output =
          JsonParser.parseReader(new InputStreamReader(outputRes.getInputStream()));
      Comparison subset = jsonComparator.isSubset(output, transform);
      Assertions.assertTrue(subset.getResult());
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail(e.getMessage());
    }
  }

  /**
   * Transform.
   *
   * @param test the test
   * @param inputPattern the input pattern
   * @return the json element
   * @throws Exception the exception
   */
  protected JsonElement transform(String test, String inputPattern) throws Exception {
    JsonElement transform = JsonNull.INSTANCE;
    inputPattern =
        null == inputPattern
            ? String.format(inputFormat, test)
            : String.format(inputFormat, inputPattern);
    ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    Resource resource = resourceResolver.getResource(inputPattern);

    String string = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
    transform = jsonMystique.transform(string, test);
    return transform;
  }
}
