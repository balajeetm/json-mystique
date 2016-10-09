package com.futuresight.util.mystique.lever;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.JsonElement;

@Component
public class MystiqueModule extends SimpleModule {

	@Autowired
	private GsonDeserialiser gsonDeserialiser;

	@Autowired
	private GsonSerialiser gsonSerialiser;

	@PostConstruct
	private void init() {
		this.addDeserializer(JsonElement.class, gsonDeserialiser);
		this.addSerializer(JsonElement.class, gsonSerialiser);
	}

}
