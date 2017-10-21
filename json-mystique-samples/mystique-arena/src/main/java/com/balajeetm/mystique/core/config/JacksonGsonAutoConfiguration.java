/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Jan, 2017 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core.config;

import java.io.IOException;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.balajeetm.mystique.core.module.MystiqueModule;
import com.balajeetm.mystique.starter.MystiqueAutoConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class JacksonGsonAutoConfiguration. This ensures that the jackson object
 * mapper is able to automically serialise and deserialise gson json elements
 *
 * @author balajeetm
 */
@Configuration
@AutoConfigureAfter(value = { MystiqueAutoConfiguration.class })
@ConditionalOnBean(value = { MystiqueModule.class })
@ConditionalOnClass(value = { ObjectMapper.class })
public class JacksonGsonAutoConfiguration {

	/**
	 * The Class MystiqueMvcConfiguration.
	 */
	@Configuration
	@ConditionalOnWebApplication
	@AutoConfigureAfter(value = { WebMvcAutoConfiguration.class, JacksonAutoConfiguration.class,
			HttpMessageConvertersAutoConfiguration.class })
	@Import(value = { MystiqueModuleConfig.class })
	protected static class MystiqueMvcConfiguration {
	}

	/**
	 * The Class MystiqueMapperConfiguration.
	 */
	@Configuration
	@ConditionalOnMissingBean(value = { MystiqueMvcConfiguration.class })
	@Import(value = { MystiqueModuleConfig.class })
	protected static class MystiqueMapperConfiguration {
	}

	/**
	 * The Class MystiqueRestTemplateConfiguration.
	 */
	@Configuration
	@ConditionalOnClass(value = { RestTemplate.class })
	@AutoConfigureAfter(value = { MystiqueModuleConfig.class })
	@Import(value = { MystiqueRestTemplateConfig.class })
	protected static class MystiqueRestTemplateConfiguration {

		/**
		 * Mystique rest template.
		 *
		 * @return the rest template
		 */
		@Bean
		@ConditionalOnMissingBean(value = { RestTemplate.class })
		@ConditionalOnClass(value = { MystiqueRestTemplateConfig.class })
		public RestTemplate mystiqueRestTemplate() {
			RestTemplate restTemplate = new RestTemplate();
			// To ensure non 2xx responses do not throw exceptions
			restTemplate.setErrorHandler(new ResponseErrorHandler() {

				@Override
				public boolean hasError(ClientHttpResponse response) throws IOException {
					return false;
				}

				@Override
				public void handleError(ClientHttpResponse response) throws IOException {
				}
			});
			return restTemplate;
		}
	}
}
