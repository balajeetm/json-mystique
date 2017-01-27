/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Jan, 2017 by balajeetm
 */
package com.balajeetm.mystique.core.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.balajeetm.mystique.core.mvc.MystiqueModule;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class MystiqueMvcConfig.
 */
@Configuration
@ComponentScan(basePackages = { "com.balajeetm.mystique.core.mvc" })
public class MystiqueMvcConfig {

	/**
	 * Object mapper configurer.
	 *
	 * @param mystiqueModule the mystique module
	 * @return the object mapper configurer
	 */
	@Bean
	public static ObjectMapperConfigurer objectMapperConfigurer(MystiqueModule mystiqueModule) {
		return new ObjectMapperConfigurer(mystiqueModule);
	}

	/**
	 * The Class ObjectMapperConfigurer.
	 */
	private static class ObjectMapperConfigurer implements BeanPostProcessor {

		/** The mystique module. */
		private MystiqueModule mystiqueModule;

		/**
		 * Instantiates a new object mapper configurer.
		 *
		 * @param mystiqueModule the mystique module
		 */
		public ObjectMapperConfigurer(MystiqueModule mystiqueModule) {
			this.mystiqueModule = mystiqueModule;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.springframework.beans.factory.config.BeanPostProcessor#
		 * postProcessBeforeInitialization(java.lang.Object, java.lang.String)
		 */
		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			if (bean instanceof ObjectMapper) {
				postProcessObjectMapper((ObjectMapper) bean);
			}
			return bean;
		}

		/**
		 * Post process object mapper.
		 *
		 * @param objectMapper the object mapper
		 */
		private void postProcessObjectMapper(ObjectMapper objectMapper) {
			objectMapper.registerModule(mystiqueModule);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.springframework.beans.factory.config.BeanPostProcessor#
		 * postProcessAfterInitialization(java.lang.Object, java.lang.String)
		 */
		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			return bean;
		}
	}
}
