Welcome to the json-mystique wiki!

## Maven Download

[Json Mystique](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.balajeetm.mystique%22) downloads at Maven Central

### Maven Dependency Snippet
The maven dependency snippet for various libs is as below

#### Json Mystique - Native Library
---
[2.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C2.0.8%7Cjar) - JsonMystique with parallel processing capabilities

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique</artifactId>
  <version>2.0.8</version>
</dependency>
```

#### Json Mystique - Spring Boot Starter
---
[2.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique-starter%7C2.0.8%7Cjar) - Spring Boot compliant Json Mystique with starters and auto configuration

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique-starter</artifactId>
  <version>2.0.8</version>
</dependency>
```

The json mystique ships with a spring boot starter which autoconfigures the json mystique environment appropriately, creating all the necessary beans. Its primary features are
* Configures the simple [Jackson](https://github.com/FasterXML/jackson) Json Convertor as a bean
* Configures the simple Gson](https://github.com/google/gson) Json Convertor as a bean
* Configures Jackson Object Mapper with the ability to serialise and deserialise Gson Objects (JsonElement)
* Configures JsonMystique with its appropriate turns as a bean
* If Spring-web is in the classpath, it connfigures a RestTemplate with the ability to serialise and deserialise Gson Objects (JsonElement)
* Automatically registers custom Json Mystique `MystTurns` with JsonMystique

#### Json Mystique - Gson Utilities
---
[2.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cgson-utils%7C2.0.8%7Cjar) Gson based, Json Manipulation Utilities

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>gson-utils</artifactId>
  <version>2.0.8</version>
</dependency>
```

The json mystique gson utility library provides a set of utility classes to operate on raw json objects and serialise deserialise POJOs.
The utility classes are
* [Gson Convertor](../json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/convertor/GsonConvertor.java) - Utility for POJO Object Model Mapping
* [Json Lever](../json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/lever/JsonLever.java) - Utility to operate on raw json objects. Simplified and type safe use of the Gson library. Allows deep and shallow merge of jsons
* [Json Query](../json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/bean/lever/JsonQuery.java) - Utility to perform queries on a json element (can be an object or an array)
* [Json Comparator](../json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/bean/lever/JsonComparator.java) - Utility to compare two jsons

#### Json Mystique - Jackson Utilities
---
[2.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjackson-utils%7C2.0.8%7Cjar) Jackson based, Json Manipulation Utilities

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>jackson-utils</artifactId>
  <version>2.0.8</version>
</dependency>
```

The json mystique jackson utility library provides a set of utility classes to operate on raw json objects and serialise deserialise POJOs.
The utility classes are
* [Jackson Convertor](../json-mystique-utils/jackson-utils/src/main/java/com/balajeetm/mystique/util/jackson/bean/convertor/JacksonConvertor.java) - Utility for POJO Object Model Mapping

## Usage Guide

For more information on usage, refer [Usage Guide](Usage-Guide.md)
Hop over here for the [documentation index](_Sidebar.md)