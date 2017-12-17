/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Jan, 2017 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.samples.controller;

import java.net.URI;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.balajeetm.mystique.core.JsonMystique;
import com.balajeetm.mystique.samples.util.MystiqueSampleConfig;
import com.balajeetm.mystique.samples.util.TestModel;
import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import springfox.documentation.annotations.ApiIgnore;

/**
 * The Class SampleController.
 *
 * @author balajeetm
 */
@RestController
@RequestMapping("/mystique")
public class SampleController {

  /** The json mystique. */
  @Autowired JsonMystique jsonMystique;

  /** The rest template. */
  @Autowired RestTemplate restTemplate;

  /** The config. */
  @Autowired MystiqueSampleConfig config;

  @Qualifier("jsonLever")
  @Autowired
  JsonLever lever;

  /**
   * Ping.
   *
   * @return the string
   */
  @GetMapping(value = {"/ping"})
  @ApiIgnore
  public String ping() {
    return "Ping Working";
  }

  /**
   * Testmodel.
   *
   * @param body the body
   * @return the test model
   */
  @PostMapping(value = {"/testmodel"})
  @ApiIgnore
  public TestModel testmodel(@RequestBody TestModel body) {
    return body;
  }

  /**
   * Serialise.
   *
   * @param msg the msg
   * @return the json object
   */
  @GetMapping(value = {"/gson/serialise"})
  @ApiIgnore
  public JsonObject serialise(@RequestParam(value = "msg") String msg) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("report", "Gson Serialisation working good!");
    jsonObject.addProperty("status", Boolean.TRUE);
    jsonObject.addProperty("msg", msg);
    return jsonObject;
  }

  /**
   * Rest template.
   *
   * @return the json element
   */
  @GetMapping(value = {"/resttemplate"})
  @ApiIgnore
  public ResponseEntity<JsonElement> restTemplate() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("JsonStub-User-Key", config.getUserKey());
    headers.set("JsonStub-Project-Key", config.getProjectKey());
    headers.setContentType(MediaType.APPLICATION_JSON);
    RequestEntity<?> requestEntity =
        new RequestEntity<>(headers, HttpMethod.GET, URI.create(config.getEndpoint()));
    ResponseEntity<JsonElement> exchange = restTemplate.exchange(requestEntity, JsonElement.class);
    return exchange;
  }

  /**
   * Deserialise.
   *
   * @param payload the payload
   * @return the json element
   */
  @PostMapping(value = {"/gson/deserialise"})
  @ApiIgnore
  public JsonElement deserialise(@RequestBody JsonElement payload) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("report", "Gson Deserialisation working good!");
    jsonObject.addProperty("status", Boolean.TRUE);
    jsonObject.add("payload", payload);
    return jsonObject;
  }

  /**
   * Convert.
   *
   * @param specName the spec name
   * @param payload the payload
   * @return the json element
   */
  @PostMapping(value = {"/convert"})
  @ApiIgnore
  public JsonElement convert(
      @RequestParam(value = "specName", required = false) String specName,
      @RequestBody JsonElement payload) {
    specName = null == specName ? "copy" : specName;
    return jsonMystique.transform(payload, specName);
  }

  @PostMapping(value = {"/height/{root}"})
  public Integer height(@RequestBody JsonObject payload, String root) {
    JsonElement rootele = lever.get(payload, lever.newJsonArray(root));
    transverse(payload, lever.asJsonObject(rootele));
    Integer dia = 0;
    Set<Entry<String, JsonElement>> entrySet = payload.entrySet();
    for (Entry<String, JsonElement> entry : entrySet) {
      JsonElement value = entry.getValue();
      Integer through = lever.asInt(lever.get(value, "through"));
      if (through > dia) {
        dia = through;
      }
    }
    return dia;
  }

  private Integer transverse(JsonObject payload, JsonObject node) {
    Integer leftDia = 0;
    Integer rightDia = 0;
    if (lever.isNotNull(lever.get(node, "left"))) {
      leftDia = transverse(payload, lever.asJsonObject(payload.get(lever.getString(node, "left"))));
    }
    if (lever.isNotNull(lever.get(node, "right"))) {
      rightDia =
          transverse(payload, lever.asJsonObject(payload.get(lever.getString(node, "right"))));
    }
    leftDia = leftDia > 0 ? 1 + leftDia : leftDia;
    rightDia = rightDia > 0 ? 1 + rightDia : rightDia;
    lever.set(node, "leftDia", new JsonPrimitive(leftDia));
    lever.set(node, "rightDia", new JsonPrimitive(rightDia));
    lever.set(node, "through", new JsonPrimitive(leftDia + rightDia));
    return leftDia < rightDia ? rightDia : leftDia;
  }

  /**
   * Error.
   *
   * @param e the e
   * @return the json object
   */
  @ExceptionHandler(value = {Exception.class})
  public JsonObject error(Exception e) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("report", "Oops! Something went wrong!");
    jsonObject.addProperty("msg", e.getLocalizedMessage());
    return jsonObject;
  }
}
