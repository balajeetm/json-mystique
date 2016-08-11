package com.futuresight.util.mystique.lever;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.gson.internal.$Gson$Types;

public final class GsonParametrizedType implements ParameterizedType, Serializable {
	private final String ownerType;
	private final Class groupClass;
	private final Class elementClass;
	private final String rawType;
	private final List<Type> typeArguments;
	private static Logger logger = Logger.getLogger(GsonParametrizedType.class);

	public GsonParametrizedType(String groupType, String elementType) {

		this.ownerType = null;
		this.rawType = groupType;
		this.groupClass = getClass(groupType);
		this.elementClass = getClass(elementType);
		this.typeArguments = Lists.newArrayList();
		if (null != elementClass) {
			this.typeArguments.add(elementClass);
		}
	}

	private Class getClass(String className) {
		Class finalClass = null;
		try {
			finalClass = Class.forName(className);
		}
		catch (Exception e) {
			logger.warn(String.format("Invalid Class %s", className), e);
		}
		return finalClass;
	}

	public Type[] getActualTypeArguments() {
		return typeArguments.toArray(new Type[typeArguments.size()]);
	}

	public Type getRawType() {
		return groupClass;
	}

	public Type getOwnerType() {
		return null;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof ParameterizedType && $Gson$Types.equals(this, (ParameterizedType) other);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(typeArguments.toArray()) ^ rawType.hashCode() ^ hashCodeOrZero(ownerType);
	}

	@Override
	public String toString() {
		return String.format("%s<%s>", rawType, (null == elementClass) ? null : elementClass.getName());
	}

	private static final long serialVersionUID = 0;

	private static int hashCodeOrZero(Object o) {
		return o != null ? o.hashCode() : 0;
	}
}
