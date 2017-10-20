If you are working with Jsons, one or other time you would have got a need to merge jsons. Unfortunately and strangely there are not mainly libraries in Java to provide this feature. So JsonMystique comes with an utility to merge jsons.

It tries to replicate the [lodash](https://lodash.com) utilityMethods called "merge" and "mergeWith"

## Usage Guide

### Step 1
As previously stated, Json Mystique uses spring for dependency injection and wiring.
Json Mystique objects thus should be added to the component scan path so that they can be boot loaded
So if you are using Spring already, add the below Json Mystique config to the class path as below

`@Import(value={JsonMystiqueConfig.class})`

If you are using xml as a spring configuration file, add the below

`<context:component-scan base-package="com.futuresight.util.mystique" />`

### Step 2
There is a Utility Class [JsonLever](https://github.com/balajeetm/json-mystique/blob/master/src/main/java/com/futuresight/util/mystique/JsonLever.java)

Autowire the JsonLever class into the class where you wish to perform the json merge. This can be done as below

`@Autowired`
`private JsonLever jsonLever;`

Or if your class had access to application context

`JsonLever jsonLever = context.getBean(JsonLever.class);`

### Step 3
Invoke the merge method

`public JsonElement merge(JsonElement src1, JsonElement src2)`

This method recursively merges own and inherited enumerable string keyed properties of source objects and returns the destination object. Source properties that resolve to undefined are skipped if a destination value exists. Array and plain object properties are merged recursively. Other objects and value types are overridden by assignment. Source objects are applied from left to right. Subsequent sources overwrite property assignments of previous sources.

## Array merge

If the the array field exists in both the source objects, then for every item in the array, the item is merge with the similarly placed item in the other source array

### Example

`JsonObject object = {`
  `'a': [{ 'b': 2 }, { 'd': 4 }]`
`};`
 
`JsonObject other = {`
  `'a': [{ 'c': 3 }, { 'e': 5 }]`
`};`
 
`jsonLever.merge(object, other);`
`=> { 'a': [{ 'b': 2, 'c': 3 }, { 'd': 4, 'e': 5 }] }`

If you wish to just merge the two arrays and not the individual items in the array the use the below

`public JsonElement merge(JsonElement src1, JsonElement src2, Boolean mergeArray)`

### Example

`JsonObject object = { 'a': [1], 'b': [2] };`
`JsonObject other = { 'a': [3], 'b': [4] };`
 
`jsonLever.merge(object, other, Boolean.TRUE);`
`=> { 'a': [1, 3], 'b': [2, 4] }`

> The same can be found as **merge** and **mergeArray** in the JsonLever [BDD](https://github.com/balajeetm/json-mystique/blob/master/src/test/java/com/futuresight/util/mystique/JsonLeverBDDTest.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself
