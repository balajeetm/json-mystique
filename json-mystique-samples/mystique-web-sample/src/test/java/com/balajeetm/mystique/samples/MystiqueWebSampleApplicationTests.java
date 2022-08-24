/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 26 Jan, 2017 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.samples;

import com.balajeetm.mystique.util.gson.lever.Comparison;
import com.balajeetm.mystique.util.gson.lever.JsonComparator;
import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

/**
 * The Class MystiqueWebSampleApplicationTests.
 *
 * @author balajeetm
 */
@ContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MystiqueWebSampleApplicationTests {

  /** The rest template. */
  @Autowired private RestTemplate restTemplate;

  /** The json lever. */
  @Autowired private JsonLever jsonLever;

  /** The comparator. */
  @Autowired private JsonComparator comparator;

  @LocalServerPort @Autowired private String localPort;

  /** The file format. */
  protected String fileFormat = "classpath:expected/%s.json";

  private String getEndpoint(String path) {
    return String.format("http://localhost:%s/%s", localPort, path);
  }

  /** Context loads. */
  @Test
  public void contextLoads() {}

  /** Ping test. */
  @Test
  public void pingTest() {
    String body = this.restTemplate.getForObject(getEndpoint("/mystique/ping"), String.class);
    Assertions.assertEquals("Ping Working", body);
  }

  /** Gson serialise test. */
  @Test
  public void gsonSerialiseTest() {
    try {
      JsonElement body =
          this.restTemplate.getForObject(
              getEndpoint(String.format("/mystique/gson/serialise?msg=%s", "balajeetm")),
              JsonElement.class);
      JsonElement resource = getResource("gsonSerialise");
      Assertions.assertEquals(resource, body);
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail(e.getMessage());
    }
  }

  /** Gson deserialise test. */
  @Test
  public void gsonDeserialiseTest() {
    try {
      JsonObject payload = new JsonObject();
      payload.addProperty("name", "balajeetm");
      JsonElement response =
          this.restTemplate.postForObject(
              getEndpoint("/mystique/gson/deserialise"), payload, JsonElement.class);
      JsonElement resource = getResource("gsonDeserialise");
      Comparison subset = comparator.isSubset(resource, response);
      Assertions.assertTrue(subset.getResult(), Arrays.toString(subset.getMsgs().toArray()));
      Assertions.assertEquals(payload, jsonLever.asJsonObject(response).get("payload"));
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail(e.getMessage());
    }
  }

  /** Convert test. */
  @Test
  public void convertTest() {
    try {
      JsonObject payload = new JsonObject();
      payload.addProperty("name", "balajeetm");
      JsonElement response =
          this.restTemplate.postForObject(
              getEndpoint("/mystique/convert"), payload, JsonElement.class);
      Assertions.assertEquals(payload, jsonLever.getJsonObject(response, "employee"));
    } catch (Exception e) {
      e.printStackTrace();
      Assertions.fail(e.getMessage());
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
  private JsonElement getResource(String fileName)
      throws JsonIOException, JsonSyntaxException, IOException {
    ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    Resource outputRes = resourceResolver.getResource(String.format(fileFormat, fileName));
    return JsonParser.parseReader(new InputStreamReader(outputRes.getInputStream()));
  }
}
