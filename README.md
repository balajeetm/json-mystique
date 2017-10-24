[![Build Status](https://travis-ci.org/balajeetm/json-mystique.svg?branch=master)](https://travis-ci.org/balajeetm/json-mystique)

# Json Mystique
Utility for Json Manipulation, Json Conversion and Json transformation in Java.<br>
This is vaguely similar to [lodash's](https://lodash.com/) object manipulation capabilities but adds a lot more punch to it.

Json mystique ships a set of libraries written in and for Java, useful for json manipulation & transformation 
*   The library exposes simple `get`, `set` and ``default` methods for json manipulation.<br>
```
Eg. JsonElement json = { 'a': [{ 'b': { 'c': 3 } }] };
jsonLever.get(json, 'a.0.b.c');
// => 3
``` 
*   The library takes an input json as a string or [JsonElement](https://google.github.io/gson/apidocs/com/google/gson/JsonElement.html) and transforms the same to a json string or [JsonElement](https://google.github.io/gson/apidocs/com/google/gson/JsonElement.html).
The transformation is performed via a ruleSet file that specifies the mappings in a json format

## Features and Primary Use Cases
*   The library is useful for simple Json representation, access and manipulation in Java
```
Eg. JsonElement json = { 'a': [{ 'b': { 'c': 3 } }] };
jsonLever.get(json, 'a.0.b.c');
// => 3
jsonLever.get(object, ["a", "0", "b", "c"]);
// => 3
jsonLever.get(object, "a.b.c", "default");
// => "default"
``` 
*   The library is useful for transforming from one json to another, even when the input and output jsons are non identical
    *   Multiple fields from the input can be operated on to map to a single field in the output
    *   Custom converters can be easily plugged to handle custom domain logic
*   The library exposes a standard interface for converting POJO to json and vice versa
    *   The standard interface is implemented in both Gson and Jackson
*   The library is useful for getting subset or projections of a json object
*   The library is useful for comparing two jsons
    *   Compare if two jsons are identical
    *   Compare if one json is a subset of another
*   The library exposes a query interface on a json, to retrieve subset of fields from the source

## Dependencies
*   The library uses maven for its build management
*   The library can be primarily used as a stand-alone java lib and depends on  
    *   [Gson - 2.8.2](https://mvnrepository.com/artifact/com.google.code.gson/gson) for json String to Java pojo transformations
    *   [Jackson - 2.8.10](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind) for json String to Java pojo transformations
*   For the [Spring](https://spring.io/) fan-boys, Json Mystique also ships a Spring-Starter, which depends on
    *   [Spring Boot - 1.5.8.RELEASE](http://docs.spring.io/spring-boot/docs/1.5.8.RELEASE/reference/htmlsingle/)
    *   [Spring-Framework - 4.3.12.RELEASE](http://docs.spring.io/spring/docs/4.3.12.RELEASE/spring-framework-reference/htmlsingle/) for IOC

The actual versions can be found in the [pom file](/pom.xml)

## Maven Download

[Json Mystique](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.balajeetm.mystique%22) and all its libraries downloads at Maven Central.<br>
The different library utils and their capabilities are detailed below:

### Maven Dependency Snippet
The maven dependency snippet for various libs is as below

#### Json Mystique - Native Library
---

> **NOTE**<br>
There are three variants of JsonMystique.<br>
The `1.x.x` and `0.x.x` versions have reached EOL. `2.x.x` is the only active version as of now<br><br>
[2.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C2.0.7%7Cjar) - Standalone json transformation utility<br>
[1.x.x](http://search.maven.org/#artifactdetails%7Ccom.github.balajeetm%7Cjson-mystique%7C1.0.8%7Cjar) - Parallel processing json transformation utility<br>
[0.x.x](http://search.maven.org/#artifactdetails%7Ccom.github.balajeetm%7Cjson-mystique%7C0.0.1%7Cjar) - Sequential processing json transformation utility

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique</artifactId>
  <version>2.0.8</version>
</dependency>
```

> NOTE<br>
2.x.x is the only supported version currently since the parallel processing logic is mainstream. The other versions (0.x.x and 1.x.x) have been deprecated.<br>
It is highly recommended and suggested, to only use the 2.x.x versions of Mystique.<br>
Please raise issues, for support if any, on earlier versions.<br>

#### Json Mystique - Spring Boot Starter
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique-starter</artifactId>
  <version>2.0.8</version>
</dependency>
```

The custom [spring-boot-starter](https://docs.spring.io/spring-boot/docs/1.5.8.RELEASE/reference/htmlsingle/#boot-features-custom-starter) for JsonMystique. Autoconfigures all necessary beans and seamlessly integrates with Spring's RestControllers and Jackson libraries.

The json mystique's spring boot starter autoconfigures the json mystique environment appropriately, creating all the necessary beans. Its primary features are
*   Configures the simple [Jackson](https://github.com/FasterXML/jackson) Json Convertor as a bean
*   Configures the simple [Gson](https://github.com/google/gson) Json Convertor as a bean
*   Configures Jackson Object Mapper with the ability to serialise and deserialise Gson Objects (JsonElement)
*   Configures JsonMystique with its appropriate turns as a bean
*   If Spring-web is in the classpath, it connfigures a RestTemplate with the ability to serialise and deserialise Gson Objects (JsonElement)
*   Automatically registers custom Json Mystique `MystTurns` with JsonMystique

For more details refer the [usage guide](Usage-Guide.md) or the [sample projects](json-mystique-samples/mystique-web-sample) for usage

#### Json Mystique - Gson Utilities
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>gson-utils</artifactId>
  <version>2.0.8</version>
</dependency>
```

The json mystique gson utility library provides a set of utility classes to operate on raw json objects and serialise deserialise POJOs.
The utility classes are
*   [Gson Convertor](json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/convertor/GsonConvertor.java) - Utility for POJO Object Model Mapping
*   [Json Lever](json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/lever/JsonLever.java) - Utility to operate on raw json objects. Simplified and type safe use of the Gson library. Allows deep and shallow merge of jsons
*   [Json Query](json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/lever/JsonQuery.java) - Utility to perform queries on a json element (can be an object or an array)
*   [Json Comparator](json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/lever/JsonComparator.java) - Utility to compare two jsons

#### Json Mystique - Jackson Utilities
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>jackson-utils</artifactId>
  <version>2.0.8</version>
</dependency>
```

The json mystique jackson utility library provides a set of utility classes to operate on raw json objects and serialise deserialise POJOs.
The utility classes are
* [Jackson Convertor](json-mystique-utils/jackson-utils/src/main/java/com/balajeetm/mystique/util/jackson/convertor/JacksonConvertor.java) - Utility for POJO Object Model Mapping

## Documentation

For detailed documentation, refer the [documentation index](documentation/_Sidebar.md)<br>
For more information on the usage and detailed usage guides, refer the [documentation home](documentation/Home.md)<br>