/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.util.gson.convertor;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.gson.internal.$Gson$Types;

/**
 * The Class GsonParametrizedType.
 *
 * @author balajeetm
 */
public final class GsonParametrizedType implements ParameterizedType, Serializable {

	/** The owner type. */
	private final String ownerType;

	/** The group class. */
	private final Class<?> groupClass;

	/** The element class. */
	private final Class<?> elementClass;

	/** The raw type. */
	private final String rawType;

	/** The type arguments. */
	private final List<Type> typeArguments;

	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(this.getClass()
			.getName());

	/**
	 * Instantiates a new gson parametrized type.
	 *
	 * @param groupType the Java group type
	 * @param pojoType the Java POJO type
	 */
	public GsonParametrizedType(String groupType, String pojoType) {

		this.ownerType = null;
		this.rawType = groupType;
		this.groupClass = getClass(groupType);
		this.elementClass = getClass(pojoType);
		this.typeArguments = Lists.newArrayList();
		if (null != elementClass) {
			this.typeArguments.add(elementClass);
		}
	}

	/**
	 * Gets the class for a fully qualified class name.
	 *
	 * @param className the fully qualified class name
	 * @return the java class
	 */
	private Class<?> getClass(String className) {
		Class<?> finalClass = null;
		try {
			finalClass = Class.forName(className);
		} catch (Exception e) {
			logger.warn(String.format("Invalid Class %s", className), e);
		}
		return finalClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.ParameterizedType#getActualTypeArguments()
	 */
	public Type[] getActualTypeArguments() {
		return typeArguments.toArray(new Type[typeArguments.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.ParameterizedType#getRawType()
	 */
	public Type getRawType() {
		return groupClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.ParameterizedType#getOwnerType()
	 */
	public Type getOwnerType() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		return other instanceof ParameterizedType && $Gson$Types.equals(this, (ParameterizedType) other);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(typeArguments.toArray()) ^ rawType.hashCode() ^ hashCodeOrZero(ownerType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s<%s>", rawType, (null == elementClass) ? null : elementClass.getName());
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 0;

	/**
	 * Hash code or zero.
	 *
	 * @param o the o
	 * @return the int
	 */
	private static int hashCodeOrZero(Object o) {
		return o != null ? o.hashCode() : 0;
	}
}
