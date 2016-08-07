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

import com.google.gson.JsonElement;
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
	public Mystique getMystique(JsonElement turn) {
		Mystique mystique = null;
		try {
			if (null != turn && turn.isJsonArray()) {
				mystique = context.getBean(MultiTurnMystique.class);
			}
			else {
				JsonObject turnObject = null == turn ? null : turn.getAsJsonObject();
				JsonElement turnTypeEle = null == turnObject ? null : turnObject.get("type");
				String turnType = (null == turnTypeEle) ? "" : turnTypeEle.getAsString();
				if (StringUtils.isEmpty(turnType) || StringUtils.equalsIgnoreCase(turnType, MysType.copy.name())) {
					mystique = context.getBean(CopyMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.mystique.name())) {
					mystique = context.getBean(JsonMystique.class);
				}
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.bean.name())) {
					String bean = StringUtils.trimToEmpty(turnObject.get("value").getAsString());
					try {
						mystique = (Mystique) context.getBean(Class.forName(bean));
					}
					catch (ClassNotFoundException | ClassCastException e) {
						logger.error(
								String.format("Invalid mystique. Error while getting mystique %s : %s", turnObject,
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
				/*else if (StringUtils.startsWithIgnoreCase(turn, "turn:")) {
					String command = StringUtils.removeStartIgnoreCase(turn, "turn:");
					if ("copy".equalsIgnoreCase(command)) {
						mystique = context.getBean(CopyMystique.class);
					}
					else if ("concat".equalsIgnoreCase(command)) {
						mystique = context.getBean(ConcatMystique.class);
					}
				}*/
				else {
					logger.error(String.format("Invalid mystique %s", turnObject));
				}
			}

		}
		catch (NoSuchBeanDefinitionException e) {
			logger.error(String.format("Invalid turn %s defined", turn), e);
		}

		return mystique;
	}
}
