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
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.futuresight.util.mystique.Tarot;
import com.futuresight.util.mystique.config.JsonMystiqueConfig;
import com.futuresight.util.mystique.lever.ConvertorException;
import com.futuresight.util.mystique.lever.ConvertorInterface;
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
			String locationPattern = "classpath:jsonmystique/ptest.mys";
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource resource = resourceResolver.getResource(locationPattern);
			String string = IOUtils.toString(resource.getInputStream());

			ConvertorInterface instance = JsonJacksonConvertor.getInstance();
			List<Tarot> deserializeList = instance.deserializeList(resource.getInputStream(), Tarot.class);
			List<Tarot> deserializeGroup = instance.deserializeGroup(resource.getInputStream(), List.class,
					Tarot.class);
			Collection<Tarot> deserializeGroup2 = instance.deserializeGroup(resource.getInputStream(),
					Collection.class, Tarot.class);
			Assert.assertNotNull(deserializeList);
			Assert.assertNotNull(deserializeGroup);
			Assert.assertNotNull(deserializeGroup2);
			deserializeList = instance.deserializeList(string, Tarot.class);
			deserializeGroup = instance.deserializeGroup(string, List.class, Tarot.class);
			deserializeGroup2 = instance.deserializeGroup(string, Collection.class, Tarot.class);
			Assert.assertNotNull(deserializeList);
			Assert.assertNotNull(deserializeGroup);
			Assert.assertNotNull(deserializeGroup2);

			for (Tarot tarot : deserializeList) {
				String serialize = instance.serialize(tarot);
				Tarot deserialize = instance.deserialize(serialize, Tarot.class);
				Assert.assertNotNull(deserialize);
				deserialize = instance.deserialize(tarot, Tarot.class);
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
			ConvertorInterface instance = JsonJacksonConvertor.getInstance();
			instance.deserializeList((InputStream) null, Tarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void jacksonNegativeTest2() {
		try {
			ConvertorInterface instance = JsonJacksonConvertor.getInstance();
			instance.deserializeGroup((InputStream) null, List.class, Tarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void jacksonNegativeTest3() {
		try {
			ConvertorInterface instance = JsonJacksonConvertor.getInstance();
			instance.deserializeGroup((InputStream) null, Collection.class, Tarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void jacksonNegativeTest4() {
		try {
			ConvertorInterface instance = JsonJacksonConvertor.getInstance();
			instance.deserializeList((String) null, Tarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void jacksonNegativeTest5() {
		try {
			ConvertorInterface instance = JsonJacksonConvertor.getInstance();
			instance.deserializeGroup((String) null, List.class, Tarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void jacksonNegativeTest6() {
		try {
			ConvertorInterface instance = JsonJacksonConvertor.getInstance();
			instance.deserializeGroup((String) null, Collection.class, Tarot.class);
		}
		catch (ConvertorException e) {
			Assert.assertTrue(true);
		}
	}

}
