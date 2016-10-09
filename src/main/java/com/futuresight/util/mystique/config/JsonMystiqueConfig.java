/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.futuresight.util.mystique.lever.MystiqueModule;

/**
 * The Class JsonMystiqueConfig.
 *
 * @author balajeetm
 */
@Configuration
@ComponentScan(basePackages = {"com.futuresight.util.mystique"})
public class JsonMystiqueConfig {

	/**
	 * Mystique object mapper.
	 *
	 * @param mystiqueModule the mystique module
	 * @return the object mapper
	 */
	@Bean
	public ObjectMapper mystiqueObjectMapper(MystiqueModule mystiqueModule) {

		ObjectMapper objectMapper = new ObjectMapper();

		// to prevent exception when encountering unknown property:
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// to allow serialization of "empty" POJOs (no properties to serialize)
		// (without this setting, an exception is thrown in those cases)
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		// to write java.util.Date, Calendar as number (timestamp):
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		objectMapper.setSerializationInclusion(Include.NON_NULL);

		// JsonParser.Feature for configuring parsing settings:
		// to allow use of apostrophes (single quotes), non standard
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

		objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

		// Add Mix Ins if needed
		// eg. objectMapper.addMixInAnnotations(MagicProductUmbrella.class,
		// Test.class);

		// set various strategies
		// eg.
		// objectMapper.enableDefaultTypingAsProperty(DefaultTyping.OBJECT_AND_NON_CONCRETE,
		// "remoteClass");

		objectMapper.registerModule(mystiqueModule);

		return objectMapper;

	}
}
