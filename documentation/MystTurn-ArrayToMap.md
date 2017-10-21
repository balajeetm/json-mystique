The features of the ArrayToMap MystTurn is as below:

* If the type is specified, the value should be equal to "arrayToMap"
* The ArrayToMap MystTurn can operate on single field only
* This mystique considers the source field to be a **json array** only
* The mystique iterates over each item in the array and for each item, creates a field in the json object whose fieldName is is the value of some field within the array item
* Since a json object is nothing more than a map where the key is always a string, the arrayToMap mystTurn practically converts an array to map
* ArrayToMap MystTurn takes additional attributes such as "key" and "value"

## Additional attributes for ArrayToMap MystTurn

* key
 * While processing every item within the source array, this attribute identifies the field within the item, which should be used as the fieldName for a field to be created in the output json
 * This attribute is a fully qualified path that denotes a field in the array item
 * Thus "key" is an array of strings or a `'.'`(dot) separated string representing the fully qualified path
 * The field that this key points to **should** be a string since this will be the be used as a fieldName

* value
 * Once the key identifies a field in the array item, json mystique creates a field in the output json based on the key. The value of this field created is controlled by the value field.
 * This attribute defines what attributes from array item needs to be copied to the destination
 * The value one of the below types
    * Array of Strings - In which case, the field denoted by this fully qualified path is retrieved from the array item
    * Array of Array of Strings - In which case, every (array of strings) denotes a fully qualified path and json mystique retrieves these fields and places them into a json object and returns the json object
    * Json object - In this case, it refers to a turn and transforms the array item as per the turn specification
 * If the value is null, Json Mystique returns the array item as is

## Time for a road trip 
Let's jump back to packing items for our road trip

In the previous example we planned to copy few movie dvds so that we can watch them in the tents at the trip. We had many movies of different genres. Their genres were identified by a genre rating on the DVD. We wanted to mark the DVD with the explicit genre when we put it in the backpack. The genre name and genre ratings were however available in a catalog in the wardrobe. But the genre rating will not available as a json like before. It is now available as an array of genre items where each item defines a code and description as seen below. We would need to construct a jsonObject from this array. Let's use arrayToMap for this

**inputJson**
```json
{
  "wardrobe": {
    "videoGenreCatalog": [
      {
        "genreCode": "act",
        "descr": "Action"
      },
      {
        "genreCode": "com",
        "descr": "Comedy"
      },
      {
        "genreCode": "adu",
        "descr": "Adult"
      }
    ],
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
        "to": "genres",
        "type": "arrayToMap",
        "key": "genreCode",
        "value": "descr"
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

> The same can be found as **arrayToMap11** in the JsonMystique [BDD](../json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Structure of the ArrayToMap MystTurn

The below is the basic structure of the json

```json
{
	"from": ["from1", "from2"],
	"to": ["to1", "to2"],
	"optional": null / true / false,
	"turn": null / {
		"type": null / "arrayToMap",
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
		"key": ["fr1", "fr2"],
		"value": null / ["from7", "from8"] / [
			["from7", "from8"],
			["from9", "from10"]
		] / {
			"type": "turnType"
		}
	}
}
```

Hop over here for the [documentation index](_Sidebar.md)