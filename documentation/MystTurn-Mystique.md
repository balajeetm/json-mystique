The features of the Mystique MystTurn is as below:

* If the type is specified, the value should be equal to "mystique"
* The Mystique MystTurn can operate on single field only
* The sourceField can be any jsonElement (json object or array)
* There are times when we want to hierarchically call mystique rule files. This turn, let's you operate on a source field and transform it using another mystique ruleset/file
* Mystique MystTurn takes an additional attribute by the name "value"

## Additional attributes for Mystique MystTurn

* value
 * This attribute defines the name of the mystique file/rule that should be used to transform the input source
 * The value is of type string

## Time for a road trip 
Let's jump back to packing items for our road trip

In the previous example we planned to copy few movie dvds so that we can watch them in the tents at the trip. We had many movies of different genres. Their genres were identified by a genre rating on the DVD. We wanted to mark the DVD with the explicit genre when we put it in the backpack. The genre name and genre ratings were however available in a catalog in the wardrobe. There was only one dvd in the previous examples, which wouldn't be the case. Let's presume we have many dvds and we want pick each now and mark the dvds with the genre

**inputJson
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
    "dvd": [
      {
        "name": "Rambo",
        "genre": "act"
      },
      {
        "name": "Hangover",
        "genre": "com"
      },
      {
        "name": "American Pie",
        "genre": "adu"
      }
    ]
  }
}
```


The ruleset file for the above is
**Ruleset**
```json
[{
	"from": ["wardrobe", "dvds", "[*]"],
	"to": ["backpack", "movies"],
	"turn": {
		"type": "mystique",
		"value": "12Custom"
	},
	"deps": [{
		"from": ["wardrobe", "videoGenreCatalog"],
		"to": ["genres"],
		"turn": {
			"type": "arrayToMap",
			"key": ["genreCode"],
			"value": ["descr"]
		}
	}]
}]
```

As you can see, it call another mystique file called "12Custom". Let's define that mystique file
The 12Custom ruleset file is as below
**Ruleset**
```json
[{
	"from": "name",
	"to": "name"
}, {
	"from": "genre",
	"to": "genre",
	"turn": {
		"type": "getFromDeps",
		"key": "genres"
	}
}]
```

**outputJson**
```json
{
  "backpack": {
    "movies": [{
      "name": "Rambo",
      "genre": "Action"
    },
{
      "name": "Hangover",
      "genre": "Comedy"
    },
{
      "name": "American Pie",
      "genre": "Adult"
    }
]
  }
}
```

So we can see the genre is picked appropriately for every dvd

> The same can be found as **mystique12** in the JsonMystique [BDD](../json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Structure of the Mystique MystTurn

The below is the basic structure of the json

```json
{
	"from": ["from1", "from2"],
	"to": ["to1", "to2"],
	"optional": null / true / false,
	"turn": null / {
		"type": null / "mystique",
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
		"value": "mystiqueName"
	}
}
```