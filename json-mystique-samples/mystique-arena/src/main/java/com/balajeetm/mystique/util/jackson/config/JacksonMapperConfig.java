/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 24 Jan, 2017 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.jackson.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * The Class JacksonMapperConfig. Configures the Mystique Object Mapper Bean
 *
 * @author balajeetm
 */
@Configuration
public class JacksonMapperConfig {

	/**
	 * Creates the Mystique object mapper bean.
	 *
	 * @return the mystique object mapper with configured defaults
	 */
	@Bean
	public ObjectMapper mystiqueObjectMapper() {

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

		return objectMapper;

	}

}
