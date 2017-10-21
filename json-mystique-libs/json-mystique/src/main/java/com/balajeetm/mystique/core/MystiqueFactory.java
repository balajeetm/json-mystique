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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.balajeetm.mystique.core.util.MystiqueConstants;
import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.google.gson.JsonObject;

/**
 * A factory for creating Mystique objects.
 *
 * @author balajeetm
 */
public class MystiqueFactory {

  /** The logger. */
  protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  /** The turns. */
  private Map<Class<? extends MystTurn>, MystTurn> turns;

  /** The json lever. */
  private JsonLever jsonLever;

  /** Instantiates a new mystique factory. */
  private MystiqueFactory() {
    turns = new HashMap<>();
    jsonLever = JsonLever.getInstance();
    init();
  }

  /** Inits the. */
  private void init() {
    AbstractMystTurn turn = new CopyMystTurn();
    turn.setFactory(this);
    turns.put(CopyMystTurn.class, turn);
    turn = new MystiqueMystTurn();
    turn.setFactory(this);
    turns.put(MystiqueMystTurn.class, turn);
    turn = new ConstantMystTurn();
    turn.setFactory(this);
    turns.put(ConstantMystTurn.class, turn);
    turn = new ConcatMystTurn();
    turn.setFactory(this);
    turns.put(ConcatMystTurn.class, turn);
    turn = new ArrayToMapMystTurn();
    turn.setFactory(this);
    turns.put(ArrayToMapMystTurn.class, turn);
    turn = new GetFromDepsMystTurn();
    turn.setFactory(this);
    turns.put(GetFromDepsMystTurn.class, turn);
    turn = new ConditionMystTurn();
    turn.setFactory(this);
    turns.put(ConditionMystTurn.class, turn);
    turn = new DateMystTurn();
    turn.setFactory(this);
    turns.put(DateMystTurn.class, turn);
    turn = new StringUtilsMystTurn();
    turn.setFactory(this);
    turns.put(StringUtilsMystTurn.class, turn);
    ToggleMystTurn toggle = new ToggleMystTurn();
    toggle.setFactory(this);
    turns.put(ToggleMystTurn.class, toggle);
    ChainMystTurn chain = new ChainMystTurn();
    chain.setFactory(this);
    turns.put(ChainMystTurn.class, chain);
  }

  /**
   * Gets the single instance of MystiqueFactory.
   *
   * @return single instance of MystiqueFactory
   */
  public static MystiqueFactory getInstance() {
    return Creator.INSTANCE;
  }

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized
  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static MystiqueFactory INSTANCE = new MystiqueFactory();
  }

  /**
   * Gets the mystique.
   *
   * @param turn the turn
   * @return the mystique
   */
  public MystTurn getMystTurn(JsonObject turn) {
    MystTurn mystique = null;

    if (jsonLever.isNull(turn)) {
      mystique = turns.get(CopyMystTurn.class);
    } else {
      String turnType =
          jsonLever.asString(turn.get(MystiqueConstants.TYPE), MystiqueConstants.EMPTY);
      if (StringUtils.isEmpty(turnType)
          || StringUtils.equalsIgnoreCase(turnType, MysType.copy.name())) {
        mystique = turns.get(CopyMystTurn.class);
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.mystique.name())) {
        mystique = turns.get(MystiqueMystTurn.class);
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.custom.name())) {
        String bean =
            jsonLever.asString(turn.get(MystiqueConstants.VALUE), MystiqueConstants.EMPTY);
        try {
          mystique = turns.get(Class.forName(bean));
        } catch (ClassNotFoundException | ClassCastException e) {
          logger.error(
              String.format(
                  "Invalid mystique. Error while getting mystique %s : %s", turn, e.getMessage()),
              e);
        }
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.constant.name())) {
        mystique = turns.get(ConstantMystTurn.class);
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.concat.name())) {
        mystique = turns.get(ConcatMystTurn.class);
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.arrayToMap.name())) {
        mystique = turns.get(ArrayToMapMystTurn.class);
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.getFromDeps.name())) {
        mystique = turns.get(GetFromDepsMystTurn.class);
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.condition.name())) {
        mystique = turns.get(ConditionMystTurn.class);
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.dateConvertor.name())) {
        mystique = turns.get(DateMystTurn.class);
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.stringUtils.name())) {
        mystique = turns.get(StringUtilsMystTurn.class);
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.toggle.name())) {
        mystique = turns.get(ToggleMystTurn.class);
      } else if (StringUtils.equalsIgnoreCase(turnType, MysType.chain.name())) {
        mystique = turns.get(ChainMystTurn.class);
      } else {
        logger.error(String.format("Invalid mystique %s", turn));
      }
    }

    return mystique;
  }

  /**
   * Register.
   *
   * @param turn the turn
   */
  public void register(MystTurn turn) {
    turns.put(turn.getClass(), turn);
  }

  /**
   * Gets the date function.
   *
   * @param action the action
   * @return the date function
   */
  public MystiqueFunction getDateFunction(String action) {
    MystiqueFunction mystFunction = null;
    switch (action) {
      case MystiqueConstants.TRANSFORM:
        mystFunction = TransformFunction.getInstance();
        break;

      case MystiqueConstants.NOW:
        mystFunction = NowFunction.getInstance();
        break;

      default:
        break;
    }
    return mystFunction;
  }
}
