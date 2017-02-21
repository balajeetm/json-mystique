/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core.bean;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.balajeetm.mystique.core.bean.lever.MystiqueLever;
import com.balajeetm.mystique.util.gson.bean.lever.MysCon;
import com.google.gson.JsonObject;

/**
 * A factory for creating Mystique objects.
 *
 * @author balajeetm
 */
@Component
public class MystiqueFactory {

	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());

	/** The context. */
	@Autowired
	private ApplicationContext context;

	/** The json lever. */
	@Autowired
	private MystiqueLever jsonLever;

	/**
	 * Instantiates a new mystique factory.
	 */
	private MystiqueFactory() {
	}

	/**
	 * Gets the mystique.
	 *
	 * @param turn the turn
	 * @return the mystique
	 */
	public MystTurn getMystTurn(JsonObject turn) {
		MystTurn mystique = null;
		try {

			if (jsonLever.isNull(turn)) {
				mystique = context.getBean(CopyMystTurn.class);
			} else {
				String turnType = jsonLever.getAsString(turn.get(MysCon.TYPE), MysCon.EMPTY);
				if (StringUtils.isEmpty(turnType) || StringUtils.equalsIgnoreCase(turnType, MysType.copy.name())) {
					mystique = context.getBean(CopyMystTurn.class);
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.mystique.name())) {
					mystique = context.getBean(MystiqueMystTurn.class);
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.bean.name())) {
					String bean = jsonLever.getAsString(turn.get(MysCon.VALUE), MysCon.EMPTY);
					try {
						mystique = (MystTurn) context.getBean(Class.forName(bean));
					} catch (ClassNotFoundException | ClassCastException e) {
						logger.error(
								String.format("Invalid mystique. Error while getting mystique %s : %s", turn,
										e.getMessage()),
								e);
					}
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.constant.name())) {
					mystique = context.getBean(ConstantMystTurn.class);
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.concat.name())) {
					mystique = context.getBean(ConcatMystTurn.class);
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.arrayToMap.name())) {
					mystique = context.getBean(ArrayToMapMystTurn.class);
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.getFromDeps.name())) {
					mystique = context.getBean(GetFromDepsMystTurn.class);
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.condition.name())) {
					mystique = context.getBean(ConditionMystTurn.class);
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.dateConvertor.name())) {
					mystique = context.getBean(DateMystTurn.class);
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.stringUtils.name())) {
					mystique = context.getBean(StringUtilsMystTurn.class);
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.toggle.name())) {
					mystique = context.getBean(ToggleMystTurn.class);
				} else if (StringUtils.equalsIgnoreCase(turnType, MysType.chain.name())) {
					mystique = context.getBean(ChainMystTurn.class);
				} else {
					logger.error(String.format("Invalid mystique %s", turn));
				}
			}

		} catch (NoSuchBeanDefinitionException e) {
			logger.error(String.format("Invalid turn %s defined", turn), e);
		}

		return mystique;
	}

	/**
	 * Gets the date function.
	 *
	 * @param action the action
	 * @return the date function
	 */
	public MystFunction getDateFunction(String action) {
		MystFunction mystFunction = null;
		try {
			switch (action) {
			case MysCon.TRANSFORM:
				mystFunction = context.getBean(TransformFunction.class);
				break;

			case MysCon.NOW:
				mystFunction = context.getBean(NowFunction.class);
				break;

			default:
				break;
			}
		} catch (NoSuchBeanDefinitionException e) {
			mystFunction = context.getBean(NowFunction.class);
		}
		return mystFunction;
	}
}
