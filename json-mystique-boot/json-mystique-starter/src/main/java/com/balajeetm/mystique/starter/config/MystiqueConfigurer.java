/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 20 Oct, 2017 by balajeemohan
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.starter.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.balajeetm.mystique.core.JsonMystique;
import com.balajeetm.mystique.core.MystTurn;

/**
 * The Class MystiqueConfigurer.
 *
 * @author balajeemohan
 */
public class MystiqueConfigurer implements BeanPostProcessor {

  /** The json mystique. */
  private JsonMystique jsonMystique;

  /**
   * Instantiates a new mystique configurer.
   *
   * @param jsonMystique the json mystique
   */
  public MystiqueConfigurer(JsonMystique jsonMystique) {
    this.jsonMystique = jsonMystique;
  }

  /* (non-Javadoc)
   * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
   */
  @Override
  public Object postProcessAfterInitialization(Object bean, String beannName)
      throws BeansException {
    return bean;
  }

  /* (non-Javadoc)
   * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
   */
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    if (bean instanceof MystTurn) {
      postProcessMystique((MystTurn) bean);
    }
    return bean;
  }

  /**
   * Post process mystique.
   *
   * @param turn the turn
   */
  private void postProcessMystique(MystTurn turn) {
    jsonMystique.register(turn);
  }
}
