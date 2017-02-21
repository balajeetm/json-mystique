/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Jan, 2017 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.starter;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.balajeetm.mystique.core.bean.JsonMystique;
import com.balajeetm.mystique.core.config.JsonMystiqueConfig;

/**
 * The Class MystiqueAutoConfiguration. This configuration class is responsible
 * for creating all the necessary beans of Json Mystique and automically load
 * and prepare json mystique files available in the classpath for transformation
 *
 * @author balajeetm
 */
@Configuration
@ConditionalOnClass(value = { JsonMystique.class })
@AutoConfigureAfter(value = { WebMvcAutoConfiguration.class, JacksonAutoConfiguration.class,
		HttpMessageConvertersAutoConfiguration.class })
public class MystiqueAutoConfiguration {

	/**
	 * The Class MystiqueConfiguration.
	 */
	@Configuration
	@Import(value = { JsonMystiqueConfig.class })
	protected static class MystiqueConfiguration {
	}
}
