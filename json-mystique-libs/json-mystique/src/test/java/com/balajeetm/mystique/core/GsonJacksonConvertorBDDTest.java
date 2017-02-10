/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.balajeetm.mystique.core;

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

import com.balajeetm.mystique.core.config.JsonMystiqueConfig;
import com.balajeetm.mystique.core.config.MystiqueModuleConfig;
import com.balajeetm.mystique.util.jackson.bean.convertor.JacksonConvertor;
import com.balajeetm.mystique.util.jackson.config.JacksonUtilConfig;
import com.balajeetm.mystique.util.json.error.ConvertorException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;

/**
 * The Class JsonMystiqueBDDTest.
 *
 * @author balajeetm
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { JsonMystiqueConfig.class,
		JacksonUtilConfig.class, MystiqueModuleConfig.class })
public class GsonJacksonConvertorBDDTest {

	/** The instance. */
	@Autowired
	private JacksonConvertor instance;

	/**
	 * Inits the.
	 */
	@Before
	public void init() {
	}

	@Test
	public void jacksonGsonTest() {
		try {
			String locationPattern = "classpath:gsonJackson/gsonJackson.json";
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource resource = resourceResolver.getResource(locationPattern);

			JsonNode jacksonObject = instance.deserialize(resource.getInputStream(), JsonNode.class);
			JsonElement gsonObject = instance.deserialize(resource.getInputStream(), JsonElement.class);
			JsonElement transGsonObject = instance.deserialize(jacksonObject, JsonElement.class);
			JsonNode transJacksonObject = instance.deserialize(gsonObject, JsonNode.class);

			Assert.assertEquals(gsonObject, transGsonObject);
			Assert.assertEquals(jacksonObject, transJacksonObject);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void jacksonNegativeTest7() {
		try {
			JsonElement deserialize = instance.deserialize((String) null, JsonElement.class);
			Assert.assertNull(deserialize);
		} catch (ConvertorException e) {
			Assert.assertTrue(e.getMessage(), false);
		}
	}
}
