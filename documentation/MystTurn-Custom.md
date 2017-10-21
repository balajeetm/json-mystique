The features of the Custom MystTurn is as below:

* If the type is specified, the value should be equal to "bean"
* The Custom MystTurn can define any number custom attributes within the turn

## Additional attributes for Custom MystTurn

Can use any number of custom attributes

## How to write a Custom MystTurn

To write a custom MystTurn, write a class as below:
* Implement the [com.futuresight.util.mystique.MystTurn](../json-mystique-libs/json-mystique/src/main/java/com/futuresight/util/mystique/MystTurn.java) interface
* The interface has a single method with the below signature

`JsonElement transform(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn, JsonObject resultWrapper)`

* The arguments of the "transform" method mean the below
  * source denotes, the input source fields identified for processing
  * deps, the global dependencies
  * aces, the local dependencies for this turn
  * turn, the turn json defined. One can read all custom properties from this json object as below
    `turn.get("customProp")`
  * resultWrapper, the final result object. Since turns can update result json directly, we need the reference to the result Wrapper

* However, this is a very elementary way of implementing a MystTurn. You will have to do the below manually
  * Updating local aces within the turn
  * Returning a default value in case the transformation returns a null object
  * Copying the transformed output directly to output from within the turn
  * Optionally skipping the copy to the output json if the transformed value is null
  * Get granular source field from within the source field passed

* This requirements are very generic to all turns. So if you want to reuse these features, **extend** another class instead called [com.futuresight.util.mystique.AbstractMystTurn](../json-mystique-libs/json-mystique/src/main/java/com/futuresight/util/mystique/AbstractMystTurn.java)

* In this case, implement the below method

`JsonElement transmute(List<JsonElement> source, JsonObject deps, JsonObject aces, JsonObject turn)`

* Remember, this turn must be a spring managed bean. Turns are generally stateless and singleton and thus you can use "@Component" annotation to create this bean

## Time for a road trip 
Let's jump back to packing items for our road trip

Remember, we had a music player in our wardrobe which we wished to carry for the road trip. We also had the earphone. We carried this using the [ConcatMystTurn](MystTurn-Concat.md). Lets implement a custom mystTurn for the same


**inputJson**
```json
{
  "wardrobe": {
    "player": {
      "name": "musicPlayer"
    },
    "earphone": {
      "name": "noise-cancelled-earpiece"
    }
  }
}
```

We wish to tie the player and earphone together using the "~" strap. So the output json would be as below 

**outputJson**
```json
{
  "backpack": {
    "player": "musicPlayer~noise-cancelled-earpiece"
  }
}
```

The ruleset file for the above is
**Ruleset**
```json
[{
	"from": [
		["wardrobe", "player", "name"],
		["wardrobe", "earphone", "name"]
	],
	"to": ["backpack", "player"],
	"turn": {
		"type": "custom",
		"value": "com.futuresight.util.mystique.CustomMystTurn"
	}
}]
```

> The same can be found as **custom16** in the JsonMystique [BDD](../json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself. Also refer to the [CustomMystTurn](../json-mystique-libs/json-mystique/src/test/java/com/futuresight/util/mystique/CustomMystTurn.java)

Hop over here for the [documentation index](_Sidebar.md)