/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique.lever;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * The Class JacksonConvertor.
 *
 * @author balajeetm
 */
@Component
public class JacksonConvertor implements JsonConvertor {

	/** The object mapper. */
	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		Creator.INSTANCE = this;
	}

	/**
	 * Gets the single instance of JsonJacksonConvertor.
	 *
	 * @return single instance of JsonJacksonConvertor
	 */
	public static JsonConvertor getInstance() {
		return Creator.INSTANCE;
	}

	// Efficient Thread safe Lazy Initialization
	// works only if the singleton constructor is non parameterized
	/**
	 * The Class Creator.
	 */
	private static class Creator {

		/** The instance. */
		public static JsonConvertor INSTANCE = new JacksonConvertor();
	}

	/**
	 * Instantiates a new json jackson convertor.
	 */
	private JacksonConvertor() {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.cisco.cab.utils.convertor.ConvertorInterface#deserialize(java.lang
	 * .String, java.lang.Class)
	 */
	/**
	 * Deserialize.
	 *
	 * @param <T> the generic type
	 * @param objectString the object string
	 * @param objectType the object type
	 * @return the t
	 * @throws ConvertorException the convertor exception
	 */
	public <T> T deserialize(String objectString, Class<T> objectType) throws ConvertorException {
		T value = null;
		try {
			value = null != objectString ? objectMapper.readValue(objectString, objectType) : value;
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.futuresight.boink.common.objectconvertor.ConvertorInterface#deserialize
	 * (java.lang.Object, java.lang.Class)
	 */
	public <T> T deserialize(Object object, Class<T> objectType) throws ConvertorException {
		T value = null;
		try {
			value = null != object ? objectMapper.convertValue(object, objectType) : value;
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.futuresight.boink.common.objectconvertor.ConvertorInterface#deserialize
	 * (java.io.InputStream, java.lang.Class)
	 */
	public <T> T deserialize(InputStream inputStream, Class<T> objectType) throws ConvertorException {
		T value = null;
		try {
			value = null != inputStream ? objectMapper.readValue(inputStream, objectType) : value;
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
		return value;
	}

	/**
	 * Gets the convertor exception.
	 * 
	 * @param e the e
	 * @return the convertor exception
	 */
	private ConvertorException getConvertorException(Exception e) {
		return new ConvertorException(e);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.cisco.cab.utils.convertor.ConvertorInterface#serialize(java.lang.
	 * Object)
	 */
	/**
	 * Serialize.
	 *
	 * @param value the value
	 * @return the string
	 * @throws ConvertorException the convertor exception
	 */
	public String serialize(Object value) throws ConvertorException {
		try {
			return objectMapper.writeValueAsString(value);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.futuresight.boink.common.objectconvertor.ConvertorInterface#
	 * deserializeGroup(java.io.InputStream, java.lang.Class, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T deserializeGroup(InputStream inputStream, Class<T> groupClass, Class<?> elementClass)
			throws ConvertorException {
		JavaType javaType = getJavaType(groupClass, elementClass);
		try {
			return (T) objectMapper.readValue(inputStream, javaType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.futuresight.boink.common.objectconvertor.ConvertorInterface#
	 * deserializeList(java.io.InputStream, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> deserializeList(InputStream inputStream, Class<T> elementClass) throws ConvertorException {
		JavaType javaType = getJavaType(List.class, elementClass);
		try {
			return (List<T>) objectMapper.readValue(inputStream, javaType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.futuresight.boink.common.objectconvertor.ConvertorInterface#
	 * deserializeList(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> deserializeList(Object object, Class<T> elementClass) throws ConvertorException {
		JavaType javaType = getJavaType(List.class, elementClass);
		try {
			return (List<T>) objectMapper.convertValue(object, javaType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.cisco.cab.utils.convertor.ConvertorInterface#groupSerialize(java.
	 * lang.String, java.lang.Class, java.lang.Class)
	 */
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
	public <T, U> T deserializeGroup(String objectString, Class<T> groupClass, Class<U> elementClass)
			throws ConvertorException {
		return deserializeGroupOnJavaType(objectString, groupClass, elementClass);
	}

	/**
	 * Deserialize group on java type.
	 *
	 * @param <T> the generic type
	 * @param <U> the generic type
	 * @param objectString the object string
	 * @param groupClass the group class
	 * @param elementClass the element class
	 * @return the t
	 */
	private <T, U> T deserializeGroupOnJavaType(String objectString, Class<T> groupClass, Class<U> elementClass) {
		JavaType javaType = getJavaType(groupClass, elementClass);
		try {
			return objectMapper.readValue(objectString, javaType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/**
	 * Gets the java type.
	 *
	 * @param <T> the generic type
	 * @param <U> the generic type
	 * @param groupClass the group class
	 * @param elementClass the element class
	 * @return the java type
	 */
	private <T, U> JavaType getJavaType(Class<T> groupClass, Class<U> elementClass) {
		JavaType javaType = null;
		if (List.class.isAssignableFrom(groupClass)) {
			javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, elementClass);
		}
		else if (Collection.class.isAssignableFrom(groupClass)) {
			javaType = TypeFactory.defaultInstance().constructCollectionType(Collection.class, elementClass);
		}
		return javaType;
	}

	/* (non-Javadoc)
	 * @see com.futuresight.utl.mystique.ConvertorInterface#deserializeList(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> deserializeList(String jsonString, Class<T> elementClass) throws ConvertorException {
		JavaType javaType = getJavaType(List.class, elementClass);
		try {
			return (List<T>) objectMapper.readValue(jsonString, javaType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

}
