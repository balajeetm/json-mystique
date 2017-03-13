Welcome to the json-mystique wiki!

## Maven Download

[Json Mystique](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.balajeetm.mystique%22) downloads at Maven Central

### Maven Dependency Snippet
The maven dependency snippet for various libs is as below

#### Json Mystique - Native Library
---

There are three variants of JsonMystique
* [0.x.x](http://search.maven.org/#artifactdetails%7Ccom.github.balajeetm%7Cjson-mystique%7C0.0.1%7Cjar) - All the processing of JsonMystique is sequential
* [1.x.x](http://search.maven.org/#artifactdetails%7Ccom.github.balajeetm%7Cjson-mystique%7C1.0.8%7Cjar) - JsonMystique parallely processes various parts of the input Json
* [2.x.x](http://search.maven.org/#artifactdetails%7Ccom.balajeetm.mystique%7Cjson-mystique%7C2.0.5%7Cjar) - Spring Boot compliant Json Mystique with starters and auto configuration

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique</artifactId>
  <version>2.0.5</version>
</dependency>
```

> There is absolutely no change in the usage or the syntax and semantics of the two versions. Moving forward, the parallel processing logic would be mainstream

#### Json Mystique - Spring Boot Starter
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique-starter</artifactId>
  <version>2.0.5</version>
</dependency>
```

The json mystique spring boot starter autoconfigures the json mystique environment appropriately, creating all the necessary beans.
It also appropriately configures the [Jackson](https://github.com/FasterXML/jackson) Object Mapper to ensure it can serialise deserialise [Gson](https://github.com/google/gson) objects, iff jackson is in the classpath. In a web environment, it also configures Spring RestTemplate to ensure it can serialise deserialise [Gson](https://github.com/google/gson) objects seamlessly, iff, RestTemplate is in the classpath

#### Json Mystique - Gson Utilities
---

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>gson-utils</artifactId>
  <version>2.0.5</version>
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
  <version>2.0.5</version>
</dependency>
```

The json mystique jackson utility library provides a set of utility classes to operate on raw json objects and serialise deserialise POJOs.
The utility classes are
* [Jackson Convertor](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-utils/jackson-utils/src/main/java/com/balajeetm/mystique/util/jackson/bean/convertor/JacksonConvertor.java) - Utility for POJO Object Model Mapping

## Usage Guide

For more information on usage, refer [Usage Guide](https://github.com/balajeetm/json-mystique/wiki/Usage-Guide)