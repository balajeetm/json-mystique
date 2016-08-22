/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
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
import com.futuresight.util.mystique.lever.ConvertorInterface;
import com.futuresight.util.mystique.lever.JsonGsonConvertor;
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
public class JsonMystiqueBDDTest {

	@Autowired
	private JsonMystique jsonMystique;

	/** The json parser. */
	private JsonParser jsonParser;

	/**
	 * Instantiates a new json mystique bdd test.
	 */
	public JsonMystiqueBDDTest() {
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

	@Test
	public void simpleCopy01() {
		try {
			String inputPattern = "classpath:jsonmystique/01SimpleCopy.input";
			String outputPattern = "classpath:jsonmystique/01SimpleCopy.output";
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource resource = resourceResolver.getResource(inputPattern);

			String string = IOUtils.toString(resource.getInputStream());
			Resource outputRes = resourceResolver.getResource(outputPattern);
			JsonElement output = jsonParser.parse(new InputStreamReader(outputRes.getInputStream()));
			JsonElement transform = jsonMystique.transform(string, "01SimpleCopy");
			Assert.assertEquals(output, transform);
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void simpleCopy02() {
		try {
			String inputPattern = "classpath:jsonmystique/02SimpleCopy.input";
			String outputPattern = "classpath:jsonmystique/02SimpleCopy.output";
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource resource = resourceResolver.getResource(inputPattern);

			String string = IOUtils.toString(resource.getInputStream());
			Resource outputRes = resourceResolver.getResource(outputPattern);
			JsonElement output = jsonParser.parse(new InputStreamReader(outputRes.getInputStream()));
			JsonElement transform = jsonMystique.transform(string, "02SimpleCopy");
			Assert.assertEquals(output, transform);
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void forEach03() {
		try {
			String inputPattern = "classpath:jsonmystique/03ForEach.input";
			String outputPattern = "classpath:jsonmystique/03ForEach.output";
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource resource = resourceResolver.getResource(inputPattern);

			String string = IOUtils.toString(resource.getInputStream());
			Resource outputRes = resourceResolver.getResource(outputPattern);
			JsonElement output = jsonParser.parse(new InputStreamReader(outputRes.getInputStream()));
			JsonElement transform = jsonMystique.transform(string, "03ForEach");
			Assert.assertEquals(output, transform);
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void default04() {
		try {
			String inputPattern = "classpath:jsonmystique/04Default.input";
			String outputPattern = "classpath:jsonmystique/04Default.output";
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource resource = resourceResolver.getResource(inputPattern);

			String string = IOUtils.toString(resource.getInputStream());
			Resource outputRes = resourceResolver.getResource(outputPattern);
			JsonElement output = jsonParser.parse(new InputStreamReader(outputRes.getInputStream()));
			JsonElement transform = jsonMystique.transform(string, "04Default");
			Assert.assertEquals(output, transform);
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void chumma() {
		try {
			String locationPattern = "classpath:jsonmystique/*.abc";
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = null;
			resources = resourceResolver.getResources(locationPattern);
			if (!ArrayUtils.isEmpty(resources)) {
				Resource resource = resources[0];
				if (resource.exists()) {
					ConvertorInterface instance = JsonGsonConvertor.getInstance();
					List<Chumma> deserializeList = instance.deserializeList(resource.getInputStream(), Chumma.class);
					for (Chumma chumma : deserializeList) {
						System.out.println("oh");
					}
				}
			}
		}
		catch (Exception e) {
			System.err.println("A");
		}
	}
}
