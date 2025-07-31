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

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    return other instanceof ParameterizedType && parameterizedTypeEquals(this, (ParameterizedType) other);
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

  /**
   * Compares two {@link ParameterizedType} instances for semantic equality.
   *
   * <p>This method checks that both types have the same:
   *
   * <ul>
   *   <li>Owner type (e.g., enclosing class)
   *   <li>Raw type (e.g., List, Map)
   *   <li>Actual type arguments (e.g., List&lt;String&gt; vs List&lt;Integer&gt;)
   * </ul>
   *
   * This is functionally equivalent to the internal behavior of Gson's {@code $Gson$Types.equals}
   * and can be used as a safe replacement in modern Java or Gson-based code.
   *
   * @param a the first {@code ParameterizedType} to compare
   * @param b the second {@code ParameterizedType} to compare
   * @return {@code true} if both parameterized types are structurally equal; {@code false}
   *     otherwise
   */
  private boolean parameterizedTypeEquals(ParameterizedType a, ParameterizedType b) {
    return Objects.equals(a.getOwnerType(), b.getOwnerType())
        && Objects.equals(a.getRawType(), b.getRawType())
        && Arrays.equals(a.getActualTypeArguments(), b.getActualTypeArguments());
  }
}
