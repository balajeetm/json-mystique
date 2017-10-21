Json Comparator is a utility class that let's you check if one Json is a subset of another Json

The class can be found [here](../json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/lever/JsonComparator.java)

## Usage Guide

### Step 1
Grab hold of the JsonComparator as below:

```java
JsonComparator comparator = JsonComparator.getInstance();
```

If you are using the `json-mystique-starter`, you can just autowire JsonComparator as below:

```java
@Autowired JsonComparator comparator;
```

### Step 2
Invoke the isSubset method.

There are two variants
 * `public Comparison isSubset(String subsetStr, String actualStr)`

   Checks if stringified json **subsetStr** is a subset of stringified json **actualStr**

 * `public Comparison isSubset(JsonElement subset, JsonElement actual)`

   Checks if json **subset** is a subset of json **actual**

The [Comparison](../json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/lever/Comparison.java) contains a boolean attribute called result that denotes if the subset json is the subset of the actual json.

In the case that the subset json is not the subset of the actual json, the error messages are returned as part of the [Comparison](../json-mystique-utils/gson-utils/src/main/java/com/balajeetm/mystique/util/gson/lever/Comparison.java) 

Hop over here for the [documentation index](_Sidebar.md)