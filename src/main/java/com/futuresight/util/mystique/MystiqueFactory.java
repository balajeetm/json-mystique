package com.futuresight.util.mystique;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.Mystique.MystiqueType;

@Component
public class MystiqueFactory {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private ApplicationContext context;

	private Map<String, Class<? extends Mystique>> standardMystiques;

	private MystiqueFactory() {
		standardMystiques = new HashMap<String, Class<? extends Mystique>>();
		standardMystiques.put(null, CopyMystique.class);
		standardMystiques.put(MystiqueType.COPY.name(), CopyMystique.class);
		standardMystiques.put(MystiqueType.CONCAT.name(), ConcatMystique.class);
	}

	@SuppressWarnings("unchecked")
	public Mystique getMystique(String myst) {
		Mystique mystique = null;
		Class<? extends Mystique> mystClass = standardMystiques.get(StringUtils.upperCase(myst));
		if (mystClass == null) {
			try {
				mystClass = (Class<? extends Mystique>) Class.forName(myst);
			}
			catch (ClassNotFoundException | ClassCastException e) {
				logger.error(
						String.format("Invalid mystique. Error while getting mystique %s : %s", myst, e.getMessage()),
						e);
			}
		}

		mystique = context.getBean(mystClass);
		return mystique;
	}

	public String getConvertor(String convertor) {
		convertor = StringUtils.trimToEmpty(convertor);
		return StringUtils.isEmpty(convertor) ? MystiqueType.COPY.name() : convertor;
	}
}
