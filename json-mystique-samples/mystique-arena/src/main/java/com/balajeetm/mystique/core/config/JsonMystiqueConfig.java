/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.balajeetm.mystique.util.gson.config.GsonUtilConfig;

/**
 * The Class JsonMystiqueConfig.
 *
 * @author balajeetm
 */
@Configuration
@ComponentScan(basePackages = { "com.balajeetm.mystique.core.bean" })
@Import(GsonUtilConfig.class)
public class JsonMystiqueConfig {
}
