JsonLever is an utility packaged with JsonMystique to easily operate on jsons

## Why JsonLever
JsonLever makes operating with Jsons easier by taking the hassle out of working with arrays, numbers, objects, strings, etc.<br>
JsonLever's methods are modular and are wonderful for:
*   Iterating json arrays, json objects, & json primitives (string, number, boolean etc)
*   Manipulating jsons, retrieving and updating values
*   Cloning Jsons
*   Retrieving subset of a json or selecting specific fields from a json

## Usage Guide

### Configuring JsonLever
Below are the steps to configure json lever to get it booted up using native library or the starter.

Although you could just copy gson-utils jars (along with its dependencies), we generally recommend that you use a build tool that supports dependency management (such as Maven or Gradle).

### Gson Utils - Native Library
---

#### Step 1
Add the gson-utils jar to your classpath.
If you are using maven for dependency management, add the below json-mystique dependency to your pom file

```xml
<dependency>
  <groupId>com.balajeetm.mystique</groupId>
  <artifactId>gson-utils</artifactId>
  <version>2.x.x</version>
</dependency>
```

>**NOTE :** Adding **"json-msytique"** as the dependency, transitively adds **"gson-utils"** to the classpath and thus all utilities of **"gson-utils"** are available out of the box.<br>**"json-msytique"** is thus the only uber dependency needed to enjoy all the utilities/capabilities of JsonMystique.

#### Step 2
Next, get the instance of JsonLever to play around with

`JsonLever jsonLever = JsonLever.getInstance()`

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

Grab hold of the JsonLever as below:

```java
JsonLever jsonLever = JsonLever.getInstance();
```

If you are using the `json-mystique-starter`, you can just autowire JsonLever as below:

```java
@Autowired JsonLever jsonLever;
```

>**NOTE**<br>
JsonLever is lazily loaded. Until the `getInstance` is invoked, its object is not instantiated.

## Api Reference

JsonLever exposes [lodash](https://lodash.com/) like utilities to operate on a json.<br>

### JsonLever.get(json, path, [defaultValue])

Gets the value at path of json. If the resolved value is undefined, JsonNull is returned in its place.

**Example**
```java
json = { "a": [{ "b": { "c": 3 } }] };

path = "a.0.b.c"
jsonLever.get(json, path);
// => 3

path =  ["a", 0, "b", "c"]
jsonLever.get(json, path);
// => 3

path = "a.b.c"
jsonLever.get(json, path, new JsonPrimitive("default");
// => 'default'
```

This is a functionally overloaded method, with abilities to provide the json source and path in different formats<br>
1.  `public JsonElement get(JsonElement source, String jpath)`
2.  `public JsonElement get(Object source, String jpath)`
3.  `public JsonElement get(JsonElement source, JsonArray jpath)`
4.  `public JsonElement get(Object source, JsonArray jpath)`
5.  `public JsonElement get(JsonElement source, Object... jpath)`
6.  `public JsonElement get(Object source, Object... jpath)`
7.  `public JsonElement get(JsonElement source, String jpath, JsonElement defaultValue)`
8.  `public JsonElement get(Object source, String jpath, JsonElement defaultValue)`
9.  `public JsonElement get(JsonElement source, JsonArray jpath, JsonElement defaultValue)`
10. `public JsonElement get(Object source, JsonArray jpath, JsonElement defaultValue)`

>Since - 2.0.8

**Arguments**<br>
*   **source**<br>
    The json source. It can be one of the below
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)
    *   Object<br>
        Can be any Java POJO, that can be jsonified, or a String representing a valid json
*   **jpath**<br>
    Defines the fully qualified json path to the field required. It can be one of the below:
    *   String<br>
        '.' separated string defining the fully qualified json path.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = "a.0.b.c"
        jsonLever.get(json, path);
        // => 3
        ```
    *   JsonArray<br>
        JsonArray representing the fully qualified json path. Array indexes are represented as numerals.<br>
        Only strings and numerals are allowed in the json array. String represent a json object"s fieldname and numerals represent array indexes.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = ["a", 0, "b", "c"]
        jsonLever.get(json, path);
        // => 3
        ```
    *   Object[] - Object Array<br>
        Object Array representing the fully qualified json path<br>
        Only strings and numerals are allowed in the json array. String represents a json object"s fieldname and numerals represent array indexes.<br>
        The object can also be JsonPrimitives representing strings and numerals.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = ["a", 0, "b", "c"]
        jsonLever.get(json, path);
        // => 3
*   **defaultValue**<br>
    Represents the value defined for undefined resolved values. It can be one of the below:
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

**Returns**<br>
Returns the resolved value as:<br>
*   **JsonElement**<br>
    Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

### JsonLever.set(json, path, value)

Sets the value at path of object. If a portion of path doesn't exist, it's created. Arrays are created for missing index properties while objects are created for all other missing properties.

**Example**
```java
json = { "a": [{ "b": { "c": 3 } }] };
 
jsonLever.set(json, "a.0.b.c", 4);
System.out.println(json)
// => { "a": [{ "b": { "c": 4 } }] };
 
jsonLever.set(json, ["x", 1, "y", "z"], 5);
System.out.println(json)
// => { "a": [{ "b": { "c": 4 } }], "x": [null, { "y": { "z": 5 } }] };
```

This is a functionally overloaded method, with abilities to provide the json source and path in different formats<br>
1.  `public JsonElement set(JsonElement source, String jpath, JsonElement value)`
2.  `public JsonElement set(JsonElement source, JsonArray jpath, JsonElement value)`

>Since - 2.0.8

**Arguments**<br>
*   **source**<br>
    The json source. It can be one of the below
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)
*   **jpath**<br>
    Defines the fully qualified json path to the field required. It can be one of the below:
    *   String<br>
        '.' separated string defining the fully qualified json path.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = "a.0.b.c"
        jsonLever.set(json, path, new JsonPrimitive("4"));
        System.out.println(json)
        // => { "a": [{ "b": { "c": "4" } }] }
        ```
    *   JsonArray<br>
        JsonArray representing the fully qualified json path. Array indexes are represented as numerals.<br>
        Only strings and numerals are allowed in the json array. String represent a json object's fieldname and numerals represent array indexes.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = ["x", 1, "y", "z"]
        jsonLever.set(json, path, new JsonPrimitive("5"));
        // => { "a": [{ "b": { "c": 3 } }], "x": [null, {"y": "z"}] }
        ```
*   **value**<br>
    The value to set. It can be one of the below:
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

**Returns**<br>
Returns the source json with value set:<br>
*   **JsonElement**<br>
    Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

### JsonLever.get*(json, jpath)

Gets the value at path of json as the `'*'` typed JsonElement. If the resolved value cannot be typed to the required type, `null` is returned.

>Since - 2.0.8

**Arguments**<br>
*   **source**<br>
    The json source. It can be one of the below
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)
    *   Object<br>
        Can be any Java POJO, that can be jsonified, or a String representing a valid json
*   **jpath**<br>
    Defines the fully qualified json path to the field required. It can be one of the below:
    *   String<br>
        '.' separated string defining the fully qualified json path.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = "a.0.b.c"
        jsonLever.getJsonPrimitive(json, path);
        // => 3
        ```
    *   JsonArray<br>
        JsonArray representing the fully qualified json path. Array indexes are represented as numerals.<br>
        Only strings and numerals are allowed in the json array. String represent a json object"s fieldname and numerals represent array indexes.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = ["a", 0, "b", "c"]
        jsonLever.getLong(json, path);
        // => 3
        ```
    *   Object[] - Object Array<br>
        Object Array representing the fully qualified json path<br>
        Only strings and numerals are allowed in the json array. String represents a json object"s fieldname and numerals represent array indexes.<br>
        The object can also be JsonPrimitives representing strings and numerals.<br>
        ```java
        json = { "a": [{ "b": { "c": "3" } }] };
        path = ["a", 0, "b", "c"]
        jsonLever.getString(json, path);
        // => 3

**Returns**<br>
Returns the resolved value as:<br>
*   **JsonElement**<br>
    Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

There are multiple `get*` utilities are below<br>
1.  `public JsonObject getJsonObject(JsonElement source, String jpath)`
2.  `public JsonObject getJsonObject(Object source, String jpath)`
3.  `public JsonObject getJsonObject(JsonElement source, JsonArray jpath)`
4.  `public JsonObject getJsonObject(Object source, JsonArray jpath)`
5.  `public JsonObject getJsonObject(JsonElement source, Object... jpath)`
6.  `public JsonObject getJsonObject(Object source, Object... jpath)`
    **Example**<br>
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    path = "a.0";
    jsonLever.getJsonObject(json, path);
    // { "b": { "c": 3 } }

    path = ["a", 0];
    jsonLever.getJsonObject(json, path);
    // { "b": { "c": 3 } }

    jsonLever.getJsonObject(json, "a", 0);
    // { "b": { "c": 3 } }
    
    path = "a.0.b"
    jsonLever.getJsonObject(json, path);
    // => { "c": 3 } 

    path = "a.b"
    jsonLever.getJsonObject(json, path);
    // => null 

    path = "a.0.b.c"
    jsonLever.getJsonObject(json, path);
    // => null 
    ```

7.  `public JsonArray getJsonArray(JsonElement source, String jpath)`
8.  `public JsonArray getJsonArray(Object source, String jpath)`
9.  `public JsonArray getJsonArray(JsonElement source, JsonArray jpath)`
10. `public JsonArray getJsonArray(Object source, JsonArray jpath)`
11. `public JsonArray getJsonArray(JsonElement source, Object... jpath)`
12. `public JsonArray getJsonArray(Object source, Object... jpath)`
    **Example**<br>
    ```java
    json = { "a": { "a1": [{ "b": { "c": 3 } }]} };
 
    path = "a.a1";
    jsonLever.getJsonArray(json, path);
    // [{ "b": { "c": 3 } }]

    path = ["a", "a1"];
    jsonLever.getJsonArray(json, path);
    // [{ "b": { "c": 3 } }]

    jsonLever.getJsonArray(json, "a", "a1");
    // [{ "b": { "c": 3 } }]
    
    path = "a.0.b"
    jsonLever.getJsonArray(json, path);
    // => null 

    path = "a.b"
    jsonLever.getJsonArray(json, path);
    // => null 

    path = "a.0.b.c"
    jsonLever.getJsonArray(json, path);
    // => null 
    ```

13. `public String getString(JsonElement source, String jpath)`
14. `public String getString(Object source, String jpath)`
15. `public String getString(JsonElement source, JsonArray jpath)`
16. `public String getString(Object source, JsonArray jpath)`
17. `public String getString(JsonElement source, Object... jpath)`
18. `public String getString(Object source, Object... jpath)`
    **Example**<br>
    ```java
    json = { "a": [{ "b": { "c": "3" } }] };
 
    path = "a.0";
    jsonLever.getString(json, path);
    // null

    path = ["a", 0];
    jsonLever.getString(json, path);
    // null

    jsonLever.getString(json, "a", 0);
    // null
    
    path = "a.0.b.c"
    jsonLever.getString(json, path);
    // => "3" 

    path = "a.b"
    jsonLever.getString(json, path);
    // => null 

    path = ["a", 0, "b", "c"]
    jsonLever.getString(json, path);
    // => "3"

19. `public String getString(JsonElement source, String jpath)`
20. `public String getString(Object source, String jpath)`
21. `public String getString(JsonElement source, JsonArray jpath)`
22. `public String getString(Object source, JsonArray jpath)`
23. `public String getString(JsonElement source, Object... jpath)`
24. `public String getString(Object source, Object... jpath)`
    **Example**<br>
    ```java
    json = { "a": [{ "b": { "c": "3" } }] };
 
    path = "a.0";
    jsonLever.getString(json, path);
    // null

    path = ["a", 0];
    jsonLever.getString(json, path);
    // null

    jsonLever.getString(json, "a", 0);
    // null
    
    path = "a.0.b.c"
    jsonLever.getString(json, path);
    // => "3" 

    path = "a.b"
    jsonLever.getString(json, path);
    // => null 

    path = ["a", 0, "b", "c"]
    jsonLever.getString(json, path);
    // => "3"

25. `public Long getLong(JsonElement source, String jpath)`
26. `public Long getLong(Object source, String jpath)`
27. `public Long getLong(JsonElement source, JsonArray jpath)`
28. `public Long getLong(Object source, JsonArray jpath)`
29. `public Long getLong(JsonElement source, Object... jpath)`
30. `public Long getLong(Object source, Object... jpath)`
    **Example**<br>
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    path = "a.0";
    jsonLever.getLong(json, path);
    // null

    path = ["a", 0];
    jsonLever.getLong(json, path);
    // null

    jsonLever.getLong(json, "a", 0);
    // null
    
    path = "a.0.b.c"
    jsonLever.getLong(json, path);
    // => 3 

    path = "a.b"
    jsonLever.getLong(json, path);
    // => null 

    path = ["a", 0, "b", "c"]
    jsonLever.getLong(json, path);
    // => 3

31. `public Boolean getBoolean(JsonElement source, String jpath)`
32. `public Boolean getBoolean(Object source, String jpath)`
33. `public Boolean getBoolean(JsonElement source, JsonArray jpath)`
34. `public Boolean getBoolean(Object source, JsonArray jpath)`
35. `public Boolean getBoolean(JsonElement source, Object... jpath)`
36. `public Boolean getBoolean(Object source, Object... jpath)`
    **Example**<br>
    ```java
    json = { "a": [{ "b": { "c": true } }] };
 
    path = "a.0";
    jsonLever.getBoolean(json, path);
    // null

    path = ["a", 0];
    jsonLever.getBoolean(json, path);
    // null

    jsonLever.getBoolean(json, "a", 0);
    // null
    
    path = "a.0.b.c"
    jsonLever.getBoolean(json, path);
    // => true 

    path = "a.b"
    jsonLever.getBoolean(json, path);
    // => null 

    path = ["a", 0, "b", "c"]
    jsonLever.getBoolean(json, path);
    // => true

37. `public JsonPrimitive getJsonPrimitive(JsonElement source, String jpath)`
38. `public JsonPrimitive getJsonPrimitive(Object source, String jpath)`
39. `public JsonPrimitive getJsonPrimitive(JsonElement source, JsonArray jpath)`
40. `public JsonPrimitive getJsonPrimitive(Object source, JsonArray jpath)`
41. `public JsonPrimitive getJsonPrimitive(JsonElement source, Object... jpath)`
42. `public JsonPrimitive getJsonPrimitive(Object source, Object... jpath)`
    **Example**<br>
    ```java
    json = { "a": [{ "b": { "c": true } }] };
 
    path = "a.0";
    jsonLever.getJsonPrimitive(json, path);
    // null

    path = ["a", 0];
    jsonLever.getJsonPrimitive(json, path);
    // null

    jsonLever.getJsonPrimitive(json, "a", 0);
    // null
    
    path = "a.0.b.c"
    jsonLever.getJsonPrimitive(json, path);
    // => true 

    path = "a.b"
    jsonLever.getJsonPrimitive(json, path);
    // => null 

    json = { "a": [{ "b": { "c": 1 } }] };
    path = ["a", 0, "b", "c"]
    jsonLever.getJsonPrimitive(json, path);
    // => 1

### JsonLever.is*(json)

Checks if value is classified as the `'*'` category

>Since - 2.0.8

**Arguments**<br>
*   **json**<br>
    The json source. It can be one of the below
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

**Returns**<br>
Returns `true` is the value is classified as the category, else `false`

**Example**
```java
json = { "a": [{ "b": { "c": 3 } }] };
 
jsonLever.isArray(json);
// false
 
jsonLever.isObject(json);
// => true
```

There are multiple is* utilities are below<br>
1.  `public Boolean isNull(JsonElement source)`
    **Example**<br>
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.isNull(json);
    // false
    
    json = null
    jsonLever.isNull(json);
    // => true 

    json = JsonNull.INSTANCE
    jsonLever.isNull(json);
    // => true 
    ```
2.  `public Boolean isNotNull(JsonElement source)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.isNotNull(json);
    // true
    
    json = null
    jsonLever.isNotNull(json);
    // => false 

    json = JsonNull.INSTANCE
    jsonLever.isNotNull(json);
    // => false 
    ```

3.  `public Boolean isArray(JsonElement source)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.isArray(json);
    // false
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.isArray(json);
    // => true 

    json = JsonNull.INSTANCE
    jsonLever.isArray(json);
    // => true 
    ```

4.  `public Boolean isObject(JsonElement source)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.isObject(json);
    // true
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.isObject(json);
    // => false 

    json = JsonNull.INSTANCE
    jsonLever.isObject(json);
    // => false 
    ```

5.  `public Boolean isPrimitive(JsonElement source)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.isPrimitive(json);
    // false
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.isPrimitive(json);
    // => false 

    json = JsonNull.INSTANCE
    jsonLever.isPrimitive(json);
    // => false

    json = new JsonPrimitive("test")
    jsonLever.isPrimitive(json);
    // => true

    json = new JsonPrimitive(1)
    jsonLever.isPrimitive(json);
    // => true    
    ```

6.  `public Boolean isNumber(JsonElement source)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.isNumber(json);
    // false
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.isNumber(json);
    // => false 

    json = JsonNull.INSTANCE
    jsonLever.isNumber(json);
    // => false

    json = new JsonPrimitive("test")
    jsonLever.isNumber(json);
    // => false

    json = new JsonPrimitive(1)
    jsonLever.isNumber(json);
    // => true    
    ```

7.  `public Boolean isString(JsonElement source)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.isString(json);
    // false
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.isString(json);
    // => false 

    json = JsonNull.INSTANCE
    jsonLever.isString(json);
    // => false

    json = new JsonPrimitive("test")
    jsonLever.isString(json);
    // => true

    json = new JsonPrimitive(1)
    jsonLever.isString(json);
    // => false    
    ```

8.  `public Boolean isBoolean(JsonElement source)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.isBoolean(json);
    // false
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.isBoolean(json);
    // => false 

    json = JsonNull.INSTANCE
    jsonLever.isBoolean(json);
    // => false

    json = new JsonPrimitive("test")
    jsonLever.isBoolean(json);
    // => false

    json = new JsonPrimitive(true)
    jsonLever.isBoolean(json);
    // => true    
    ```

### JsonLever.as*(json, [defaultValue])

Returns the json as the required json type if the type matches, else return the defaultValue if configured, else return null

>Since - 2.0.8

**Arguments**<br>
*   **json**<br>
    The json source. It can be one of the below
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

*   **defaultValue**<br>
    The defaultValue in 
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

**Returns**<br>
Returns the json in the required type else returns `null`

**Example**
```java
JsonElement json = { "a": [{ "b": { "c": 3 } }] };
 
jsonLever.asJsonPrimitive(json, new JsonPrimitive("default"));
// "default"
```

There are multiple as* utilities are below<br>
1.  `public JsonPrimitive asJsonPrimitive(JsonElement source)`
2.  `public JsonPrimitive asJsonPrimitive(JsonElement source, JsonPrimitive defaultValue)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.asJsonPrimitive(json);
    // null
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.asJsonPrimitive(json);
    // => null 

    json = JsonNull.INSTANCE
    jsonLever.asJsonPrimitive(json, new JsonPrimitive("default");
    // => "default"

    json = new JsonPrimitive("test")
    jsonLever.asJsonPrimitive(json);
    // => "test"

    json = new JsonPrimitive(1)
    jsonLever.asJsonPrimitive(json);
    // => 1    
    ```

3.  `public JsonObject asJsonObject(JsonElement element)`
4.  `public JsonObject asJsonObject(JsonElement source, JsonObject defaultJson)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.asJsonObject(json);
    // { "a": [{ "b": { "c": 3 } }] }
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.asJsonObject(json);
    // => null 

    json = JsonNull.INSTANCE
    jsonLever.asJsonObject(json, new JsonObject());
    // => {}

    json = new JsonPrimitive("test")
    jsonLever.asJsonObject(json);
    // => null

    json = new JsonPrimitive(1)
    jsonLever.asJsonObject(json, new JsonObject());
    // => {}    
    ```

5.  `public JsonArray asJsonArray(JsonElement source)`
6.  `public JsonArray asJsonArray(JsonElement source, JsonArray defaultArray)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.asJsonArray(json);
    // null
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.asJsonArray(json);
    // => ["a", "b", 1, true, {"x": "z"}] 

    json = JsonNull.INSTANCE
    jsonLever.asJsonArray(json);
    // => null

    json = new JsonPrimitive("test", new JsonArray())
    jsonLever.asJsonArray(json);
    // => []

    json = new JsonPrimitive(1)
    jsonLever.asJsonArray(json, new JsonArray());
    // => []  
    ```

7.  `public JsonElement asJsonElement(JsonElement source, JsonElement defaultJson)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.asJsonElement(json, JsonNull.INSTANCE);
    // { "a": [{ "b": { "c": 3 } }] }
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.asJsonElement(json, JsonNull.INSTANCE);
    // => ["a", "b", 1, true, {"x": "z"}] 

    json = JsonNull.INSTANCE
    jsonLever.asJsonElement(json, new JsonPrimitive("default"));
    // => "default"

    json = new JsonPrimitive("test", new JsonArray())
    jsonLever.asJsonElement(json);
    // => "test"

    json = new JsonPrimitive(1)
    jsonLever.asJsonElement(json, new JsonArray());
    // => 1  
    ```

8.  `public String asString(JsonElement source)`
9.  `public String asString(JsonElement source, String defaultStr)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.asString(json);
    // null
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.asString(json, "default");
    // => "default"

    json = JsonNull.INSTANCE
    jsonLever.asString(json, "default");
    // => "default"

    json = new JsonPrimitive("test")
    jsonLever.asString(json);
    // => "test"

    json = new JsonPrimitive(1)
    jsonLever.asString(json);
    // => null  
    ```

10.  `public Boolean asBoolean(JsonElement source)`
11.  `public Boolean asBoolean(JsonElement source, Boolean defaultBool)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.asBoolean(json);
    // null
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.asBoolean(json, Boolean.TRUE);
    // => true

    json = JsonNull.INSTANCE
    jsonLever.asBoolean(json);
    // => null

    json = new JsonPrimitive("test")
    jsonLever.asBoolean(json, Boolean.FALSE);
    // => false

    json = new JsonPrimitive(Boolean.TRUE)
    jsonLever.asBoolean(json);
    // => true  
    ```

12.  `public Long asLong(JsonElement source)`
13.  `public Long asLong(JsonElement source, Long defaultLong)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.asLong(json);
    // null
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.asLong(json, 1l);
    // => 1

    json = JsonNull.INSTANCE
    jsonLever.asLong(json);
    // => null

    json = new JsonPrimitive("test")
    jsonLever.asLong(json, 2l);
    // => 2

    json = new JsonPrimitive(3)
    jsonLever.asLong(json);
    // => 3  
    ```

14.  `public Integer asInt(JsonElement source)`
15.  `public Integer asInt(JsonElement source, Integer defaultInt)`
    **Example**
    ```java
    json = { "a": [{ "b": { "c": 3 } }] };
 
    jsonLever.asLong(json);
    // null
    
    json = ["a", "b", 1, true, {"x": "z"}]
    jsonLever.asLong(json, 1);
    // => 1

    json = JsonNull.INSTANCE
    jsonLever.asLong(json);
    // => null

    json = new JsonPrimitive("test")
    jsonLever.asLong(json, 2);
    // => 2

    json = new JsonPrimitive(3)
    jsonLever.asLong(json);
    // => 3  
    ```

### Utilities - JsonLever.get*

There many other utility functions exposed by JsonLever. Let's look at them one by one.

>Since - 2.0.8

1.  `public static JsonLever getInstance()`<br>
    We have seen this before; this is the way to grab the Singleton Instance of JsonLever<br>
    ```java
    JsonLever jsonLever = JsonLever.getInstance();
    ```
2.  `public JsonParser getJsonParser()`<br>
    JsonLever uses [Gson's](https://github.com/google/gson) JsonParser object internally, to parse jsons.<br>
    The utility method, let's us grab hold of the JsonParser instance in use.
    ```java
    JsonLever jsonLever = JsonLever.getInstance();
    JsonParser parser = jsonLever.getJsonParser();
    ```
3.  `public JsonParser getGson()`<br>
    JsonLever uses [Gson's](https://github.com/google/gson) Gson object internally, to serialize and deserialize POJOs.<br>
    The utility method, let's us grab hold of the Gson instance in use.
    ```java
    JsonLever jsonLever = JsonLever.getInstance();
    Gson gson = jsonLever.getGson();
    ```
4.  `public JsonElement getFirst(Iterable<JsonElement> jsonList)`
    A null-safe way of retrieving the first json element from an Iterables of JsonElement, if available. Else returns null

    **Arguments**<br>
    *   **jsonList**<br>
    The json list. It can be one of the below
        *   JsonArray<br>
            Gson's JsonArray representing an array of JsonElements
        *   Iterable<JsonElement>
            Any java iterable of JsonElement

    **Returns**<br>
    Returns the first json element if available else returns `null`

    ```java
    JsonLever jsonLever = JsonLever.getInstance();

    json = [1, "2"]
    jsonLever.getFirst(json)
    // 1

    json = null
    jsonLever.getFirst(json)
    // null
    ```

5.  `public JsonArray getJpath(String jpath)`
6.  `public JsonArray getJpath(JsonElement jpath)`<br>
    Gets the jpath for a '.' separated string defining the fully qualified path of a field in Json.<br>
    Array Indexes are referred via numbers. If the input is anything apart from String, JsonPrimitive String or JsonArray, it returns null
    **Example**<br>
    ```java
    JsonLever jsonLever = JsonLever.getInstance();

    path = "a.b.0.4.c";
    jsonLever.getJpath(path);
    // ["a", "b", 0, 4, "c"]

    path = {'a': 'b'};
    jsonLever.getJpath(path);
    // null
    ```

### Utilities - Other Utilities

There many other utility functions exposed by JsonLever. Let's look at them one by one.

>Since - 2.0.8

1.  `public JsonArray newJsonArray(Object... path)`<br>
    Returns a new Json Array for the series of inputs. Only primitive inputs and jsons are allowed.
    **Example**<br>
    ```java
    JsonLever jsonLever = JsonLever.getInstance();

    jsonLever.newJsonArray("a", "1", 2, {'a': 'b'});
    // ["a", "1", 2, {'a': 'b'}]
    ```

2. `public String toString(JsonElement source)`<br>
    Null safe toString method for a json element.
    **Example**<br>
    ```java
    JsonLever jsonLever = JsonLever.getInstance();

    jsonLever.toString({'a': 'b'});
    // {'a': 'b'}

    jsonLever.toString(null);
    // null
    ```

3.  `public JsonElement deepClone(JsonElement source)`
4.  `public JsonObject deepClone(JsonObject source)`
5.  `public JsonArray deepClone(JsonArray source)`
6.  `public JsonPrimitive deepClone(JsonPrimitive source)`<br>
    Null safe, clone operation on JsonElements. Can deep clone the entire json and its hierarchy.<br>
    Is a comparatively heavy process
    **Example**<br>
    ```java
    JsonLever jsonLever = JsonLever.getInstance();

    jsonLever.deepClone({'a': 'b'});
    // {'a': 'b'}

    jsonLever.deepClone(null);
    // null
    ```

7.  `public JsonElement subset(JsonElement source, JsonArray jPathArray)`<br>
    Returns the subset of the input for all the paths specified in the jPathArray.

    **Example**<br>
    ```java
    JsonLever jsonLever = JsonLever.getInstance();

    json = {'a': {'a1': 1, 'a2': 2}, 'b': {'b1': 1, 'b2': 2}, 'c': [1, 2, 3]}
    jsonLever.subset(json, ['a.a1', 'b.b2', 'c.1']);
    // {'a': {'a1': 1}, 'b': {'b2': 2}, 'c': [null, 2]}
    ```

8.  `public JsonObject simpleMerge(JsonObject to, JsonObject from)`
    `public JsonObject simpleMerge(JsonObject to, JsonObject... from)`<br>
    Merges the first level fields on two jsons

    **Example**<br>
    ```java
    JsonLever jsonLever = JsonLever.getInstance();

    a = {'a': {'a1': 1}}
    b = {'b': {'b2': 2}}
    jsonLever.simpleMerge(a, b);
    // {'a': {'a1': 1}, 'b': {'b2': 2}}
    ```

9.  `public JsonElement merge(JsonElement source1, JsonElement source2)`
10. `public JsonElement merge(JsonElement source1, JsonElement source2, Boolean mergeArray)`<br>
    Recursive merge of two json objects.

    **Example**<br>
    ```java
    JsonLever jsonLever = JsonLever.getInstance();

    a = {'a': {'a1': {'a2': 'a3'}}}
    b = {'a': {'a1': {'a4': 'a5'}}}
    jsonLever.merge(a, b);
    // {'a': {'a1': {'a2': 'a3', 'a4': 'a5'}}}
    ```


Hop over here for the [documentation index](_Sidebar.md)




