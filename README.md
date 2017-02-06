[![Build Status](https://travis-ci.org/balajeetm/json-mystique.svg?branch=master)](https://travis-ci.org/balajeetm/json-mystique)

# Json Mystique
Utility for Json Conversion and json transformation in Java

Json mystique is a json transformation library written in and for Java. The library takes an input json as a string or jsonElement and transforms the same to a json string or JsonElement.
The transformation is performed via a ruleSet file that specifies the mappings in a json format

## Features and Primary Use Cases
* The library is useful for transforming from one json to another, even when the input and output jsons are non identical
* Multiple fields from the input can be operated on to map to a single field in the output
* Custom converters can be easily plugged to handle custom domain logic

## Dependencies
* The library uses maven as its build management
* The library primarily depends on 
    * Spring-Context for Spring based wiring and 
    * [Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson) for json String to Java pojo transformations

The actual versions can be found in the pom file


## Maven Download

[Json Mystique](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C2.0.2%7Cjar) downloads at Maven Central

There are three variants of JsonMystique
* 0.x.x - Where all the processing of JsonMystique is sequential
* 1.x.x - JsonMystique parallely processes various parts of the input Json
* 2.x.x - Spring Boot compliant Json Mystique with starters and auto configuration

#### NOTE
2.x.x is the only supported version currently. The other versions (0.x.x and 1.x.x) have been deprecated.
It is highly recommended and suggested, to only use the 2.x.x versions of Mystique.
Please raise issues, for support if any, on earlier versions.

### Maven Dependency Snippet
The maven dependency snippet is as below

```xml
<dependency>
  <groupId>com.github.balajeetm</groupId>
  <artifactId>json-mystique</artifactId>
  <version>0.0.1</version>
</dependency>
```

```xml
<dependency>
  <groupId>com.github.balajeetm</groupId>
  <artifactId>json-mystique</artifactId>
  <version>1.0.8</version>
</dependency>
```

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique</artifactId>
  <version>2.0.2</version>
</dependency>
```

> There is absolutely no change in the usage or the syntax and semantics of the versions. The parallel processing spring boot compliant versions are mainstream

For more information on the usage and detailed usage guides, refer the [wiki](https://github.com/balajeetm/json-mystique/wiki)
