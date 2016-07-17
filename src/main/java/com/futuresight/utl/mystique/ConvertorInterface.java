/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.utl.mystique;

import java.io.InputStream;
import java.util.List;

/**
 * The Interface ConvertorInterface.
 *
 * @author balajeetm
 */
public interface ConvertorInterface {

	/**
	 * Deserialize.
	 * 
	 * @param <T> the generic type
	 * @param objectString the object string
	 * @param objectType the object type
	 * @return the t
	 * @throws ConvertorException the convertor exception
	 */
	<T> T deserialize(String objectString, Class<T> objectType) throws ConvertorException;

	/**
	 * Deserialize.
	 *
	 * @param <T> the generic type
	 * @param object the object
	 * @param objectType the object type
	 * @return the t
	 * @throws ConvertorException the convertor exception
	 */
	<T> T deserialize(Object object, Class<T> objectType) throws ConvertorException;

	/**
	 * Deserialize.
	 * 
	 * @param <T> the generic type
	 * @param inputStream the input stream
	 * @param objectType the object type
	 * @return the t
	 * @throws ConvertorException the convertor exception
	 */
	<T> T deserialize(InputStream inputStream, Class<T> objectType) throws ConvertorException;

	/**
	 * Serialize.
	 * 
	 * @param value the value
	 * @return the string
	 * @throws ConvertorException the convertor exception
	 */
	String serialize(Object value) throws ConvertorException;

	/**
	 * Deserialize group.
	 *
	 * @param <T> the generic type
	 * @param <U> the generic type
	 * @param objectString the object string
	 * @param groupClass the group class
	 * @param elementClass the element class
	 * @return the t
	 * @throws ConvertorException the convertor exception
	 */
	<T, U> T deserializeGroup(String objectString, Class<T> groupClass, Class<U> elementClass)
			throws ConvertorException;

	/**
	 * Deserialize group.
	 * 
	 * @param <T> the generic type
	 * @param inputStream the input stream
	 * @param groupClass the group class
	 * @param elementClass the element class
	 * @return the t
	 * @throws ConvertorException the convertor exception
	 */
	<T> T deserializeGroup(InputStream inputStream, Class<T> groupClass, Class<?> elementClass)
			throws ConvertorException;

	/**
	 * Deserialize list.
	 *
	 * @param <T> the generic type
	 * @param inputStream the input stream
	 * @param elementClass the element class
	 * @return the list
	 * @throws ConvertorException the convertor exception
	 */
	<T> List<T> deserializeList(InputStream inputStream, Class<T> elementClass) throws ConvertorException;

	/**
	 * Deserialize list.
	 *
	 * @param <T> the generic type
	 * @param jsonString the json string
	 * @param elementClass the element class
	 * @return the list
	 * @throws ConvertorException the convertor exception
	 */
	<T> List<T> deserializeList(String jsonString, Class<T> elementClass) throws ConvertorException;

	/**
	 * Deserialize list.
	 *
	 * @param <T> the generic type
	 * @param object the object
	 * @param elementClass the element class
	 * @return the list
	 * @throws ConvertorException the convertor exception
	 */
	<T> List<T> deserializeList(Object object, Class<T> elementClass) throws ConvertorException;
}
