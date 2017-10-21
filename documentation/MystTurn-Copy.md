The features of the Copy MystTurn is as below:

* This is the default turn, i.e, if a turn is not specified or if the type attribute within a turn is not specified, it is presumed to be a Copy MystTurn
* If the type is specified, the value should be equal to "copy"
* The Copy MystTurn, blindly copies fields from input Json to the output
* The Copy MystTurn can operate on multiple source fields
* When multiple fields are defined in the rule, copy turn will put all the chosen fields into a JsonArray and copy the same to the destination field
* Copy MystTurn requires no other additional metadata

## Time for a road trip 
Let's get back to packing our backpack for the roadtrip.
Just to recap, we are getting ready for a road trip and we are trying to gather items for the road trip.
The input json denotes the items that exists in the house at various places and the output json specifies where these items need to be for the road trip.

Cool, now that the stage is set. Let's begin

### Simple copy of fields
The input json defines my wardrobe which has a shirt, pant and a towel. I want to put them into my backpack defined by the output json

**inputJson**
```json
{
  "wardrobe": {
    "shirt": {
      "color": "red",
      "size": "40"
    },
    "pant": {
      "color": "blue",
      "size": "39"
    },
    "towel": {
      "color": "white",
      "size": "15"
    }
  }
}
```

**outputJson**
```json
{
  "backpack": {
    "shirt": {
      "color": "red",
      "size": "40"
    },
    "trouser": {
      "color": "blue",
      "length": "39"
    },
    "handtowel": {
      "color": "white"
    }
  }
}
```

> Observe that the items are moved into backpack and pant is renamed to trouser, with its size renamed to length and towel to handtowel

The ruleset file for the above is
**Ruleset**
```json
[
  {
    "from": "wardrobe.shirt.color",
    "to": "backpack.shirt.color"
  },
  {
    "from": "wardrobe.shirt.size",
    "to": "backpack.shirt.size"
  },
  {
    "from": "wardrobe.pant.color",
    "to": "backpack.trouser.color"
  },
  {
    "from": "wardrobe.pant.size",
    "to": "backpack.trouser.length"
  },
  {
    "from": "wardrobe.towel.color",
    "to": "backpack.handtowel.color"
  }
]
```

So we see the ruleset is a simple array of transformations, specifying where to pick the field **"from"** and where to paste the corresponding field **"to"**.

Observe here, that unlike the fields of pant and towel, the fields of shirt are copied as is, so instead of copying every field from shirt, we can copy the entire shirt json itself.

After all, **json mystique, let's you transform/copy any json field, be it primitive (string, number, boolean) or complex (array/object)**

So, let's transform the ruleset as below
```json
[
  {
    "from": "wardrobe.shirt",
    "to": "backpack.shirt"
  },
  {
    "from": "wardrobe.pant.color",
    "to": "backpack.trouser.color"
  },
  {
    "from": "wardrobe.pant.size",
    "to": "backpack.trouser.length"
  },
  {
    "from": "wardrobe.towel.color",
    "to": "backpack.handtowel.color"
  }
]
```

> The same can be found as **simpleCopy01** in the JsonMystique [BDD](../json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

### Picking fields from arrays

Its highly unlikely to have just one shirt in the wardrobe, let's say there were three shirts and we want to pick the first one for the trip.
Also, there is a pouch to keep the handtowel separately, and we wish to keep the handtowel in the second rack and not the first. So the input and output json is transformed as below

**inputJson**
```json
{
  "wardrobe": {
    "shirt": [
      {
        "color": "red",
        "size": "40"
      },
      {
        "color": "blue",
        "size": "40"
      },
      {
        "color": "orange",
        "size": "42"
      }
    ],
    "pant": {
      "color": "blue",
      "size": "39"
    },
    "towel": {
      "color": "white",
      "size": "15"
    }
  }
}
```

**outputJson**
```json
{
  "backpack": {
    "shirt": {
      "color": "red",
      "size": "40"
    },
    "trouser": {
      "color": "blue",
      "length": "39"
    },
    "handtowel": [
      null,
      {
        "color": "white"
      }
    ]
  }
}
```

> Observe how the first item in the handtowel rack of output is null, since we only filled the second item

The ruleset for the above is
```json
[{
"from": ["wardrobe", "shirt", 0],
"to": ["backpack", "shirt"]
}, {
"from": ["wardrobe", "pant", "color"],
"to": ["backpack", "trouser", "color"]
}, {
"from": ["wardrobe", "pant", "size"],
"to": ["backpack", "trouser", "length"]
}, {
"from": ["wardrobe", "towel", "color"],
"to": ["backpack", "handtowel", 1, "color"]
}]
```

> The same can be found as **simpleCopy02** in the JsonMystique [BDD](../json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Additional attributes for Copy MystTurn

* value
 * This attribute defines what attributes from "source field" which needs to be copied to the destination
 * The value one of the below types
    * Array of Strings - In which case, the field denoted by this fully qualified path is retrieved from the "source field"
    * Array of Array of Strings - In which case, every (array of strings) denotes a fully qualified path and json mystiques retrieves these fields and places them into a json object and returns the json object
    * Json object - In this case, it refers to a turn and transforms the "identified field" as per the turn specification
 * If the value is null, Json Mystique returns the "source field" as is

### Getting a subset of a json

The above value attribute can be used to pick a subset of a json.
Provide an array of string arrays to denote the fields from the source json that need to be copied to destination

Let's say we later decided to carry only the shirt and trouser and leave the handTowel at home, since the handTowel will be available at the trip site. This is how the mystique file would look

**inputJson**
```json
{
  "backpack": {
    "shirt": {
      "color": "red",
      "size": "40"
    },
    "trouser": {
      "color": "blue",
      "length": "39"
    },
    "handtowel": [
      null,
      {
        "color": "white"
      }
    ]
  }
}
```


**outputJson**
```json
{
  "backpack": {
    "shirt": {
      "color": "red",
      "size": "40"
    },
    "trouser": {
      "color": "blue",
      "length": "39"
    }
  }
}
```

The ruleset for the above is
```json
[{
	"from": [],
	"to": [],
	"turn": {
		"value": [
			["backpack", "shirt"],
			["backpack", "trouser"]
		]
	}
}]
```

## Structure of the Copy MystTurn

The below is the basic structure of the json

```json
{
	"from": ["from1", "from2"] / [
		["from1", "from2"],
		["from3", "from4"]
	],
	"to": ["to1", "to2"],
	"optional": null / true / false,
	"turn": null / {
		"type": null / "copy",
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
		"value": null / ["array of strings"] / [["array"], ["of"], ["arrays"]] / 
		{
			"turnJson"
		}
	}
}
```