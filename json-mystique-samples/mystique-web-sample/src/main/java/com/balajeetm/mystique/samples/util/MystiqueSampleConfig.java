/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 10 Feb, 2017 by balajeetm
 * 
 */
package com.balajeetm.mystique.samples.util;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

/**
 * The Class MystiqueSampleConfig.
 *
 * @author balajeetm
 */
@ConfigurationProperties(prefix = "mystique.rest")
@Validated
@Component

/**
 * Instantiates a new mystique sample config.
 */
@Data
public class MystiqueSampleConfig {

	/** The user key. */
	String userKey;

	/** The project key. */
	String projectKey;

	/** The endpoint. */
	@NotEmpty
	String endpoint;

}
