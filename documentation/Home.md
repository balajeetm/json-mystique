Welcome to the json-mystique wiki!

## Maven Download

[Json Mystique](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.balajeetm.mystique%22) and all its libraries download at Maven Central.<br>
The different library utils, their capabilities & their maven dependency snippets are detailed below:

### Json Mystique - Native Library
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique</artifactId>
  <version>3.0.0</version>
</dependency>
```

> **NOTE**<br>
There are four variants of JsonMystique.<br>
The `1.x.x` and `0.x.x` versions have reached EOL. `2.x.x` & `3.x.x` are the only active version as of now<br><br>
[3.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C3.0.0%7Cjar) - Latest Spring Support<br>
[2.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C2.5.3%7Cjar) - Standalone json transformation utility<br>
[1.x.x](http://search.maven.org/#artifactdetails%7Ccom.github.balajeetm%7Cjson-mystique%7C1.0.8%7Cjar) - Parallel processing json transformation utility<br>
[0.x.x](http://search.maven.org/#artifactdetails%7Ccom.github.balajeetm%7Cjson-mystique%7C0.0.1%7Cjar) - Sequential processing json transformation utility

> NOTE
[2.2.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C2.5.3%7Cjar) onwards supports Spring Boot 2.1.X<br>
[3.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C3.0.0%7Cjar) onwards supports Spring Boot 3.X.X<br>

> NOTE<br>
2.x.x & 3.x.x are the only supported version currently since the parallel processing logic is mainstream. The other versions (0.x.x and 1.x.x) have been deprecated.<br>
It is highly recommended and suggested, to only use the 3.x.x versions of Mystique.<br>
Please raise issues, for support if any, on earlier versions.<br>

### Json Mystique - Spring Boot Starter
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique-starter</artifactId>
  <version>3.0.0</version>
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

For more details refer the [usage guide](JsonMystique-Usage-Guide.md) or the [sample projects](json-mystique-samples/mystique-web-sample) for usage

### Json Mystique - Gson Utilities
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>gson-utils</artifactId>
  <version>3.0.0</version>
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
  <version>3.0.0</version>
</dependency>
```

The json mystique jackson utility library provides a set of utility classes, implemented using jackson, to operate on raw json objects and serialise deserialise POJOs.
The utility classes are
* [Jackson Convertor](json-mystique-utils/jackson-utils/src/main/java/com/balajeetm/mystique/util/jackson/convertor/JacksonConvertor.java) - Utility for POJO Object Model Mapping

## Usage Guide

Hop over here for the [documentation index](_Sidebar.md)