JsonLever is an utility packaged with JsonMystique to easily operate on jsons

## Why JsonLever
JsonLever makes operating with Jsons easier by taking the hassle out of working with arrays, numbers, objects, strings, etc.<br>
JsonLever's methods are modular and are wonderful for:
*   Iterating json arrays, json objects, & json primitives (string, number, boolean etc)
*   Manipulating jsons, retrieving and updating values
*   Cloning Jsons
*   Retrieving subset of a json or selecting specific fields from a json

## Usage Guide

Grab hold of the JsonLever as below:

```java
JsonLever jsonLever = JsonLever.getInstance();
```

If you are using the `json-mystique-starter`, you can just autowire JsonLever as below:

```java
@Autowired JsonLever jsonLever;
```

>NOTE<br>
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
*   <u>**source**</u><br>
    The json source. It can be one of the below
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)
    *   Object<br>
        Can be any Java POJO, that can be jsonified, or a String representing a valid json
*   <u>**jpath**</u><br>
    Defines the fully qualified json path to the field required. It can be one of the below:
    *   <u>String</u><br>
        '.' separated string defining the fully qualified json path.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = "a.0.b.c"
        jsonLever.get(json, path);
        // => 3
        ```
    *   <u>JsonArray</u><br>
        JsonArray representing the fully qualified json path. Array indexes are represented as numerals.<br>
        Only strings and numerals are allowed in the json array. String represent a json object"s fieldname and numerals represent array indexes.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = ["a", 0, "b", "c"]
        jsonLever.get(json, path);
        // => 3
        ```
    *   <u>Object[] - Object Array</u><br>
        Object Array representing the fully qualified json path<br>
        Only strings and numerals are allowed in the json array. String represents a json object"s fieldname and numerals represent array indexes.<br>
        The object can also be JsonPrimitives representing strings and numerals.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = ["a", 0, "b", "c"]
        jsonLever.get(json, path);
        // => 3
*   <u>defaultValue</u><br>
    Represents the value defined for undefined resolved values. It can be one of the below:
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

**Returns**<br>
Returns the resolved value as:
    *   <u>**JsonElement**</u><br>
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
*   <u>**source**</u><br>
    The json source. It can be one of the below
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)
*   <u>**jpath**</u><br>
    Defines the fully qualified json path to the field required. It can be one of the below:
    *   <u>String</u><br>
        '.' separated string defining the fully qualified json path.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = "a.0.b.c"
        jsonLever.set(json, path, new JsonPrimitive("4"));
        System.out.println(json)
        // => { "a": [{ "b": { "c": "4" } }] }
        ```
    *   <u>JsonArray</u><br>
        JsonArray representing the fully qualified json path. Array indexes are represented as numerals.<br>
        Only strings and numerals are allowed in the json array. String represent a json object's fieldname and numerals represent array indexes.<br>
        ```java
        json = { "a": [{ "b": { "c": 3 } }] };
        path = ["x", 1, "y", "z"]
        jsonLever.set(json, path, new JsonPrimitive("5"));
        // => { "a": [{ "b": { "c": 3 } }], "x": [null, {"y": "z"}] }
        ```
*   <u>**value**</u><br>
    The value to set. It can be one of the below:
    *   JsonElement<br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

**Returns**<br>
Returns the source json with value set:
    *   <u>**JsonElement**</u><br>
        Gson's JsonElement (JsonObject, JsonArray, JsonPrimitive or JsonNull)

Hop over here for the [documentation index](_Sidebar.md)




