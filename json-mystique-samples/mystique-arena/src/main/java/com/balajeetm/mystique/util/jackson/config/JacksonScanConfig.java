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

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The Class JacksonScanConfig. Configures and instantiates all the mystique
 * beans.
 *
 * @author balajeetm
 */
@Configuration
@ComponentScan(basePackages = { "com.balajeetm.mystique.util.jackson.bean" })
public class JacksonScanConfig {

}
