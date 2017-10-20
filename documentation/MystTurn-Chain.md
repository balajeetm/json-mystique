The features of the Chain MystTurn is as below:

* If the type is specified, the value should be equal to "chain"
* The Chain MystTurn can operate on single or multiple fields
* The sourceField can be any jsonElement (json object or array)
* There are times when we want to execute multiple turns for a single source field/set of source fields which might operate on different attributes of the source fields. Chain MystTurn helps in this case
* We may use this to group processing of attributes within source field
* Chain MystTurn takes an additional attribute by the name "turns"
* Since Chain MystTurn executes multiple turns internally, the outcome of this turn is always a null Json. So the process of copying the transformation to the destination should be done within each internal turn that Chain MystTurn executes

## Additional attributes for Mystique MystTurn

* turns
 * This attribute defines the different turns that need to be executed for the source fields identified
 * Every item within this array refers to a turn

## Time for a road trip 
Let's jump back to packing items for our road trip

We would want to uniquely identify the backpack, just in case it gets jumbled up with others during the roadtrip. So we would want to tag our backpack. With lets say, our name and blood group for obvious reason. So, we have our driving license in the wardrobe and we want to pick values from there. Ideally we can write separate tarots for each field (tag and blood group), all of which are coming from our driving license. We will process license at one shot

**inputJson
```json
{
  "wardrobe": {
    "drivingLicense": {
      "name": "Dheeru",
      "licenseNum": "xx-yy-zz",
      "validFrom": "01-01-2010",
      "validTo": "01-01-2030",
      "bloodGroup": "B +ve"
    }
  }
}
```


The ruleset file for the above is
**Ruleset**
```json
[{
	"from": ["wardrobe", "drivingLincense"],
	"turn": {
		"type": "chain",
		"turns": [{
			"from": ["name"],
			"to": ["backpack", "tag", "name"]
		}, {
			"from": ["bloodGroup"],
			"to": ["backpack", "tag", "bloodGroup"]
		}]
	}
}]
```

**outputJson**
```json
{
  "backpack": {
    "tag": {
      "name": "Dheeru",
      "bloodGroup": "B +ve"
    }
  }
}
```

So we can see the genre is picked appropriately for every dvd

> The same can be found as **chain14** in the JsonMystique [BDD](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Structure of the Chain MystTurn

The below is the basic structure of the json

```json
{
	"from": ["from1", "from2"],
	"optional": null / true / false,
	"turn": null / {
		"type": "chain",
		"from": null / ["from6"],
		"turns": [{
				"type": "turnType",
				"default": null / {
					"value": {
						"field1": "field1"
					},
					"turn": {
						"from": ["from5"]
					},
				},
				"from": null / ["from6"],
				"to": ["to3", "to4"]
			}

		]
	}
}
```