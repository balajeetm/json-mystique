/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.balajeetm.mystique.util.json.convertor;

import java.io.InputStream;
import java.util.List;

import com.balajeetm.mystique.util.json.error.ConvertorException;

/**
 * The Interface JsonConvertor.
 *
 * @author balajeetm
 */
public interface JsonConvertor {

	/**
	 * Deserialize a json input string to Java POJO.
	 *
	 * @param <T> the generic type of the Java POJO to be deserialized to
	 * @param jsonString the json input string
	 * @param pojoType the java pojo class
	 * @return the deserialized Java POJO
	 * @throws ConvertorException the convertor exception on any failures during
	 *             conversion
	 */
	<T> T deserialize(String jsonString, Class<T> pojoType) throws ConvertorException;

	/**
	 * Deserialize a Java object to another Java POJO.
	 *
	 * @param <T> the generic type of the Java POJO to be deserialized to
	 * @param object the java object to be deserialized
	 * @param pojoType the java pojo class
	 * @return the deserialized Java POJO
	 * @throws ConvertorException the convertor exception on any failures during
	 *             conversion
	 */
	<T> T deserialize(Object object, Class<T> pojoType) throws ConvertorException;

	/**
	 * Deserialize a json input stream to Java POJO.
	 * 
	 * @param <T> the generic type of the Java POJO to be deserialized to
	 * @param inputStream the json input stream
	 * @param pojoType the java pojo class
	 * @return the deserialized Java POJO
	 * @throws ConvertorException the convertor exception on any failures during
	 *             conversion
	 */
	<T> T deserialize(InputStream inputStream, Class<T> pojoType) throws ConvertorException;

	/**
	 * Serialize.
	 * 
	 * @param pojo the Java POJO to be serialized
	 * @return the json string
	 * @throws ConvertorException the convertor exception on any failures during
	 *             conversion
	 */
	String serialize(Object pojo) throws ConvertorException;

	/**
	 * Deserialize a json string to a Java POJO group.
	 *
	 * @param <T> the generic Java group type to be deserialized to
	 * @param jsonString the json string
	 * @param groupClass the java group class
	 * @param pojoType the java pojo class
	 * @return the deserialized group of java POJOs
	 * @throws ConvertorException the convertor exception on any failures during
	 *             conversion
	 */
	<T> T deserializeGroup(String jsonString, Class<T> groupClass, Class<?> pojoType) throws ConvertorException;

	/**
	 * Deserialize a json input stream to a Java POJO group.
	 * 
	 * @param <T> the generic Java group type to be deserialized to
	 * @param inputStream the json input stream
	 * @param groupClass the java group class
	 * @param pojoType the java pojo class
	 * @return the deserialized group of java POJOs
	 * @throws ConvertorException the convertor exception on any failures during
	 *             conversion
	 */
	<T> T deserializeGroup(InputStream inputStream, Class<T> groupClass, Class<?> pojoType) throws ConvertorException;

	/**
	 * Deserialize a json input stream to a Java POJO list.
	 *
	 * @param <T> the generic type
	 * @param inputStream the json input stream
	 * @param pojoType the java pojo class
	 * @return the deserialized list of java POJOs
	 * @throws ConvertorException the convertor exception on any failures during
	 *             conversion
	 */
	<T> List<T> deserializeList(InputStream inputStream, Class<T> pojoType) throws ConvertorException;

	/**
	 * Deserialize a json string to a Java POJO list.
	 *
	 * @param <T> the generic Java group type to be deserialized to
	 * @param jsonString the json string
	 * @param pojoType the java pojo class
	 * @return the deserialized list of java POJOs
	 * @throws ConvertorException the convertor exception on any failures during
	 *             conversion
	 */
	<T> List<T> deserializeList(String jsonString, Class<T> pojoType) throws ConvertorException;

	/**
	 * Deserialize list.
	 *
	 * @param <T> the generic Java group type to be deserialized to
	 * @param object the Java object group to be serialized
	 * @param pojoType the java pojo class
	 * @return the deserialized list of java POJOs
	 * @throws ConvertorException the convertor exception on any failures during
	 *             conversion
	 */
	<T> List<T> deserializeList(Object object, Class<T> pojoType) throws ConvertorException;
}
