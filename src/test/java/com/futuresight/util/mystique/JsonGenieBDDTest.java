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
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
public class JsonGenieBDDTest {

	@Autowired
	private JsonGenie jsonGenie;

	/** The json parser. */
	private JsonParser jsonParser;

	/**
	 * Instantiates a new json mystique bdd test.
	 */
	public JsonGenieBDDTest() {
		jsonParser = new JsonParser();
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
			JsonElement output = jsonParser.parse(new InputStreamReader(outputRes.getInputStream()));
			JsonElement transform = jsonGenie.transform(string, "ptest1");
			Assert.assertEquals(output, transform);
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
