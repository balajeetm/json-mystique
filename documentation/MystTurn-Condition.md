The features of the Condition MystTurn is as below:

* If the type is specified, the value should be equal to "condition"
* The Condition MystTurn, transforms the input field based on a condition
* It can transform into different outputs based on whether the condition is successfully satisfied or not
* The Condition MystTurn can operate on single field only
* Condition MystTurn takes additional attributes such as "value", "true", "false"

## Additional attributes for Condition MystTurn

* value
 * This attribute defines the value that the input source field needs to be matched with
 * The value can be any json element (primitive, json object or json array)
 * If the value is null, Json Mystique checks for the existence of the input field, i.e, the condition is successfully is satisfied if the input source field is a non null value

* true
 * This attribute defines the object to be return when the condition is successfully satisfied
 * This attribute is of type json
 * The structure of this json is exactly similar to that of a [default](Attribute-Turn.md#the-structure-of-default)
 * So, you can basically return a constant value (any jsonelement) if the condition is successfully satisfied or execute a turn that returns a value
* If this attribute is missing and the condition is successfully satisfied, the turn returns a Boolean value equal to "true"


* false
 * This attribute defines the object to be return when the condition is not successfully satisfied
 * This attribute is of type json
 * The structure of this json is exactly similar to that of a [default](Attribute-Turn.md#the-structure-of-default)
 * So, you can basically return a constant value (any jsonelement) if the condition is successfully satisfied or execute a turn that returns a value
* If this attribute is missing and the condition is not successfully satisfied, the turn returns a Boolean value equal to "false"

## Time for a road trip 
Let's jump back to packing items for our road trip

We need to pack shoes for the trip. I preferably want to carry trekking shoes. So if trekking shoes are present in my wardrobe, I'll pack them, else, I'll pack my sports shoes. If neither is present, I'll buy a new nike shoe

**inputJson
```json
{
  "wardrobe": {
    "shoes": {
      "sports": {
        "type": "nike",
        "size": "12"
      }
    }
  }
}
```


The ruleset file for the above is
**Ruleset**
```json
[{
	"from": ["wardrobe", "shoes", "trekking"],
	"to": ["backpack", "shoe"],
	"turn": {
		"type": "condition",
		"true": {
			"turn": {
				"from": ["@ace(available)", "trekking"]
			}
		},
		"false": {
			"turn": {
				"from": ["@ace(available)", "sports"]
			}
		},
		"default": {
			"value": {
				"type": "nike",
				"descr": "new shoe",
				"size": "13"
			}
		}
	},
	"aces": {
		"available": {
			"from": ["wardrobe", "shoes"]
		}
	}
}]
```

**outputJson**
```json
{
  "backpack": {
    "shoe": {
      "type": "nike",
      "size": "12"
    }
  }
}
```

So we can see that the sports show is packed in the backpack since trekking shoes are not available.

### Why pre-process the ace?

Within the scope of the turn, all the processing can only be performed on the source element. However, in the above case, we want to pick an alternate field from the source json if an expected field is not available.

So we pre-process the content that we need from the original json and keep it handy for later use.

> The same can be found as **condition07** in the JsonMystique [BDD](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Structure of the Condition MystTurn

The below is the basic structure of the json

```json
{
	"from": [
		["from1", "from2"],
		["from3", "from4"]
	],
	"to": ["to1", "to2"],
	"optional": null / true / false,
	"turn": null / {
		"type": null / "condition",
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
		"value": "expectedValue",
		"true": null / {
			"value": {
				"field1": "field1"
			},
			"turn": {
				"from": ["from7"]
			},
		},
		"false": null / {
			"value": {
				"field1": "field1"
			},
			"turn": {
				"from": ["from8"]
			},
		}
	}
}
```