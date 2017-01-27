/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 26 Jan, 2017 by balajeetm
 */
package com.balajeetm.mystique.samples;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.junit4.SpringRunner;

import com.balajeetm.mystique.util.gson.bean.lever.JsonComparator;
import com.balajeetm.mystique.util.gson.bean.lever.JsonLever;
import com.balajeetm.mystique.util.gson.bean.lever.MystResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * The Class MystiqueWebSampleApplicationTests.
 *
 * @author balajeetm
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MystiqueWebSampleApplicationTests {

	/** The rest template. */
	@Autowired
	private TestRestTemplate restTemplate;

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

	/** The comparator. */
	@Autowired
	private JsonComparator comparator;

	/** The file format. */
	protected String fileFormat = "classpath:expected/%s.json";

	/**
	 * Context loads.
	 */
	@Test
	public void contextLoads() {
	}

	/**
	 * Ping test.
	 */
	@Test
	public void pingTest() {
		String body = this.restTemplate.getForObject("/mystique/ping", String.class);
		Assertions.assertThat(body).isEqualTo("Ping Working");
	}

	/**
	 * Gson serialise test.
	 */
	@Test
	public void gsonSerialiseTest() {
		try {
			JsonElement body = this.restTemplate
					.getForObject(String.format("/mystique/gson/serialise?msg=%s", "balajeetm"), JsonElement.class);
			JsonElement resource = getResource("gsonSerialise");
			Assert.assertEquals(resource, body);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Gson deserialise test.
	 */
	@Test
	public void gsonDeserialiseTest() {
		try {
			JsonObject payload = new JsonObject();
			payload.addProperty("name", "balajeetm");
			JsonElement response = this.restTemplate.postForObject("/mystique/gson/deserialise", payload,
					JsonElement.class);
			JsonElement resource = getResource("gsonDeserialise");
			MystResult subset = comparator.isSubset(resource, response);
			Assert.assertTrue(Arrays.toString(subset.getMsgs().toArray()), subset.getResult());
			Assert.assertEquals(payload, jsonLever.getAsJsonObject(response).get("payload"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Convert test.
	 */
	@Test
	public void convertTest() {
		try {
			JsonObject payload = new JsonObject();
			payload.addProperty("name", "balajeetm");
			JsonElement response = this.restTemplate.postForObject("/mystique/convert", payload, JsonElement.class);
			Assert.assertEquals(payload, jsonLever.getFieldAsJsonObject(response, "employee"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Gets the resource.
	 *
	 * @param fileName the file name
	 * @return the resource
	 * @throws JsonIOException the json IO exception
	 * @throws JsonSyntaxException the json syntax exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private JsonElement getResource(String fileName) throws JsonIOException, JsonSyntaxException, IOException {
		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		Resource outputRes = resourceResolver.getResource(String.format(fileFormat, fileName));
		return jsonLever.getJsonParser().parse(new InputStreamReader(outputRes.getInputStream()));
	}

}
