/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.futuresight.util.mystique.config.JsonMystiqueConfig;
import com.futuresight.util.mystique.lever.JsonComparator;
import com.futuresight.util.mystique.lever.MystResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

/**
 * The Class JsonMystiqueBDDTest.
 *
 * @author balajeetm
 */
/**
 * @author balajeetm
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {JsonMystiqueConfig.class})
public class JsonMystiqueBDDTest {

	/** The json mystique. */
	@Autowired
	private JsonMystique jsonMystique;

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

	/** The json comparator. */
	@Autowired
	private JsonComparator jsonComparator;

	/** The input format. */
	private String inputFormat = "classpath:jsonmystique/%s.input";

	/** The output format. */
	private String outputFormat = "classpath:jsonmystique/%s.output";

	/**
	 * Instantiates a new json mystique bdd test.
	 */
	public JsonMystiqueBDDTest() {
	}

	/**
	 * Inits the.
	 */
	@Before
	public void init() {
	}

	/**
	 * Test.
	 */
	@Test
	public void test() {
		try {
			String locationPattern = "classpath:jsonmystique/ptest.json";
			String outputPattern = "classpath:jsonmystique/ptest.output";
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource resource = resourceResolver.getResource(locationPattern);

			String string = IOUtils.toString(resource.getInputStream());
			Resource outputRes = resourceResolver.getResource(outputPattern);
			JsonElement output = jsonLever.getJsonParser().parse(new InputStreamReader(outputRes.getInputStream()));
			JsonElement transform = jsonMystique.transform(string, "ptest1");
			Boolean transformSuccess = transform != null && !transform.isJsonNull() && transform.isJsonObject();
			Assert.assertTrue(transformSuccess);
			JsonElement jsonElement = transform.getAsJsonObject().get("ba14");
			transform.getAsJsonObject().remove("ba14");
			Assert.assertTrue(null != jsonElement && !jsonElement.isJsonNull());
			Assert.assertEquals(output, transform);
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Simple copy01.
	 */
	@Test
	public void simpleCopy01() {
		testEqual("01SimpleCopy");
	}

	/**
	 * Simple copy02.
	 */
	@Test
	public void simpleCopy02() {
		testEqual("02SimpleCopy");
	}

	/**
	 * For each03.
	 */
	@Test
	public void forEach03() {
		testEqual("03ForEach");
	}

	/**
	 * Default04.
	 */
	@Test
	public void default04() {
		testEqual("04Default");
	}

	/**
	 * Aces05.
	 */
	@Test
	public void aces05() {
		testEqual("05Aces");
	}

	/**
	 * Concat06.
	 */
	@Test
	public void concat06() {
		testEqual("06Concat");
	}

	/**
	 * Condition07.
	 */
	@Test
	public void condition07() {
		testEqual("07Condition");
	}

	/**
	 * Constant08.
	 */
	@Test
	public void constant08() {
		testEqual("08Constant", "07Condition");
	}

	/**
	 * Date09.
	 */
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
	 * Array to map11.
	 */
	@Test
	public void arrayToMap11() {
		testEqual("11ArrayToMap");
	}

	/**
	 * Mystique12.
	 */
	@Test
	public void mystique12() {
		testEqual("12Mystique");
	}

	/**
	 * String utils13.
	 */
	@Test
	public void stringUtils13() {
		testEqual("13StringUtils");
	}

	/**
	 * Chain14.
	 */
	@Test
	public void chain14() {
		testEqual("14Chain");
	}

	/**
	 * Toggle15.
	 */
	@Test
	public void toggle15() {
		testEqual("15Toggle");
	}

	/**
	 * Custom16.
	 */
	@Test
	public void custom16() {
		testEqual("16Custom");
	}

	/**
	 * Test equal.
	 *
	 * @param test the test
	 */
	private void testEqual(String test) {
		testEqual(test, null, null);
	}

	/**
	 * Test equal.
	 *
	 * @param test the test
	 * @param inputPattern the input pattern
	 */
	private void testEqual(String test, String inputPattern) {
		testEqual(test, inputPattern, null);
	}

	/**
	 * Test equal.
	 *
	 * @param test the test
	 * @param inputPattern the input pattern
	 * @param outputPattern the output pattern
	 */
	private void testEqual(String test, String inputPattern, String outputPattern) {

		try {
			JsonElement transform = transform(test, inputPattern);
			outputPattern = null == outputPattern ? String.format(outputFormat, test) : String.format(outputFormat,
					outputPattern);
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource outputRes = resourceResolver.getResource(outputPattern);
			JsonElement output = jsonLever.getJsonParser().parse(new InputStreamReader(outputRes.getInputStream()));
			Assert.assertEquals(output, transform);
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test subset.
	 *
	 * @param test the test
	 * @param inputPattern the input pattern
	 */
	private void testSubset(String test, String inputPattern) {
		testSubset(test, inputPattern, null);
	}

	/**
	 * Test subset.
	 *
	 * @param test the test
	 * @param inputPattern the input pattern
	 * @param outputPattern the output pattern
	 */
	private void testSubset(String test, String inputPattern, String outputPattern) {
		try {
			JsonElement transform = transform(test, inputPattern);
			outputPattern = null == outputPattern ? String.format(outputFormat, test) : String.format(outputFormat,
					outputPattern);
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource outputRes = resourceResolver.getResource(outputPattern);
			JsonElement output = jsonLever.getJsonParser().parse(new InputStreamReader(outputRes.getInputStream()));
			MystResult subset = jsonComparator.isSubset(output, transform);
			Assert.assertTrue(subset.getResult());
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
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
	private JsonElement transform(String test, String inputPattern) throws Exception {
		JsonElement transform = JsonNull.INSTANCE;
		inputPattern = null == inputPattern ? String.format(inputFormat, test) : String.format(inputFormat,
				inputPattern);
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		Resource resource = resourceResolver.getResource(inputPattern);

		String string = IOUtils.toString(resource.getInputStream());
		transform = jsonMystique.transform(string, test);
		return transform;
	}
}
