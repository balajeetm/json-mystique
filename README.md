[![Build Status](https://travis-ci.org/balajeetm/json-mystique.svg?branch=master)](https://travis-ci.org/balajeetm/json-mystique)

# Json Mystique
The umbrella of utilities for Json Manipulation, Json Conversion and Json transformation in Java.<br>
It is vaguely similar to [lodash's](https://lodash.com/) object manipulation capabilities but adds a lot more punch to it.

Json mystique ships a set of libraries written in and for Java, useful for json manipulation & transformation 
*   **Json Manipulation**<br>
    The library exposes simple `get`, `set` and `default` methods for json manipulation.<br>
    ```
    Eg. JsonElement json = { 'a': [{ 'b': { 'c': 3 } }] };
    jsonLever.get(json, 'a.0.b.c');
    // => 3
    ``` 
    Refer [JsonLever](documentation/JsonLever.md), GsonConvertor, JacksonConvertor, JsonQuery and JsonComparator for more details.
*   **Json Transformation**<br>
    The library takes an input json as a string or [JsonElement](https://google.github.io/gson/apidocs/com/google/gson/JsonElement.html) and transforms the same to a json string or [JsonElement](https://google.github.io/gson/apidocs/com/google/gson/JsonElement.html).<br>
    The transformation is performed via a ruleSet file that specifies the mappings in a json format.<br>
    Refer JsonMystique for more details.

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
    Refer [JsonLever](documentation/JsonLever.md) for more details
*   The library is useful for transforming from one json to another, even when the input and output jsons are non identical
    *   Multiple fields from the input can be operated on to map to a single field in the output
    *   Custom converters can be easily plugged to handle custom domain logic
    Refer JsonMystique for more details.
*   The library exposes a standard interface for converting POJO to json and vice versa
    *   The standard interface is implemented in [Gson](https://github.com/google/gson) via GsonConvertor and
    *   [Jackson](https://github.com/FasterXML/jackson) via JacksonConvertor
*   The library is useful for getting subset or projections of a json object.<br>
    Refer [JsonLever](documentation/JsonLever.md) for more details
*   The library is useful for comparing two jsons
    *   Compare if two jsons are identical
    *   Compare if one json is a subset of another
    Refer JsonComparator for more details
*   The library exposes a query interface on a json, to perform various 'sql'-like queries on a json and select relevant fields from it.<br>
    Refer JsonQuery for more details

## Dependencies
*   The library requires [Java 8+](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html)
*   The library uses [maven](https://maven.apache.org/) for its build management
*   The library can be primarily used as a stand-alone java lib and depends on  
    *   [Gson - 2.8.6](https://mvnrepository.com/artifact/com.google.code.gson/gson) for json String to Java pojo transformations
    *   [Jackson - 2.11.4](https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind) for json String to Java pojo transformations
*   For the [Spring](https://spring.io/) fan-boys, Json Mystique also ships a Spring-Starter, which depends on
    *   [Spring Boot - 2.4.5](https://docs.spring.io/spring-boot/docs/2.4.5/reference/htmlsingle/)
    *   [Spring-Framework - 5.3.6](https://docs.spring.io/spring-framework/docs/5.3.6/reference/html/) for IOC

The actual versions can be found in the [pom file](/pom.xml)

## Maven Download

[Json Mystique](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.balajeetm.mystique%22) and all its libraries download at Maven Central.<br>
The different library utils, their capabilities & their maven dependency snippets are detailed below:

### Json Mystique - Native Library
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique</artifactId>
  <version>2.3.0</version>
</dependency>
```

> **NOTE**<br>
There are three variants of JsonMystique.<br>
The `1.x.x` and `0.x.x` versions have reached EOL. `2.x.x` is the only active version as of now<br><br>
[2.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C2.3.0%7Cjar) - Standalone json transformation utility<br>
[1.x.x](http://search.maven.org/#artifactdetails%7Ccom.github.balajeetm%7Cjson-mystique%7C1.0.8%7Cjar) - Parallel processing json transformation utility<br>
[0.x.x](http://search.maven.org/#artifactdetails%7Ccom.github.balajeetm%7Cjson-mystique%7C0.0.1%7Cjar) - Sequential processing json transformation utility

> NOTE
[2.2.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C2.3.0%7Cjar) onwards supports Spring Boot 2.1.X<br>

> NOTE<br>
2.x.x is the only supported version currently since the parallel processing logic is mainstream. The other versions (0.x.x and 1.x.x) have been deprecated.<br>
It is highly recommended and suggested, to only use the 2.x.x versions of Mystique.<br>
Please raise issues, for support if any, on earlier versions.<br>

### Json Mystique - Spring Boot Starter
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique-starter</artifactId>
  <version>2.3.0</version>
</dependency>
```

The custom [spring-boot-starter](https://docs.spring.io/spring-boot/docs/2.0.4.RELEASE/reference/htmlsingle/#boot-features-custom-starter) for JsonMystique. Autoconfigures all necessary beans and seamlessly integrates with Spring's RestControllers and Jackson libraries.

The json mystique's spring boot starter autoconfigures the json mystique environment appropriately, creating all the necessary beans. Its primary features are
*   Configures the simple [Jackson](https://github.com/FasterXML/jackson) Json Convertor as a bean
*   Configures the simple [Gson](https://github.com/google/gson) Json Convertor as a bean
*   Configures Jackson Object Mapper with the ability to serialise and deserialise Gson Objects (JsonElement)
*   Configures JsonMystique with its appropriate turns as a bean
*   If Spring-web is in the classpath, it connfigures a RestTemplate with the ability to serialise and deserialise Gson Objects (JsonElement)
*   Automatically registers custom Json Mystique `MystTurns` with JsonMystique

For more details refer the [usage guide](documentation/JsonMystique-Usage-Guide.md) or the [sample projects](json-mystique-samples/mystique-web-sample) for usage

### Json Mystique - Gson Utilities
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>gson-utils</artifactId>
  <version>2.3.0</version>
</dependency>
```

The json mystique gson utility library provides a set of utility classes to operate on manipulate, access and operate on json objects. All the utilities are standalone instances that can be accessed via `getInstance()` and use right away.<br>
The utility classes are
*   [Json Lever](json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/lever/JsonLever.java) - Utility to operate on raw json objects. Exposes simplified [lodash](https://lodash.com/) like object manipulation interfaces and a type safe use of the Gson library. Allows deep and shallow merge & clone of jsons
*   [Gson Convertor](json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/convertor/GsonConvertor.java) - Utility for POJO Object Model Mapping. Provides a simplified interface to convert to and from a json
*   [Json Comparator](json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/lever/JsonComparator.java) - Utility to compare two jsons
*   [Json Query](json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/lever/JsonQuery.java) - Utility to perform sql-like queries on a json element to select appropriate fields from a json (can be an object or an array)

#### Json Mystique - Jackson Utilities
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>jackson-utils</artifactId>
  <version>2.3.0</version>
</dependency>
```

The json mystique jackson utility library provides a set of utility classes, implemented using jackson, to operate on raw json objects and serialise deserialise POJOs.
The utility classes are
* [Jackson Convertor](json-mystique-utils/jackson-utils/src/main/java/com/balajeetm/mystique/util/jackson/convertor/JacksonConvertor.java) - Utility for POJO Object Model Mapping

## Spring Boot 2 Support

JsonMystique completely supports [Spring Boot 2](https://docs.spring.io/spring-boot/docs/2.1.0.RELEASE/api/).<br>
Since Spring Boot 2 is not yet available for GA, JsonMystique supports the most stable milestone version of [SpringBoot2](https://docs.spring.io/spring-boot/docs/2.1.0.RELEASE/api/) which is [2.1.0.RELEASE](https://docs.spring.io/spring-boot/docs/2.1.0.RELEASE/reference/htmlsingle/)

The support for [Spring 5](https://docs.spring.io/spring/docs/5.0.8.RELEASE/spring-framework-reference/) and Spring Boot 2 are part of Mystique's milestone release [2.3.0](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.balajeetm.mystique%22%20v%3A%222.3.0%22):

[json-mystique](http://repo1.maven.org/maven2/com/balajeetm/mystique/json-mystique/2.3.0/)
___
```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique</artifactId>
  <version>2.3.0</version>
</dependency>
```

[json-mystique-starter](http://repo1.maven.org/maven2/com/balajeetm/mystique/json-mystique-starter/2.3.0/)
___
```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique-starter</artifactId>
  <version>2.3.0</version>
</dependency>
```

[gson-utils](http://repo1.maven.org/maven2/com/balajeetm/mystique/gson-utils/2.3.0/)
___
```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>gson-utils</artifactId>
  <version>2.3.0</version>
</dependency>
```

[jackson-utils](http://repo1.maven.org/maven2/com/balajeetm/mystique/jackson-utils/2.3.0/)
___
```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>jackson-utils</artifactId>
  <version>2.3.0</version>
</dependency>
```
## Documentation

For detailed documentation, refer the [documentation index](documentation/_Sidebar.md)<br>
For more information on the usage and detailed usage guides, refer the [documentation home](documentation/Home.md)<br>