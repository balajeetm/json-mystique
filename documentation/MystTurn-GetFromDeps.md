The features of the GetFromDeps MystTurn is as below:

* If the type is specified, the value should be equal to "getFromDeps"
* The GetFromDeps MystTurn can operate on single field only
* This mystique considers the source field as a reference to some field in deps, i.e, the mystique considers the source field to be the name of some field in deps
* Thus, the mystique considers the source field to be a string
* The mystique gets the value of the field or a subset of values of the field referenced by the source field
* GetFromDeps MystTurn takes additional attributes such as "key" and "value"

## Additional attributes for GetFromDeps MystTurn

* key
 * As we had previously stated, "dependencies" is json object that is percolated through the transformation session
 * This attribute is a fully qualified path that denotes a field in dependencies
 * Thus "key" is an array of strings
 * The field that this key points to **should** be a json object

* value
 * Once the key identifies a field in the deps, json mystique retrieves the attribute of this field whose name is equal to the source field. Let us call this "identified field"
 * This attribute defines what attributes from "identified field" needs to be copied to the destination
 * The value one of the below types
    * Array of Strings - In which case, the field denoted by this fully qualified path is retrieved from the "identified field"
    * Array of Array of Strings - In which case, every (array of strings) denotes a fully qualified path and json mystiques retrieves these fields and places them into a json object and returns the json object
    * Json object - In this case, it refers to a turn and transforms the "identified field" as per the turn specification
 * If the value is null, Json Mystique returns the "identified field" as is

## Time for a road trip 
Let's jump back to packing items for our road trip

We plan to copy few movie dvds so that we can watch them in the tents at the trip. We have many movies of different genres. Their genres are identified by a genre rating on the DVD. We want to mark the DVD with the explicit genre when we put it in the backpack. The genre name and genre ratings are available in a catalog in the wardrobe. So the input json is as below

**inputJson
```json
{
  "wardrobe": {
    "videoGenreCatalog": {
      "act": "Action",
      "com": "Comedy",
      "adu": "Adult"
    },
    "dvd": {
      "name": "Rambo",
      "genre": "act"
    }
  }
}
```


The ruleset file for the above is
**Ruleset**
```json
[
  {
    "from": "wardrobe.dvd.name",
    "to": "backpack.movie.name"
  },
  {
    "from": "wardrobe.dvd.genre",
    "to": "backpack.movie.genre",
    "turn": {
      "type": "getFromDeps",
      "key": "genres"
    },
    "deps": [
      {
        "from": "wardrobe.videoGenreCatalog",
        "to": "genres"
      }
    ]
  }
]
```

**outputJson**
```json
{
  "backpack": {
    "movie": {
      "name": "Rambo",
      "genre": "Action"
    }
  }
}
```

So we can see the genre is picked appropriately

> The same can be found as **getFromDeps10** in the JsonMystique [BDD](../json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Structure of the GetFromDeps MystTurn

The below is the basic structure of the json

```json
{
	"from": ["from1", "from2"],
	"to": ["to1", "to2"],
	"optional": null / true / false,
	"turn": null / {
		"type": null / "getFromDeps",
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
		"key": ["depsKey"],
		"value": null / ["from7", "from8"] / [
			["from7", "from8"],
			["from9", "from10"]
		] / {
			"type": "turnType"
		}
	}
}
```