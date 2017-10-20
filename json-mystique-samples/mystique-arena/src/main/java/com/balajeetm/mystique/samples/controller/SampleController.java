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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.balajeetm.mystique.core.bean.JsonMystique;
import com.balajeetm.mystique.samples.util.MystiqueSampleConfig;
import com.balajeetm.mystique.samples.util.TestModel;
import com.google.gson.JsonObject;

/**
 * The Class SampleController.
 *
 * @author balajeetm
 */
@RestController
@RequestMapping("/mystique")
public class SampleController {

	/** The json mystique. */
	@Autowired
	JsonMystique jsonMystique;

	/** The rest template. */
	@Autowired
	RestTemplate restTemplate;

	/** The config. */
	@Autowired
	MystiqueSampleConfig config;

	/**
	 * Ping.
	 *
	 * @return the string
	 */
	@GetMapping(value = { "/ping" })
	public String ping() {
		return "Ping Working";
	}

	/**
	 * Testmodel.
	 *
	 * @param body
	 *            the body
	 * @return the test model
	 */
	@PostMapping(value = { "/testmodel" })
	public TestModel testmodel(@RequestBody TestModel body) {
		return body;
	}

	/**
	 * Serialise.
	 *
	 * @param msg
	 *            the msg
	 * @return the json object
	 */
	/*	@GetMapping(value = { "/gson/serialise" })
		public JsonObject serialise(@RequestParam(value = "msg") String msg) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("report", "Gson Serialisation working good!");
			jsonObject.addProperty("status", Boolean.TRUE);
			jsonObject.addProperty("msg", msg);
			return jsonObject;
		}
	
		*//**
			 * Rest template.
			 *
			 * @return the json element
			 */
	/*
	@GetMapping(value = { "/resttemplate" })
	public ResponseEntity<JsonElement> restTemplate() {
	HttpHeaders headers = new HttpHeaders();
	headers.set("JsonStub-User-Key", config.getUserKey());
	headers.set("JsonStub-Project-Key", config.getProjectKey());
	headers.setContentType(MediaType.APPLICATION_JSON);
	RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(config.getEndpoint()));
	ResponseEntity<JsonElement> exchange = restTemplate.exchange(requestEntity, JsonElement.class);
	return exchange;
	}
	
	*//**
		 * Deserialise.
		 *
		 * @param payload
		 *            the payload
		 * @return the json element
		 */
	/*
	@PostMapping(value = { "/gson/deserialise" })
	public JsonElement deserialise(@RequestBody JsonElement payload) {
	JsonObject jsonObject = new JsonObject();
	jsonObject.addProperty("report", "Gson Deserialisation working good!");
	jsonObject.addProperty("status", Boolean.TRUE);
	jsonObject.add("payload", payload);
	return jsonObject;
	}
	
	*//**
		 * Convert.
		 *
		 * @param specName
		 *            the spec name
		 * @param payload
		 *            the payload
		 * @return the json element
		 *//*
		@PostMapping(value = { "/convert" })
		public JsonElement convert(@RequestParam(value = "specName", required = false) String specName,
		
		@RequestBody JsonElement payload) {
		specName = null == specName ? "copy" : specName;
		return jsonMystique.transform(payload, specName);
		}*/

	/**
	 * Error.
	 *
	 * @param e
	 *            the e
	 * @return the json object
	 */
	@ExceptionHandler(value = { Exception.class })
	public JsonObject error(Exception e) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("report", "Oops! Something went wrong!");
		jsonObject.addProperty("msg", e.getLocalizedMessage());
		return jsonObject;
	}

}
