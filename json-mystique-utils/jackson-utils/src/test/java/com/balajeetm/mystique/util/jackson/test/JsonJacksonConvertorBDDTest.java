/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.jackson.test;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.balajeetm.mystique.util.jackson.convertor.JacksonConvertor;
import com.balajeetm.mystique.util.json.convertor.JsonConvertor;
import com.balajeetm.mystique.util.json.error.ConvertorException;

/**
 * The Class JsonJacksonConvertorBDDTest.
 *
 * @author balajeetm
 */
public class JsonJacksonConvertorBDDTest {

  /** The instance. */
  private JsonConvertor instance = JacksonConvertor.getInstance();

  /** The charset. */
  private Charset charset = Charset.forName("utf-8");

  /** Inits the. */
  @Before
  public void init() {}

  /** Jackson positive test. */
  @SuppressWarnings("unchecked")
  @Test
  public void jacksonPositiveTest() {
    try {
      String locationPattern = "classpath:samples/sample1.mys";
      ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
      Resource resource = resourceResolver.getResource(locationPattern);
      String string = IOUtils.toString(resource.getInputStream(), charset);

      List<Sample> deserializeList =
          instance.deserializeList(resource.getInputStream(), Sample.class);
      List<Sample> deserializeGroup =
          instance.deserializeGroup(resource.getInputStream(), List.class, Sample.class);
      Collection<Sample> deserializeGroup2 =
          instance.deserializeGroup(resource.getInputStream(), Collection.class, Sample.class);
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

  /** Jackson negative test 1. */
  @Test
  public void jacksonNegativeTest1() {
    try {
      instance.deserializeList((InputStream) null, Sample.class);
    } catch (ConvertorException e) {
      Assert.assertTrue(true);
    }
  }

  /** Jackson negative test 2. */
  @Test
  public void jacksonNegativeTest2() {
    try {
      instance.deserializeGroup((InputStream) null, List.class, Sample.class);
    } catch (ConvertorException e) {
      Assert.assertTrue(true);
    }
  }

  /** Jackson negative test 3. */
  @Test
  public void jacksonNegativeTest3() {
    try {
      instance.deserializeGroup((InputStream) null, Collection.class, Sample.class);
    } catch (ConvertorException e) {
      Assert.assertTrue(true);
    }
  }

  /** Jackson negative test 4. */
  @Test
  public void jacksonNegativeTest4() {
    try {
      instance.deserializeList((String) null, Sample.class);
    } catch (ConvertorException e) {
      Assert.assertTrue(true);
    }
  }

  /** Jackson negative test 5. */
  @Test
  public void jacksonNegativeTest5() {
    try {
      instance.deserializeGroup((String) null, List.class, Sample.class);
    } catch (ConvertorException e) {
      Assert.assertTrue(true);
    }
  }

  /** Jackson negative test 6. */
  @Test
  public void jacksonNegativeTest6() {
    try {
      instance.deserializeGroup((String) null, Collection.class, Sample.class);
    } catch (ConvertorException e) {
      Assert.assertTrue(true);
    }
  }

  /** Jackson negative test 7. */
  @Test
  public void jacksonNegativeTest7() {
    try {
      Sample deserialize = instance.deserialize((String) null, Sample.class);
      Assert.assertNull(deserialize);
    } catch (ConvertorException e) {
      Assert.assertTrue(e.getMessage(), false);
    }
  }
}
