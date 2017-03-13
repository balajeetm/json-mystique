The features of the Constant MystTurn is as below:

* If the type is specified, the value should be equal to "constant"
* The Constant MystTurn copies a defined constant value into the destination
* The constant can be any json element (primitive, json object or array)
* The Constant MystTurn does not require a source field to operate on
* Constant MystTurn takes an additional attributes by name "value"

## Additional attributes for Concat MystTurn

* value
 * This attribute defines the constant value that needs to be placed to the destination
 * The value can be any json element (primitive, json object or json array)

## Time for a road trip 
Let's jump back to packing items for our road trip

We would want to uniquely identify the backpack, just in case it gets jumbled up with others during the roadtrip. So we would want to tag our backpack. With lets say, our name and Id

**inputJson
Does not need any fields from the inputJson


The ruleset file for the above is
**Ruleset**
```json
[{
	"to": ["backpack", "tag"],
	"turn": {
		"type": "constant",
		"value": {
			"name": "Dheeru",
			"id": "DH01"
		}
	}
}]
```

**outputJson**
```json
{
  "backpack": {
    "tag": {
      "name": "Dheeru",
      "id": "DH01"
    }
  }
}
```

> The same can be found as **constant08** in the JsonMystique [BDD](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Structure of the Constant MystTurn

The below is the basic structure of the json

```json
{
	"from": notused,
	"to": ["to1", "to2"],
	"optional": null / true / false,
	"turn": null / {
		"type": null / "constant",
		"default": null / {
			"value": {
				"field1": "field1"
			},
			"turn": {
				"from": ["from5"]
			},
		},
		"from": null / ["from6"],
		"to": null / ["to3", "to4"],
		"value": "expectedValue"
	}
}
```