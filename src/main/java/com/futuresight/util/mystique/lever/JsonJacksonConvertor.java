/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 17 Jul, 2016 by balajeetm
 */
package com.futuresight.util.mystique.lever;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * The Class JsonJacksonConvertor.
 *
 * @author balajeetm
 */
@Component
public final class JsonJacksonConvertor implements ConvertorInterface {

	/** The object mapper. */
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
	public static ConvertorInterface getInstance() {
		return Creator.INSTANCE;
	}

	// Efficient Thread safe Lazy Initialization
	// works only if the singleton constructor is non parameterized
	/**
	 * The Class Creator.
	 */
	private static class Creator {

		/** The instance. */
		public static ConvertorInterface INSTANCE = new JsonJacksonConvertor();
	}

	/**
	 * Instantiates a new json jackson convertor.
	 */
	private JsonJacksonConvertor() {
		getObjectMapper();
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
		try {
			ObjectMapper objectMapper = getObjectMapper();
			return objectMapper.readValue(objectString, objectType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.futuresight.boink.common.objectconvertor.ConvertorInterface#deserialize
	 * (java.lang.Object, java.lang.Class)
	 */
	public <T> T deserialize(Object object, Class<T> objectType) throws ConvertorException {
		try {
			return getObjectMapper().convertValue(object, objectType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.futuresight.boink.common.objectconvertor.ConvertorInterface#deserialize
	 * (java.io.InputStream, java.lang.Class)
	 */
	public <T> T deserialize(InputStream inputStream, Class<T> objectType) throws ConvertorException {
		try {
			ObjectMapper objectMapper = getObjectMapper();
			return objectMapper.readValue(inputStream, objectType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
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

	/**
	 * Gets the object mapper.
	 * 
	 * @return the object mapper
	 */
	public ObjectMapper getObjectMapper() {
		if (null == objectMapper) {
			objectMapper = new ObjectMapper();

			/*
			 * The configurations for a object mapper. Eg. Serialization,
			 * Deserialization, etc. There are enable, disable functions for the
			 * same. We will use an explicit configure so that we can enable and
			 * disable this based on configuration
			 */
			// to prevent exception when encountering unknown property:
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// to allow serialization of "empty" POJOs (no properties to
			// serialize)
			// (without this setting, an exception is thrown in those cases)
			objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			// to write java.util.Date, Calendar as number (timestamp):
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			objectMapper.setSerializationInclusion(Include.NON_NULL);
			// objectMapper.setSerializationInclusion(Include.NON_EMPTY);

			// JsonParser.Feature for configuring parsing settings:
			// to allow use of apostrophes (single quotes), non standard
			objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

			// Add Mix Ins if needed
			// eg. objectMapper.addMixInAnnotations(MagicProductUmbrella.class,
			// Test.class);

			// set various strategies
			// eg.
			// objectMapper.enableDefaultTypingAsProperty(DefaultTyping.OBJECT_AND_NON_CONCRETE,
			// "remoteClass");
		}
		return objectMapper;
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
			return getObjectMapper().writeValueAsString(value);
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
			return (T) getObjectMapper().readValue(inputStream, javaType);
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
			return (List<T>) getObjectMapper().readValue(inputStream, javaType);
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
			return (List<T>) getObjectMapper().convertValue(object, javaType);
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
			return getObjectMapper().readValue(objectString, javaType);
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
			return (List<T>) getObjectMapper().readValue(jsonString, javaType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

}
