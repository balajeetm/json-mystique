/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Jan, 2017 by balajeetm
 */
package com.balajeetm.mystique.samples.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.balajeetm.mystique.core.bean.JsonMystique;
import com.google.gson.JsonElement;
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
	 * Serialise.
	 *
	 * @param msg the msg
	 * @return the json object
	 */
	@GetMapping(value = { "/gson/serialise" })
	public JsonObject serialise(@RequestParam(value = "msg") String msg) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("report", "Gson Serialisation working good!");
		jsonObject.addProperty("status", Boolean.TRUE);
		jsonObject.addProperty("msg", msg);
		return jsonObject;
	}

	/**
	 * Deserialise.
	 *
	 * @param payload the payload
	 * @return the json element
	 */
	@PostMapping(value = { "/gson/deserialise" })
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
	@PostMapping(value = { "/convert" })
	public JsonElement convert(@RequestParam(value = "specName", required = false) String specName,
			@RequestBody JsonElement payload) {
		specName = null == specName ? "copy" : specName;
		return jsonMystique.transform(payload, specName);
	}

	/**
	 * Error.
	 *
	 * @param e the e
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
