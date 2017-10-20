/*
 * Copyright (c) TESCO 2017.
 * All rights reserved.
 * No part of this program may be reproduced, translated or transmitted,
 * in any form or by any means, electronic, mechanical, photocopying,
 * recording or otherwise, or stored in any retrieval system of any nature,
 * without written permission of the copyright holder.
 */

/*
 * Created on 27 Jun, 2017 by balajeemohan
 */
package com.balajeetm.mystique.samples;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.balajeetm.mystique.core.config.JsonMystiqueConfig;
import com.balajeetm.mystique.core.config.MystiqueModuleConfig;
import com.balajeetm.mystique.core.config.MystiqueRestTemplateConfig;
import com.balajeetm.mystique.util.jackson.config.JacksonScanConfig;

//@Configuration
/**
 * The Class SampleConfiguration.
 *
 * <p>The configuration class to
 *
 * @author balajeemohan
 */
@Import(
  value = {
    MystiqueModuleConfig.class,
    MystiqueRestTemplateConfig.class,
    JacksonScanConfig.class,
    JsonMystiqueConfig.class
  }
)
public class SampleConfiguration {

  /**
   * Rest template.
   *
   * @return the rest template
   */
  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    // To ensure non 2xx responses do not throw exceptions
    restTemplate.setErrorHandler(
        new ResponseErrorHandler() {

          @Override
          public boolean hasError(ClientHttpResponse response) throws IOException {
            return false;
          }

          @Override
          public void handleError(ClientHttpResponse response) throws IOException {}
        });
    return restTemplate;
  }
}
