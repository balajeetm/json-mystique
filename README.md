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

[Json Mystique](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.balajeetm.mystique%22) downloads at Maven Central

### Maven Dependency Snippet
The maven dependency snippet for various libs is as below

#### Json Mystique - Native Library
---

There are three variants of JsonMystique
* [0.x.x](http://search.maven.org/#artifactdetails%7Ccom.github.balajeetm%7Cjson-mystique%7C0.0.1%7Cjar) - All the processing of JsonMystique is sequential
* [1.x.x](http://search.maven.org/#artifactdetails%7Ccom.github.balajeetm%7Cjson-mystique%7C1.0.8%7Cjar) - JsonMystique parallely processes various parts of the input Json
* [2.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C2.0.4%7Cjar) - Spring Boot compliant Json Mystique with starters and auto configuration

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique</artifactId>
  <version>2.0.4</version>
</dependency>
```

#### NOTE
2.x.x is the only supported version currently. The other versions (0.x.x and 1.x.x) have been deprecated.
It is highly recommended and suggested, to only use the 2.x.x versions of Mystique.
Please raise issues, for support if any, on earlier versions.

> There is absolutely no change in the usage or the syntax and semantics of the two versions. Moving forward, the parallel processing logic would be mainstream

#### Json Mystique - Spring Boot Starter
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique-starter</artifactId>
  <version>2.0.4</version>
</dependency>
```

The json mystique spring boot starter autoconfigures the json mystique environment appropriately, creating all the necessary beans.
It also appropriately configures the [Jackson](https://github.com/FasterXML/jackson) Object Mapper to ensure it can serialise deserialise [Gson](https://github.com/google/gson) objects, iff jackson is in the classpath. In a web environment, it also configures Spring RestTemplate to ensure it can serialise deserialise [Gson](https://github.com/google/gson) objects seamlessly, iff, RestTemplate is in the classpath.

For more details refer the [usage guide](https://github.com/balajeetm/json-mystique/wiki/Usage-Guide) or the [sample projects](https://github.com/balajeetm/json-mystique/tree/master/json-mystique-samples/mystique-web-sample) for usage

#### Json Mystique - Gson Utilities
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>gson-utils</artifactId>
  <version>2.0.4</version>
</dependency>
```

The json mystique gson utility library provides a set of utility classes to operate on raw json objects and serialise deserialise POJOs.
The utility classes are
* [Gson Convertor](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/bean/convertor/GsonConvertor.java) - Utility for POJO Object Model Mapping
* [Json Lever](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/bean/lever/JsonLever.java) - Utility to operate on raw json objects. Simplified and type safe use of the Gson library. Allows deep and shallow merge of jsons
* [Json Query](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/bean/lever/JsonQuery.java) - Utility to perform queries on a json element (can be an object or an array)
* [Json Comparator](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/bean/lever/JsonComparator.java) - Utility to compare two jsons

#### Json Mystique - Jackson Utilities
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>jackson-utils</artifactId>
  <version>2.0.4</version>
</dependency>
```

The json mystique jackson utility library provides a set of utility classes to operate on raw json objects and serialise deserialise POJOs.
The utility classes are
* [Jackson Convertor](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-utils/jackson-utils/src/main/java/com/balajeetm/mystique/util/jackson/bean/convertor/JacksonConvertor.java) - Utility for POJO Object Model Mappingndency>

#### Hop on to the Wiki
---
For more information on the usage and detailed usage guides, refer the [wiki](https://github.com/balajeetm/json-mystique/wiki)
