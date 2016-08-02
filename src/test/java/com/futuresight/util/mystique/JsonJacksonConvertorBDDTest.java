/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

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

		}
		catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

}
