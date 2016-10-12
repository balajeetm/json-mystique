/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 13 Sep, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import rx.Observable;

import com.futuresight.util.mystique.config.JsonMystiqueConfig;
import com.futuresight.util.mystique.lever.JsonQuery;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

/**
 * The Class JsonQueryBDDTest.
 *
 * @author balajeetm
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {JsonMystiqueConfig.class})
public class JsonQueryBDDTest {

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

	/** The jq. */
	@Autowired
	private JsonQuery jq;

	/**
	 * Gets the field equals.
	 *
	 * @return the field equals
	 */
	@Test
	public void getFieldEquals() {
		try {
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
			Resource qres = resourceResolver.getResource("classpath:jsonQuery/query1.json");

			JsonParser jsonParser = jsonLever.getJsonParser();
			JsonElement sample = jsonParser.parse(new InputStreamReader(samres.getInputStream()));
			JsonElement query = jsonParser.parse(new InputStreamReader(qres.getInputStream()));
			jsonLever.getAsJsonObject(query).add("from", sample);

			JsonElement result = jq.query(query.getAsJsonObject());

			Assert.assertTrue(jsonLever.isJsonArray(result));
			Assert.assertEquals(1, result.getAsJsonArray().size());

			Assert.assertEquals("prajwal", jsonLever.getAsString(result.getAsJsonArray().get(0)));
		}
		catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Gets the field not equals.
	 *
	 * @return the field not equals
	 */
	@Test
	public void getFieldNotEquals() {
		try {
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
			Resource qres = resourceResolver.getResource("classpath:jsonQuery/query2.json");

			JsonParser jsonParser = jsonLever.getJsonParser();
			JsonElement sample = jsonParser.parse(new InputStreamReader(samres.getInputStream()));
			JsonElement query = jsonParser.parse(new InputStreamReader(qres.getInputStream()));
			jsonLever.getAsJsonObject(query).add("from", sample);

			JsonElement result = jq.query(query.getAsJsonObject());

			Assert.assertTrue(jsonLever.isJsonArray(result));
			Assert.assertEquals(2, result.getAsJsonArray().size());

		}
		catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Gets the field not equals async.
	 *
	 * @return the field not equals async
	 */
	@Test
	public void getFieldNotEqualsAsync() {
		try {
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
			Resource qres = resourceResolver.getResource("classpath:jsonQuery/query2.json");

			JsonParser jsonParser = jsonLever.getJsonParser();
			JsonElement sample = jsonParser.parse(new InputStreamReader(samres.getInputStream()));
			JsonElement query = jsonParser.parse(new InputStreamReader(qres.getInputStream()));
			jsonLever.getAsJsonObject(query).add("from", sample);

			Observable<JsonElement> queryAsync = jq.queryAsync(query.getAsJsonObject());
			queryAsync.subscribe(json -> {
				Assert.assertTrue(jsonLever.isString(json));
				String str = jsonLever.getAsString(json);
				Assert.assertNotEquals("prajwal", str);

			});
		}
		catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Gets the field in.
	 *
	 * @return the field in
	 */
	@Test
	public void getFieldIn() {
		try {
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
			Resource qres = resourceResolver.getResource("classpath:jsonQuery/query3.json");

			JsonParser jsonParser = jsonLever.getJsonParser();
			JsonElement sample = jsonParser.parse(new InputStreamReader(samres.getInputStream()));
			JsonElement query = jsonParser.parse(new InputStreamReader(qres.getInputStream()));
			jsonLever.getAsJsonObject(query).add("from", sample);

			JsonElement result = jq.query(query.getAsJsonObject());

			Assert.assertTrue(jsonLever.isJsonArray(result));
			Assert.assertEquals(2, result.getAsJsonArray().size());

		}
		catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Gets the field not in.
	 *
	 * @return the field not in
	 */
	@Test
	public void getFieldNotIn() {
		try {
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
			Resource qres = resourceResolver.getResource("classpath:jsonQuery/query4.json");

			JsonParser jsonParser = jsonLever.getJsonParser();
			JsonElement sample = jsonParser.parse(new InputStreamReader(samres.getInputStream()));
			JsonElement query = jsonParser.parse(new InputStreamReader(qres.getInputStream()));
			jsonLever.getAsJsonObject(query).add("from", sample);

			JsonElement result = jq.query(query.getAsJsonObject());

			Assert.assertTrue(jsonLever.isJsonArray(result));
			Assert.assertEquals(3, result.getAsJsonArray().size());

		}
		catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * Gets the field not in limit.
	 *
	 * @return the field not in limit
	 */
	@Test
	public void getFieldNotInLimit() {
		try {
			ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			Resource samres = resourceResolver.getResource("classpath:jsonQuery/sample1.json");
			Resource qres = resourceResolver.getResource("classpath:jsonQuery/query4.json");

			JsonParser jsonParser = jsonLever.getJsonParser();
			JsonElement sample = jsonParser.parse(new InputStreamReader(samres.getInputStream()));
			JsonElement query = jsonParser.parse(new InputStreamReader(qres.getInputStream()));
			jsonLever.getAsJsonObject(query).add("from", sample);
			jsonLever.getAsJsonObject(query).add("limit", new JsonPrimitive(2));

			JsonElement result = jq.query(query.getAsJsonObject());

			Assert.assertTrue(jsonLever.isJsonArray(result));
			Assert.assertEquals(2, result.getAsJsonArray().size());

		}
		catch (Exception e) {
			Assert.assertFalse(e.getMessage(), true);
		}
	}

}
