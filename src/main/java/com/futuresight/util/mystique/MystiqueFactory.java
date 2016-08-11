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
	public Mystique getMystique(JsonElement turn) {
		Mystique mystique = null;
		try {

			if (jsonLever.isNull(turn)) {
				mystique = context.getBean(CopyMystique.class);
			}
			else if (turn.isJsonArray()) {
				mystique = context.getBean(MultiTurnMystique.class);
			}
			else {
				String turnType = null;
				JsonObject turnObject = turn.getAsJsonObject();
				JsonElement turnTypeEle = turnObject.get("type");
				turnType = (jsonLever.isNotNull(turnTypeEle)) ? turnTypeEle.getAsString() : "";
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
				else if (StringUtils.equalsIgnoreCase(turnType, MysType.dateConvertor.name())) {
					mystique = context.getBean(DateMystique.class);
				}
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

	public MystFunction getFunction(JsonElement actionJson) {
		String action = jsonLever.isNotNull(actionJson) && actionJson.isJsonPrimitive() ? StringUtils.trimToEmpty(
				actionJson.getAsString()).toLowerCase() : "now";
		MystFunction mystFunction = null;
		try {
			switch (action) {
			case "transform":
				mystFunction = context.getBean(TransformFunction.class);
				break;

			case "now":
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
