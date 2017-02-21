/*
 * Copyright (c) Balajee TM 2017.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Jan, 2017 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.samples;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class SwaggerConfiguration.
 *
 * @author balajeetm
 */
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = { "com.balajeetm.mystique.samples" })
@SuppressWarnings("unchecked")
public class SwaggerConfiguration {

	/** The type resolver. */
	@Autowired
	private TypeResolver typeResolver;

	/**
	 * Mystique api.
	 *
	 * @return the docket
	 */
	@Bean
	public Docket mystiqueApi() {
		return getDocket("mystique-apis", paths(), RequestHandlerSelectors.any(), apiInfo(),
				new Tag("Mystique Web Sample", "Mystique Web Sample ReST Apis"));
	}

	/**
	 * Manapi.
	 *
	 * @return the docket
	 */
	@Bean
	public Docket manapi() {
		return getDocket("mystique-management", PathSelectors.regex("/manage.*"), Predicates.or(getterMethods()),
				managementInfo(),
				new Tag("Mystique Web Sample Management", "Mystique Web Sample Management Apis"));
	}

	private Docket getDocket(String groupName, Predicate<String> pathPattern, Predicate<RequestHandler> apis,
			ApiInfo apiinfo, Tag tag) {
		return new Docket(DocumentationType.SWAGGER_2).groupName(groupName)
				.select()
				.apis(apis)
				.paths(pathPattern)
				.build()
				.apiInfo(apiinfo)
				.pathMapping("/")
				.directModelSubstitute(LocalDate.class, String.class)
				.genericModelSubstitutes(ResponseEntity.class)
				.alternateTypeRules(new AlternateTypeRule(
						typeResolver.resolve(DeferredResult.class,
								typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
						typeResolver.resolve(WildcardType.class)))
				.useDefaultResponseMessages(false)
				.enableUrlTemplating(false)
				.tags(tag);
	}

	/**
	 * Paths.
	 *
	 * @return the predicate
	 */
	private Predicate<String> paths() {
		return Predicates.or(PathSelectors.regex("/mystique.*"));
	}

	/**
	 * Gets the ter methods.
	 *
	 * @return the ter methods
	 */
	private Predicate<RequestHandler> getterMethods() {
		return input -> {
			Set<RequestMethod> methods = input.supportedMethods();
			return CollectionUtils.isEmpty(methods) || methods.contains(RequestMethod.GET);
		};
	}

	/**
	 * Api info.
	 *
	 * @return the api info
	 */
	private ApiInfo apiInfo() {
		return info("Mystique Web Sample APIs", "The ReST APIs for Mystique Web Sample");
	}

	/**
	 * Info.
	 *
	 * @param title the title
	 * @param description the description
	 * @return the api info
	 */
	private ApiInfo info(String title, String description) {
		return new ApiInfo(title, description, "v1", "http://www.balajeetm.com/",
				new Contact("BalajeeTM", "http://www.balajeetm.com/", "balajeetm@gmail.com"),
				"http://www.balajeetm.com/", "http://www.balajeetm.com/");
	}

	/**
	 * Management info.
	 *
	 * @return the api info
	 */
	private ApiInfo managementInfo() {
		return info("Mystique Web Sample Management APIs", "The Management APIs for Mystique Web Sample");
	}

}
