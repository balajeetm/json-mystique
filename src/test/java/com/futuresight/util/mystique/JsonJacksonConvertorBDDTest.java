/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.futuresight.util.mystique.config.JsonMystiqueConfig;
import com.futuresight.util.mystique.lever.ConvertorException;
import com.futuresight.util.mystique.lever.JsonJacksonConvertor;

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
public class JsonJacksonConvertorBDDTest {

	@Autowired
	private JsonJacksonConvertor instance;

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
	public void jacksonPositiveTest() {
		try {
			String locationPattern = "classpath:jsonmystique/ptest1.mys";
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource resource = resourceResolver.getResource(locationPattern);
			String string = IOUtils.toString(resource.getInputStream());

			List<TestTarot> deserializeList = instance.deserializeList(resource.getInputStream(), TestTarot.class);
			List<TestTarot> deserializeGroup = instance.deserializeGroup(resource.getInputStream(), List.class,
					TestTarot.class);
			Collection<TestTarot> deserializeGroup2 = instance.deserializeGroup(resource.getInputStream(),
					Collection.class, TestTarot.class);
			Assert.assertNotNull(deserializeList);
			Assert.assertNotNull(deserializeGroup);
			Assert.assertNotNull(deserializeGroup2);
			deserializeList = instance.deserializeList(string, TestTarot.class);
			deserializeGroup = instance.deserializeGroup(string, List.class, TestTarot.class);
			deserializeGroup2 = instance.deserializeGroup(string, Collection.class, TestTarot.class);
			Assert.assertNotNull(deserializeList);
			Assert.assertNotNull(deserializeGroup);
			Assert.assertNotNull(deserializeGroup2);

			for (TestTarot tarot : deserializeList) {
				String serialize = instance.serialize(tarot);
				TestTarot deserialize = instance.deserialize(serialize, TestTarot.class);
				Assert.assertNotNull(deserialize);
				deserialize = instance.deserialize(tarot, TestTarot.class);
				Assert.assertNotNull(deserialize);
			}
		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void jacksonNegativeTest1() {
		try {
			instance.deserializeList((InputStream) null, TestTarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void jacksonNegativeTest2() {
		try {
			instance.deserializeGroup((InputStream) null, List.class, TestTarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void jacksonNegativeTest3() {
		try {
			instance.deserializeGroup((InputStream) null, Collection.class, TestTarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void jacksonNegativeTest4() {
		try {
			instance.deserializeList((String) null, TestTarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void jacksonNegativeTest5() {
		try {
			instance.deserializeGroup((String) null, List.class, TestTarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void jacksonNegativeTest6() {
		try {
			instance.deserializeGroup((String) null, Collection.class, TestTarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

}
