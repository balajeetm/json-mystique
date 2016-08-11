/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 */

/*
 * Created on 11 Aug, 2016 by balajeetm
 */
package com.futuresight.util.mystique.lever;

import static com.google.gson.internal.$Gson$Preconditions.checkArgument;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuresight.util.mystique.JsonLever;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * The Class GsonJacksonConvertor.
 *
 * @author balajmoh
 */
@Component
public class GsonJacksonConvertor implements ConvertorInterface {

	@Autowired
	private JsonLever jsonLever;

	/** The gson. */
	private Gson gson;

	/**
	 * Instantiates a new gson jackson convertor.
	 */
	private GsonJacksonConvertor() {
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	protected void init() {
		Creator.INSTANCE = this;
		gson = jsonLever.getGson();
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
		public static ConvertorInterface INSTANCE = new GsonJacksonConvertor();
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.lever.ConvertorInterface#deserialize(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T deserialize(String objectString, Class<T> objectType) throws ConvertorException {
		try {
			return gson.fromJson(objectString, objectType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.lever.ConvertorInterface#deserialize(java.lang.Object, java.lang.Class)
	 */
	@Override
	public <T> T deserialize(Object object, Class<T> objectType) throws ConvertorException {
		try {
			JsonElement jsonElement = getJsonElement(object);
			return gson.fromJson(jsonElement, objectType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.lever.ConvertorInterface#deserialize(java.io.InputStream, java.lang.Class)
	 */
	@Override
	public <T> T deserialize(InputStream inputStream, Class<T> objectType) throws ConvertorException {
		try {
			return gson.fromJson(new InputStreamReader(inputStream), objectType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.lever.ConvertorInterface#serialize(java.lang.Object)
	 */
	@Override
	public String serialize(Object value) throws ConvertorException {
		try {
			return gson.toJson(value);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.lever.ConvertorInterface#deserializeGroup(java.lang.String, java.lang.Class, java.lang.Class)
	 */
	@Override
	public <T, U> T deserializeGroup(String objectString, Class<T> groupClass, Class<U> elementClass)
			throws ConvertorException {
		try {
			checkArgument(groupClass != null && elementClass != null);
			GsonParametrizedType parametrizedType = new GsonParametrizedType(groupClass.getName(),
					elementClass.getName());

			return gson.fromJson(objectString, parametrizedType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.lever.ConvertorInterface#deserializeGroup(java.io.InputStream, java.lang.Class, java.lang.Class)
	 */
	@Override
	public <T> T deserializeGroup(InputStream inputStream, Class<T> groupClass, Class<?> elementClass)
			throws ConvertorException {
		try {
			checkArgument(groupClass != null && elementClass != null);
			GsonParametrizedType parametrizedType = new GsonParametrizedType(groupClass.getName(),
					elementClass.getName());

			return gson.fromJson(new InputStreamReader(inputStream), parametrizedType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.lever.ConvertorInterface#deserializeList(java.io.InputStream, java.lang.Class)
	 */
	@Override
	public <T> List<T> deserializeList(InputStream inputStream, Class<T> elementClass) throws ConvertorException {
		try {
			checkArgument(elementClass != null);
			GsonParametrizedType parametrizedType = new GsonParametrizedType(List.class.getName(),
					elementClass.getName());

			return gson.fromJson(new InputStreamReader(inputStream), parametrizedType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.lever.ConvertorInterface#deserializeList(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> List<T> deserializeList(String jsonString, Class<T> elementClass) throws ConvertorException {
		try {
			checkArgument(elementClass != null);
			GsonParametrizedType parametrizedType = new GsonParametrizedType(List.class.getName(),
					elementClass.getName());

			return gson.fromJson(jsonString, parametrizedType);
		}
		catch (Exception e) {
			throw getConvertorException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.futuresight.util.mystique.lever.ConvertorInterface#deserializeList(java.lang.Object, java.lang.Class)
	 */
	@Override
	public <T> List<T> deserializeList(Object object, Class<T> elementClass) throws ConvertorException {
		try {
			checkArgument(elementClass != null);
			JsonElement jsonElement = getJsonElement(object);
			GsonParametrizedType parametrizedType = new GsonParametrizedType(List.class.getName(),
					elementClass.getName());

			return gson.fromJson(jsonElement, parametrizedType);
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
	 * Gets the json element.
	 *
	 * @param obj the obj
	 * @return the json element
	 */
	private JsonElement getJsonElement(Object obj) {
		return (obj instanceof JsonElement) ? (JsonElement) obj : gson.toJsonTree(obj);
	}
}
