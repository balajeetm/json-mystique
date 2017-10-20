Json Comparator is a utility class that let's you check if one Json is a subset of another Json

The class can be found [here](https://github.com/balajeetm/json-mystique/blob/master/src/main/java/com/futuresight/util/mystique/lever/JsonComparator.java)

## Usage Guide

### Step 1
As previously stated, Json Mystique uses spring for dependency injection and wiring.
Json Mystique objects thus should be added to the component scan path so that they can be boot loaded
So if you are using Spring already, add the below Json Mystique config to the class path as below

`@Import(value={JsonMystiqueConfig.class})`

If you are using xml as a spring configuration file, add the below

`<context:component-scan base-package="com.futuresight.util.mystique" />`

### Step 2
There is a Utility Class [JsonComparator](https://github.com/balajeetm/json-mystique/blob/master/src/main/java/com/futuresight/util/mystique/lever/JsonComparator.java)

Autowire the JsonComparator class into the class where you wish to perform the json merge. This can be done as below

`@Autowired`
`private JsonComparator comparator;`

Or if your class had access to application context

`JsonComparator comparator = context.getBean(JsonComparator.class);`

### Step 3
Invoke the isSubset method.

There are two variants
 * `public MystResult isSubset(String subsetStr, String actualStr)`

   Checks if stringified json **subsetStr** is a subset of stringified json **actualStr**

 * `public MystResult isSubset(JsonElement subset, JsonElement actual)`

   Checks if json **subset** is a subset of json **actual**

The [MystResult](https://github.com/balajeetm/json-mystique/blob/master/src/main/java/com/futuresight/util/mystique/lever/MystResult.java) contains a boolean attribute called result that denotes if the subset json is the subset of the actual json.

In the case that the subset json is not the subset of the actual json, the error messages are returned as part of the [MystResult](https://github.com/balajeetm/json-mystique/blob/master/src/main/java/com/futuresight/util/mystique/lever/MystResult.java)