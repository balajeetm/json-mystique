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
	public Mystique getMystique(String turn) {
		turn = StringUtils.trimToEmpty(turn);
		Mystique mystique = null;
		try {
			if (StringUtils.isEmpty(turn)) {
				mystique = context.getBean(CopyMystique.class);
			}
			else if (StringUtils.startsWithIgnoreCase(turn, "mys:")) {
				mystique = context.getBean(JsonMystique.class);
			}
			else if (StringUtils.startsWithIgnoreCase(turn, "bean:")) {
				String bean = StringUtils.removeStartIgnoreCase(turn, "bean:");
				try {
					mystique = (Mystique) context.getBean(Class.forName(bean));
				}
				catch (ClassNotFoundException | ClassCastException e) {
					logger.error(
							String.format("Invalid mystique. Error while getting mystique %s : %s", turn,
									e.getMessage()), e);
				}
			}
			else if (StringUtils.startsWithIgnoreCase(turn, "constant:")) {
				mystique = context.getBean(ConstantMystique.class);
			}
			else if (StringUtils.startsWithIgnoreCase(turn, "turn:")) {
				String command = StringUtils.removeStartIgnoreCase(turn, "turn:");
				if ("copy".equalsIgnoreCase(command)) {
					mystique = context.getBean(CopyMystique.class);
				}
				else if ("concat".equalsIgnoreCase(command)) {
					mystique = context.getBean(ConcatMystique.class);
				}
			}
			else {
				logger.error(String.format("Invalid mystique %s", turn));
			}
		}
		catch (NoSuchBeanDefinitionException e) {
			logger.error(String.format("Invalid turn %s defined", turn), e);
		}

		return mystique;
	}
}
