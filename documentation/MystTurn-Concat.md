The features of the Concat MystTurn is as below:

* If the type is specified, the value should be equal to "concat"
* The Concat MystTurn, concatenates multiple source fields using a configured separator
* The Concat MystTurn can operate on multiple source fields
* When multiple fields are defined in the rule, this turn will stringify every source field and concatenate them using the configured separator
* Concat MystTurn takes one additional attribute by the name "separator"

## Time for a road trip 
Let's jump back to packing items for our road trip

We have a music player in our wardrobe which we wish to carry for the road trip. We also have the earphone. We also have the charger and batteries as below

**inputJson**
```json
{
  "wardrobe": {
    "player": {
      "name": "musicPlayer"
    },
    "earphone": {
      "name": "noise-cancelled-earpiece"
    },
    "charger": {
      "name": "energizer"
    },
    "batteries": {
      "type": "li-ion",
      "number": 4
    }
  }
}
```

We wish to tie the player and earphone together using the "~" strap. We also wish to put the charger and batteries together using the "#" strap. So the output json would be as below 

**outputJson**
```json
{
  "backpack": {
    "player": "musicPlayer~noise-cancelled-earpiece",
    "charger": "energizer#li-ion#4"
  }
}
```

The ruleset file for the above is
**Ruleset**
```json
[{
	"from": [ ["wardrobe", "player", "name"], ["wardrobe", "earphone", "name"] ],
	"to": ["backpack", "player"],
	"turn": {
		"type": "concat",
		"separator": "~"
	}
},
{
	"from": [ ["wardrobe", "charger", "name"], ["wardrobe", "batteries", "type"], ["wardrobe", "batteries", "number"] ],
	"to": ["backpack", "charger"],
	"turn": {
		"type": "concat",
		"separator": "#"
	}
}
]
```

> The same can be found as **concat06** in the JsonMystique [BDD](../json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Additional attributes for Concat MystTurn

* separator
 * This attribute is of type string
 * This defines the separator that needs to be used for concatenation
 * If no separator is specified, it uses an empty string

## Structure of the Concat MystTurn

The below is the basic structure of the json

```json
{
	"from": [["from1", "from2"], ["from3", "from4"]],
	"to": ["to1", "to2"],
	"optional": null / true / false,
	"turn": null / {
		"type": null / "concat",
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
	    "separator": "sep"
	}
}
```

Hop over here for the [documentation index](_Sidebar.md)