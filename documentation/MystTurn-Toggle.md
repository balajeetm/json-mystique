The features of the Toggle MystTurn is as below:

* If the type is specified, the value should be equal to "toggle"
* The Toggle MystTurn can operate on single or multiple fields
* The sourceField can be any jsonElement (json object or array)
* There are times when we want to execute multiple turns for a single source field/set of source fields which might operate on different attributes of the source fields. But we need the value of the first successful execution of a turn. A successful execution of turn is defined as the execution of a turn which returns a non null value
* We may use this when we need to execute a different turn for a source field based on varying conditions
* Toggle MystTurn takes an additional attribute by the name "turns"
* Though Toggle MystTurn executes multiple turns internally, the outcome of this turn is always the outcome for the first successful turn execution

## Additional attributes for Toggle MystTurn

* turns
 * This attribute defines the different turns that need to be executed for the source fields identified
 * Every item within this array refers to a turn

## Time for a road trip 
Let's jump back to packing items for our road trip

We would want to uniquely identify the backpack, just in case it gets jumbled up with others during the roadtrip. So we would want to tag our backpack. With lets say, our name and blood group for obvious reason. So, we have an id in the wardrobe and we want to pick values from there. But we do not know which id it is. It could be a driving license or college id. Based on what id it is, we will want to pick fields from different places. Let's use toggle mystique for this

**inputJson
```json
{
  "wardrobe": {
    "id": {
      "type": "drivingLicense",
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
	"from": ["wardrobe", "id", "name"],
	"to": ["backpack", "tag", "name"]
}, {
	"from": ["wardrobe", "id"],
	"to": ["backpack", "tag", "bloodGroup"],
	"turn": {
		"type": "toggle",
		"turns": [{
			"from": ["bGroup"]
		}, {
			"from": ["bloodGroup"]
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

> The same can be found as **toggle15** in the JsonMystique [BDD](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Structure of the Toggle MystTurn

The below is the basic structure of the json

```json
{
	"from": ["from1", "from2"],
	"to": ["to1", "to2"],
	"optional": null / true / false,
	"turn": null / {
		"type": "toggle",
		"from": null / ["from6"],
		"to": ["to3", "to4"],
		"default": null / {
			"value": {
				"field1": "field1"
			},
			"turn": {
				"from": ["from5"]
			},
		},
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