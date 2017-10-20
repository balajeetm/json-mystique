/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Jan, 2017 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.spring.web.ObjectMapperConfigurer;

/**
 * The Class MystiqueWebSampleApplication.
 *
 * @author balajeetm
 */
@SpringBootApplication
public class MystiqueWebSampleApplication {

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(MystiqueWebSampleApplication.class, args);
  }

  /**
   * Configurer.
   *
   * @return the object mapper configurer
   */
  /* This is to fix the bug in springfox*/
  @Bean
  public ObjectMapperConfigurer configurer() {
    return new ObjectMapperConfigurer();
  }
}
