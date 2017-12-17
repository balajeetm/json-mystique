# Usage Guide

## Configuring Json Mystique
Below are the steps to configure json mystique to get it booted up using native library or the starter.

Although you could just copy Json Mystique jars, we generally recommend that you use a build tool that supports dependency management (such as Maven or Gradle).

### Json Mystique - Native Library
---

#### Step 1
Add the json-mystique jar to your classpath.
If you are using maven for dependency management, add the below json-mystique dependency to your pom file

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique</artifactId>
  <version>2.x.x</version>
</dependency>
```

#### Step 2
Next, get the instance of JsonMystique to play around with

`JsonMystique mystique = JsonMystique.getInstance()`

### Json Mystique - Spring Boot Starter
---

Starters are a set of convenient dependency descriptors that you can include in your application. You get a one-stop-shop for all the Json Mystique configurations via the json mystique starter

#### Step 1
If you are using maven for dependency management, add the below json-mystique-starter dependency to your pom file

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>json-mystique-starter</artifactId>
  <version>2.x.x</version>
</dependency>
```

That's it, you are good to go!<br>
All beans are autoconfigured.<br>
Even Jackson is autoconfigured to serialise/deserialise Gson Json Objects automatically, iff, Jackson is found to be available in the classpath

Not just that, in a web environment, the Spring's Jackson Http Message Convertors are auto configured to serialise/deserialise [Gson](https://github.com/google/gson) Json Elements. Any RestTemplate Beans in the classpath are also by default autoconfigured to serialise/deserialise [Gson](https://github.com/google/gson) json elements. All in a snap!

Cool huh? Great. We are good to get rolling

For more details refer the [sample projects](../json-mystique-samples/mystique-web-sample) for usage

#### NOTE

Json Mystique's auto-configuration is noninvasive. For example, if you add your own ObjectMapper or RestTemplate bean, the default ObjectMapper and RestTemplate will back away. However, it will ensure it will autoconfigure these custom beans appropriately for json serialisation

### Spring Web, Spring Boot and Gson
---
Json Mystique uses [Gson](https://github.com/google/gson) for all json serialisation or deserialisation.
If you familiar with [spring-web](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/spring-web.html), spring by default uses Jackson for the same.

So there are cases when both gson and jackson are in your classpath, especially in spring web applications.<br>
This isn't particularly a problem. The only catch however is, Jackson cannot deserialise/serialise gson objects.
What this means is that, if any of your [RestControllers](https://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#mvc-ann-restcontroller) is returning Gson Json Objects, you would get a marshalling error from spring.

To work around the problem, 
spring suggests you to use Gson as the default serialiser/deserialiser for json objects instead.
This works like charm.

However, if you are using [SpringBoot's](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) [Actuator](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready), using Gson as the default serialiser would mess up the response.
This is because actuator heavily depends on Jackson for serialisation/deserialisation by using Jackson's annotations.

This is a sticky problem indeed.<br>
Json-Mystique provides a solution out of the box for such issues. It can automatically configure the [Jackson](https://github.com/FasterXML/jackson), to serialise/deserialise Gson Json Objects.<br>
If you are using, `json-mystique-starter`, there is nothing more for you do, however, if you are using regular spring or using ObjectMapper natively, register the MystiqueModule to object mapper

```java
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(new MystiqueModule());
```

In other client web environments, there might be a need to interact with WebServers via Rest APIs. Spring Web provides a powerful client library to make rest calls via RestTemplate. However, RestTemplate by default uses a JacksonHttpMessageConvertor which internally uses the [Jackson](https://github.com/FasterXML/jackson) ObjectMapper. Even in this case, RestTemplate by default cannot serialise deserialise [Gson](https://github.com/google/gson) Json Objects. Json-Mystique provides a solution out of the box for such issues as well. It can automatically configure RestTemplate bean's (remember RestTemplate must be a spring managed bean) ObjectMapper, to serialise/deserialise [Gson](https://github.com/google/gson) Json Objects.<br>
Again, if you are using, `json-mystique-starter`, there is nothing more for you do.

## Using Json Mystique
Once json mystique is appropriately configured and all beans are available, json mystique can be used as below for Json transformation

#### Step 1
Grab the JsonMystique instance.<br>
If you are using sprinng, autowire the JsonMystique class into the class where you wish to perform the json transform. This can be done as below

`@Autowired`
`private JsonMystique jsonMystique;`

If you are usinng JsonMystqiue natively, do the below:
```java
private JsonMystique = JsonMystique.getInstance();
```

#### Step 2
Next, the rule set files for Json Transformation have to be added to the classpath.
JsonMystique looks for the ruleset files with the extension **".mys"** in the classpath under a folder called **"jsonmystique"**. These files can also be found in sub-folders under "jsonmystique" folder. So if you want to group your mystique tarot files based on your domain, feel free to do so

If you are using maven for build management, add a folder called **"jsonmystique"** to your **"src/main/resources"** and put all your ruleset files in this folder.

> Remember, the extension of these files has to be **".mys"**

#### Step 3
Now you are all set to use JsonMystique.

There are 8 methods on JsonMystique that can be used for Json transformation

The first 4 methods called *"transform"* which return a GSON POJO version of the json aka JsonElement

* `JsonElement transform(String inputJson, String ruleSetFile)`
  
  Given an inputJson, invoke the above to transform the input json based on your ruleset.
  
  It returns GSON POJO version of the json aka JsonElement
  
  eg `JsonElement transform = jsonMystique.transform(inputJsonString, "myruleset");`

  Where **inputJsonString** refers to the input json in java string format and
  **myruleset** refers to the ruleset file by name **"myruleset.mys"** in the classpath

  > Observe, you do not need to provide the extension **".mys"** when referring to the ruleset file. Just the name would suffice.

* `JsonElement transform(String inputJson, String ruleSetFile, JsonObject deps)`

  You can pass some external dependencies/domain dependencies in the form of a JsonObject

* `JsonElement transform(JsonElement sourceJson, String ruleSetFile)`

  Alternatively, you can pass the input json as a Gson POJO

* `JsonElement transform(JsonElement sourceJson, String ruleSetFile, JsonObject deps)`

  And also pass few domain dependencies as a JsonObject

On the same lines, you can invoke the **transformToString** method to get the transformed json as a java string as below

`String transform = jsonMystique.transformToString(string, "myruleset");`

The overloaded methods from getting a string json are as below :

* `String transformToString(String inputJson, String specName)`
* `String transformToString(JsonElement source, String specName)`
* `String transformToString(String inputJson, String specName, JsonObject deps)`
* `String transformToString(JsonElement source, String specName, JsonObject deps)`

## The Rule Set File

Another very important concept to grasp wrt Json Mystique is the Rule Set File.
The rule set file is where the magic is. It specifies all transformations to be carried on the input json.

These transformations are called rules/tarots. Let's jump into the [Rule Set File](The-RuleSet-File---*.mys.md) for more details<br>
Hop over here for the [documentation index](_Sidebar.md)
