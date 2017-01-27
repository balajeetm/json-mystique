/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Jan, 2017 by balajeetm
 */
package com.balajeetm.mystique.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.balajeetm.mystique.core.config.MystiqueMvcConfig;
import com.balajeetm.mystique.core.mvc.MystiqueModule;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class JacksonGsonAutoConfiguration. This ensures that the jackson object
 * mapper is able to automically serialise and deserialise gson json elements
 *
 * @author balajeetm
 */
@Configuration
@ConditionalOnClass(value = { ObjectMapper.class })
@ConditionalOnWebApplication
@AutoConfigureAfter(value = { WebMvcAutoConfiguration.class, JacksonAutoConfiguration.class,
		HttpMessageConvertersAutoConfiguration.class })
public class JacksonGsonAutoConfiguration {

	/**
	 * The Class MystiqueMvcConfiguration.
	 */
	@Configuration
	@AutoConfigureAfter(value = { MystiqueAutoConfiguration.class })
	@ConditionalOnBean(value = { ObjectMapper.class, MystiqueModule.class })
	@Import(value = { MystiqueMvcConfig.class })
	protected static class MystiqueMvcConfiguration {
	}
}
