/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.test;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.balajeetm.mystique.util.gson.bean.convertor.GsonConvertor;
import com.balajeetm.mystique.util.gson.config.GsonUtilConfig;
import com.balajeetm.mystique.util.json.error.ConvertorException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class JsonGsonConvertorBDDTest.
 *
 * @author balajeetm
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { GsonUtilConfig.class })
public class JsonGsonConvertorBDDTest {

	/** The instance. */
	@Autowired
	private GsonConvertor instance;

	/**
	 * Inits the.
	 */
	@Before
	public void init() {
	}

	/**
	 * Gson positive test.
	 */
	@Test
	public void gsonPositiveTest() {
		try {
			String locationPattern = "classpath:samples/sample1.mys";
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource resource = resourceResolver.getResource(locationPattern);
			String string = IOUtils.toString(resource.getInputStream());

			List<Sample> deserializeList = instance.deserializeList(resource.getInputStream(), Sample.class);
			List<Sample> deserializeGroup = instance.deserializeGroup(resource.getInputStream(), List.class,
					Sample.class);
			Collection<Sample> deserializeGroup2 = instance.deserializeGroup(resource.getInputStream(),
					Collection.class, Sample.class);
			Assert.assertNotNull(deserializeList);
			Assert.assertNotNull(deserializeGroup);
			Assert.assertNotNull(deserializeGroup2);
			deserializeList = instance.deserializeList(string, Sample.class);
			deserializeGroup = instance.deserializeGroup(string, List.class, Sample.class);
			deserializeGroup2 = instance.deserializeGroup(string, Collection.class, Sample.class);
			Assert.assertNotNull(deserializeList);
			Assert.assertNotNull(deserializeGroup);
			Assert.assertNotNull(deserializeGroup2);

			for (Sample tarot : deserializeList) {
				String serialize = instance.serialize(tarot);
				Sample deserialize = instance.deserialize(serialize, Sample.class);
				Assert.assertNotNull(deserialize);
				deserialize = instance.deserialize(tarot, Sample.class);
				Assert.assertNotNull(deserialize);
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Gson negative test1.
	 */
	@Test
	public void gsonNegativeTest1() {
		try {
			instance.deserializeList((InputStream) null, Sample.class);
		} catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Gson negative test2.
	 */
	@Test
	public void gsonNegativeTest2() {
		try {
			instance.deserializeGroup((InputStream) null, List.class, Sample.class);
		} catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Gson negative test3.
	 */
	@Test
	public void gsonNegativeTest3() {
		try {
			instance.deserializeGroup((InputStream) null, Collection.class, Sample.class);
		} catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Gson negative test4.
	 */
	@Test
	public void gsonNegativeTest4() {
		try {
			instance.deserializeList((String) null, Sample.class);
		} catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Gson negative test5.
	 */
	@Test
	public void gsonNegativeTest5() {
		try {
			instance.deserializeGroup((String) null, List.class, Sample.class);
		} catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Gson negative test6.
	 */
	@Test
	public void gsonNegativeTest6() {
		try {
			instance.deserializeGroup((String) null, Collection.class, Sample.class);
		} catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * Gson negative test7.
	 */
	@Test
	public void gsonNegativeTest7() {
		try {
			JsonElement deserialize = instance.deserialize((String) null, JsonElement.class);
			Assert.assertNull(deserialize);
		} catch (ConvertorException e) {
			Assert.assertTrue(e.getMessage(), false);
		}
	}

	/**
	 * Gson negative test8.
	 */
	@Test
	public void gsonNegativeTest8() {
		try {
			JsonElement deserialize = instance.deserialize((Object) null, JsonObject.class);
			Assert.assertNull(deserialize);
		} catch (ConvertorException e) {
			Assert.assertTrue(e.getMessage(), false);
		}
	}

}
