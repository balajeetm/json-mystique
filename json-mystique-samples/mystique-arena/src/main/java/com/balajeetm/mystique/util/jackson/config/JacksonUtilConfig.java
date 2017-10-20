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

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The Class JacksonUtilConfig. The Mystique Bean configurer.
 * 
 * Creates all the mystique beans, including an Object Mapper. If you do not
 * wish to create an object mapper, use {@link JacksonScanConfig} instead
 *
 * @author balajeetm
 */
@Configuration
@Import(value = { JacksonScanConfig.class, JacksonMapperConfig.class })
public class JacksonUtilConfig {
}
