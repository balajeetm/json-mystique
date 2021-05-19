/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.balajeetm.mystique.core.lever.MystiqueLever;
import com.balajeetm.mystique.core.util.MystiqueConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import lombok.Setter;

/**
 * The Class AbstractMystTurn.
 *
 * @author balajeetm
 */
public abstract class AbstractMystTurn implements MystTurn {

  /** The mystique lever. */
  protected MystiqueLever mystiqueLever;

  @Setter protected MystiqueFactory factory;

  /** Instantiates a new abstract myst turn. */
  public AbstractMystTurn() {
    mystiqueLever = MystiqueLever.getInstance();
  }

  /** The logger. */
  protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  /*
   * (non-Javadoc)
   *
   * @see
   * com.futuresight.util.mystique.SimpleTurnMystique#transform(java.util.
   * List, com.google.gson.JsonObject, com.google.gson.JsonObject,
   * com.google.gson.JsonElement)
   */
  public JsonElement transform(
      List<JsonElement> source,
      JsonObject deps,
      JsonObject aces,
      JsonObject turn,
      JsonObject resultWrapper) {

    Boolean turnExists = mystiqueLever.isNotNull(turn);
    JsonObject updatedAces = new JsonObject();
    if (turnExists) {
      JsonObject localAces =
          mystiqueLever.asJsonObject(turn.get(MystiqueConstants.ACES), (JsonObject) null);
      if (mystiqueLever.isNotNull(localAces)) {
        // Aces will only work on first source
        JsonElement first = mystiqueLever.getFirst(source);
        updatedAces = mystiqueLever.getUpdatedAces(first, localAces, deps, updatedAces);
      }
    }

    mystiqueLever.simpleMerge(updatedAces, mystiqueLever.asJsonObject(aces, new JsonObject()));

    JsonElement transform = JsonNull.INSTANCE;
    try {
      transform = transmute(source, deps, updatedAces, turn);
    } catch (RuntimeException e) {
      // Any error during transmute, log error
      String msg =
          String.format(
              "Error transforming input with specification for turn %s - %s", turn, e.getMessage());
      logger.info(msg, e);
    }

    if (turnExists) {
      if (mystiqueLever.isNull(transform)) {
        JsonObject defaultJson = mystiqueLever.asJsonObject(turn.get(MystiqueConstants.DEFAULT));
        transform = transformToDefault(defaultJson, source, deps, updatedAces);
      }

      // set the result
      JsonArray to = mystiqueLever.getJpath(turn.get(MystiqueConstants.TO));
      Boolean optional =
          mystiqueLever.asBoolean(turn.get(MystiqueConstants.OPTIONAL), Boolean.FALSE);

      mystiqueLever.set(resultWrapper, to, transform, updatedAces, optional);
    }

    return transform;
  }

  /**
   * Transform on condition.
   *
   * @param conditionalJson the conditional json
   * @param source the source
   * @param deps the deps
   * @param aces the aces
   * @param resultWrapper the result wrapper
   * @return the json element
   */
  protected JsonElement transformToDefault(
      JsonObject conditionalJson,
      List<JsonElement> source,
      JsonObject deps,
      JsonObject aces,
      JsonObject resultWrapper) {
    JsonElement transform = JsonNull.INSTANCE;
    if (mystiqueLever.isNotNull(conditionalJson)) {
      // Should not be null, can be json null
      JsonElement value = conditionalJson.get(MystiqueConstants.VALUE);
      if (null != value) {
        transform = value;
      } else {
        JsonObject defaultTurn =
            mystiqueLever.asJsonObject(conditionalJson.get(MystiqueConstants.TURN));
        if (mystiqueLever.isNotNull(defaultTurn)) {
          MystTurn mystique = factory.getMystTurn(defaultTurn);
          transform = mystique.transform(source, deps, aces, defaultTurn, resultWrapper);
        }
      }
    }
    return transform;
  }

  /**
   * Transform on condition.
   *
   * @param conditionalJson the conditional json
   * @param source the source
   * @param deps the deps
   * @param aces the aces
   * @return the json element
   */
  protected JsonElement transformToDefault(
      JsonObject conditionalJson, List<JsonElement> source, JsonObject deps, JsonObject aces) {
    // Do not update the eventual result
    return transformToDefault(conditionalJson, source, deps, aces, new JsonObject());
  }

  /**
   * Transmute.
   *
   * @param source the source
   * @param deps the deps
   * @param aces the aces
   * @param turn the turn
   * @return the json element
   */
  protected abstract JsonElement transmute(
      List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn);

  /**
   * Gets the granular source.
   *
   * @param source the source
   * @param turn the turn
   * @param deps the deps
   * @param aces the aces
   * @return the granular source
   */
  protected JsonElement getGranularSource(
      JsonElement source, JsonObject turn, JsonObject deps, JsonObject aces) {
    JsonArray from =
        mystiqueLever.isNotNull(turn)
            ? mystiqueLever.getJpath(turn.get(MystiqueConstants.FROM))
            : null;
    JsonElement conditionSource =
        mystiqueLever.isNull(from) ? source : mystiqueLever.getField(source, from, deps, aces);
    return conditionSource;
  }
}
