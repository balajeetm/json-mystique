/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 2 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.lever.MysCon;
import com.google.gson.JsonObject;

/**
 * A factory for creating Mystique objects.
 *
 * @author balajeetm
 */
@Component
public class MystiqueFactory {

	/** The logger. */
	private Logger logger = Logger.getLogger(this.getClass());

	/** The context. */
	@Autowired
	private ApplicationContext context;

	/** The json lever. */
	@Autowired
	private JsonLever jsonLever;

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
	public Mystique getMystique(JsonObject turn) {
		Mystique mystique = null;
		try {

			if (jsonLever.isNull(turn)) {
				mystique = context.getBean(CopyMystique.class);
			}
			else {
				String turnType = jsonLever.getAsString(turn.get(MysCon.TYPE), MysCon.EMPTY);
				if (StringUtils.isEmpty(turnType) || StringUtils.equalsIgnoreCase(turnType, MysType.copy.name())) {
					mystique = context.getBean(CopyMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.mystique.name())) {
					mystique = context.getBean(JsonMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.bean.name())) {
					String bean = jsonLever.getAsString(turn.get(MysCon.VALUE), MysCon.EMPTY);
					try {
						mystique = (Mystique) context.getBean(Class.forName(bean));
					}
					catch (ClassNotFoundException | ClassCastException e) {
						logger.error(
								String.format("Invalid mystique. Error while getting mystique %s : %s", turn,
										e.getMessage()), e);
					}
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.constant.name())) {
					mystique = context.getBean(ConstantMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.concat.name())) {
					mystique = context.getBean(ConcatMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.arrayToMap.name())) {
					mystique = context.getBean(MapMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.getFromDeps.name())) {
					mystique = context.getBean(GetFromDepsMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.condition.name())) {
					mystique = context.getBean(ConditionMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.dateConvertor.name())) {
					mystique = context.getBean(DateMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.stringUtils.name())) {
					mystique = context.getBean(StringUtilsMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.switched.name())) {
					mystique = context.getBean(SwitchedMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.chain.name())) {
					mystique = context.getBean(ChainMystique.class);
				}
				else {
					logger.error(String.format("Invalid mystique %s", turn));
				}
			}

		}
		catch (NoSuchBeanDefinitionException e) {
			logger.error(String.format("Invalid turn %s defined", turn), e);
		}

		return mystique;
	}

	/**
	 * Gets the date function.
	 *
	 * @param actionJson the action json
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
		}
		catch (NoSuchBeanDefinitionException e) {
			mystFunction = context.getBean(NowFunction.class);
		}
		return mystFunction;
	}
}
