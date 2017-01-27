/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Jan, 2017 by balajeetm
 */
package com.balajeetm.mystique.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.balajeetm.mystique.util.jackson.bean.convertor.JacksonConvertor;
import com.balajeetm.mystique.util.jackson.config.JacksonMapperConfig;
import com.balajeetm.mystique.util.jackson.config.JacksonScanConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class JacksonUtilAutoConfig. This config class is responsible for
 * automatically creating mystique jackson convertors and registering them with
 * the available jackson object mappers
 *
 * @author balajeetm
 */
@Configuration
@ConditionalOnClass(value = { ObjectMapper.class })
public class JacksonUtilAutoConfig {

	/**
	 * The Class JacksonConvertorConfiguration.
	 */
	@Configuration
	@ConditionalOnClass(value = { JacksonScanConfig.class, JacksonConvertor.class })
	@Import(value = { JacksonScanConfig.class })
	protected static class JacksonConvertorConfiguration {
	}

	/**
	 * The Class ObjectMapperCreationConfiguration.
	 */
	@Configuration
	@ConditionalOnMissingBean(value = { ObjectMapper.class })
	@ConditionalOnClass(value = { JacksonMapperConfig.class })
	@Import(value = { JacksonMapperConfig.class })
	protected static class ObjectMapperCreationConfiguration {
	}

}
