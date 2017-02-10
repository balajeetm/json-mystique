/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 10 Feb, 2017 by balajeetm
 */
package com.balajeetm.mystique.core.config;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.balajeetm.mystique.core.module.MystiqueModule;

/**
 * The Class MystiqueRestTemplateConfig.
 *
 * @author balajeetm
 */
@Configuration
public class MystiqueRestTemplateConfig {

	/**
	 * Rest template configurer.
	 *
	 * @param mystiqueModule the mystique module
	 * @return the rest template configurer
	 */
	@Bean
	public RestTemplateConfigurer restTemplateConfigurer(MystiqueModule mystiqueModule) {
		return new RestTemplateConfigurer(mystiqueModule);
	}

	/**
	 * The Class RestTemplateConfigurer.
	 */
	private static class RestTemplateConfigurer implements BeanPostProcessor {

		/** The mystique module. */
		private MystiqueModule mystiqueModule;

		/**
		 * Instantiates a new rest template configurer.
		 *
		 * @param mystiqueModule the mystique module
		 */
		public RestTemplateConfigurer(MystiqueModule mystiqueModule) {
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
			if (bean instanceof RestTemplate) {
				postProcessRestTemplate((RestTemplate) bean);
			}
			return bean;
		}

		/**
		 * Post process rest template.
		 *
		 * @param restTemplate the rest template
		 */
		private void postProcessRestTemplate(RestTemplate restTemplate) {
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			messageConverters
					.parallelStream()
					.filter(c -> (c instanceof MappingJackson2HttpMessageConverter))
					.forEach(c -> ((MappingJackson2HttpMessageConverter) c)
							.getObjectMapper()
							.registerModule(mystiqueModule));
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
