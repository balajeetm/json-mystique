/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.test;

import java.util.List;

import lombok.Data;

/**
 * The Class Tarot.
 *
 * @author balajeetm
 */

/** Instantiates a new sample. */
@Data
public class Sample {

  /** The to. */
  private List<String> to;

  /** The deps. */
  private List<Sample> deps;
}
