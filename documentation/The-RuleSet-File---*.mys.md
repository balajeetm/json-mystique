The rule set file is a json that specifies a list of transformations for the various fields of the input json.
These transformations are called rules/tarots.
Let's take up an example and build the rule set file incrementally.

### Time for a road trip 
So, we are getting ready for a road trip and we are trying to gather items for the road trip.
The input json denotes the items that exists in the house at various places and the output json specifies where these items need to be for the road trip.

Cool, now that the stage is set. Let's begin

For starters, let's understand a simple usage of mystique and then later go deeper down the rabbit hole.

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
[{
	"from": ["wardrobe", "shirt", "color"],
	"to": ["backpack", "shirt", "color"]
}, {
	"from": ["wardrobe", "shirt", "size"],
	"to": ["backpack", "shirt", "size"]
}, {
	"from": ["wardrobe", "pant", "color"],
	"to": ["backpack", "trouser", "color"]
}, {
	"from": ["wardrobe", "pant", "size"],
	"to": ["backpack", "trouser", "length"]
}, {
	"from": ["wardrobe", "towel", "color"],
	"to": ["backpack", "handtowel", "color"]
}]
```

As you can see above, the fully qualified path to the json element, is represented as an array of strings/numbers. For simplicity, you can use a `'.'` seperated string to denote the path. FOr eg, the above rule set can also be written as below:<br>
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

Neat huh?<br>
> NOTE: If the field name by itself has `'.'`(dots) in them, use the ArrayNotation.<br>
For simplicity, henceforth, we will only use the string notation.

So we see the ruleset is a simple array of transformations, specifying where to pick the field **"from"** and where to paste the corresponding field **"to"**. In this case, we have done nothing but a mere copy of fields, so, no other metadata is needed

## The RuleSet/Tarot Structure

So far, we saw a simple example where the values from input json were blindly copied to the output.
Though this is the primary usecase, there are cases wherein we want to transform the input differently.
This transformation of one or more input fields to an output field is called a **turn**.
Ruleset file as we know, is an array of rules/tarots. So far, we only saw the "from" and "to" fields of the tarot. There are other attributes that you can use to get more granular control on the turn. Let's go through them

* [**to**](../Attribute-To) 
  * A fully qualified path that denotes the field in the output json that needs to be populated
  * Will refer to only one field in the output json
  * Is an array of strings
  * Items in an array are denoted by the [array index notation](../Attribute-To#array-index-notation---n)
  * eg `"to": ["backpack", "shirt", 0]`

Click [here](../Attribute-To) for more details on **to**

* [**from**](../Attribute-From)
  * Denotes one or more fields in the input json that needs to be transformed to an output
  * Uses a fully qualified path to denote a field
  * When referring to a single field, is an array of strings.
    For eg `"from": ["wardrobe", "shirt"]`
  * When referring to multiple fields is an array of array of strings
    For eg `
    "from": [
		["personal", "name"],
		["personal", "id"]
	]`

Click [here](../Attribute-From) for more details on **from**

* [**turn**](../Attribute-Turn) 
  * Denotes the rule/tarot to apply and thereby transform the input field/fields to output
  * Is a json object, with attributes defining the type of turn and other specifications defining the tarot for the transformation
  * In most cases, the json structure of a turn is similar to that of a tarot "rule" with additional fields specific to the type of transformation applied

Click [here](../Attribute-Turn) for more details on **turn**

* [**optional**](../Attribute-Optional) 
  * Not all transformations will return a non null value
  * Denotes if a transformed null value, should be copied to the output
  * Is a boolean, defaulted to false
  * If optional is true and the transformed value is null, the value is not copied to the output

Click [here](../Attribute-Optional) for more details on **optional**

* [**deps**](../Attribute-Deps) 
  * Some transformations require external/domain dependencies
  * Some dependencies are available in the input json and can thus be processed before starting the transformation
  * The attribute denotes the dependencies that may be required during transformation
  * Is a Json Array
  * The json structure of the object within the array is exactly the same as the turn. Basically, we can define a list of turns to populate the dependencies
  * Is available globally throughtout the transformation session

Click [here](../Attribute-Deps) for more details on **deps**

* [**aces**](../Attribute-Aces) 
  * Sometimes, we may need to preprocess part of the input source before executing the transform
  * Is a json object
  * The json object contains a number of fields, where each field is a [**turn**](../Attribute-Turn)
  * Json Mystique parses the ace and executes these turns and updates the ace object with the output of the turn
  * This transformed output can now be used to during the execution of the tarot

Click [here](../Attribute-Aces) for more details on **aces**

Let's try to understand **[Turns](../The-Turns)** further.