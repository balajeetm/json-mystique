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

import com.balajeetm.mystique.core.JsonMystique;
import com.balajeetm.mystique.samples.util.TestModel;
import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.net.URI;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

  @Autowired @LocalServerPort String localPort;

  @Qualifier("jsonLever")
  @Autowired
  JsonLever lever;

  /**
   * Ping.
   *
   * @return the string
   */
  @GetMapping(value = {"/ping"})
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
  public TestModel pojotest(@RequestBody TestModel body) {
    return body;
  }

  /**
   * Serialise.
   *
   * @param msg the msg
   * @return the json object
   */
  @GetMapping(value = {"/gson/serialise"})
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
  public ResponseEntity<JsonElement> restTemplate() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    RequestEntity<?> requestEntity =
        new RequestEntity<>(
            headers,
            HttpMethod.GET,
            URI.create(
                String.format("http://localhost:%s/mystique/gson/serialise?msg=test", localPort)));
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
  public JsonElement convert(
      @RequestParam(value = "specName", required = false) String specName,
      @RequestBody JsonElement payload) {
    specName = null == specName ? "copy" : specName;
    return jsonMystique.transform(payload, specName);
  }

  @PostMapping(value = {"/height/{root}"})
  public Integer height(@RequestBody JsonObject payload, @PathVariable String root) {
    JsonElement rootele = lever.get(payload, lever.newJsonArray(root));
    transverse(payload, lever.asJsonObject(rootele));
    Integer dia = 0;
    Set<Entry<String, JsonElement>> entrySet = payload.entrySet();
    for (Entry<String, JsonElement> entry : entrySet) {
      JsonElement value = entry.getValue();
      Integer through = lever.asInt(lever.get(value, "through"), 0);
      if (through > dia) {
        dia = through;
      }
    }
    return dia;
  }

  private Integer transverse(JsonObject payload, JsonObject node) {
    Integer leftDia = -1;
    Integer rightDia = -1;
    if (lever.isNotNull(lever.get(node, "left"))) {
      leftDia = transverse(payload, lever.asJsonObject(payload.get(lever.getString(node, "left"))));
    }
    if (lever.isNotNull(lever.get(node, "right"))) {
      rightDia =
          transverse(payload, lever.asJsonObject(payload.get(lever.getString(node, "right"))));
    }
    leftDia = 1 + leftDia;
    rightDia = 1 + rightDia;
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
