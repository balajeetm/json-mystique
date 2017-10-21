The features of the StringUtils MystTurn is as below:

* If the type is specified, the value should be equal to "stringUtils"
* The MystTurn, transforms the input field presuming its a string
* The MystTurn can operate on single field only
* StringUtils MystTurn takes additional attributes such as "action" and "separator"

## Additional attributes for StringUtils MystTurn

* action
 * This attribute defines the action that needs to be performed on the source field assuming its a string
 * The attribute is a string
 * The attribute can take one of the below values
    * trim - If the action is "trim", it removes whitespace characters from both ends of the String, handling null by returning null
    * trimToEmpty - If the action is "trimToEmpty", it removes whitespace characters from both ends of the String, handling null by returning an empty string
    * substringAfterLast - If the action is "substringAfterLast", it gets the substring after the last occurrence of a separator. When this action is used, it looks for another attribute called separator. If separator is not provided, it uses an empty string

## Time for a road trip 
Let's jump back to packing items for our road trip

In the previous example we planned to copy few movie dvds so that we can watch them in the tents at the trip. We had many movies of different genres. Their genres were identified by a genre rating on the DVD. We wanted to mark the DVD with the explicit genre when we put it in the backpack. The genre name and genre ratings were however available in a catalog in the wardrobe. There were many dvds and we picked each one and marked the dvds with the genre. Now let's presume the names of the movies were described as "genreCode/MovieName" but when we mark the DVDs we want the movie name to be displayed without prepending genreCode. We will use StringUtils for this

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
        "name": "act/Rambo",
        "genre": "act"
      },
      {
        "name": "com/Hangover",
        "genre": "com"
      },
      {
        "name": "adu/American Pie",
        "genre": "adu"
      }
    ]
  }
}
```


The ruleset file for the above is
**Ruleset**
```json
[
  {
    "from": "wardrobe.dvds.[*]",
    "to": "backpack.movies",
    "turn": {
      "type": "mystique",
      "value": "13Custom"
    },
    "deps": [
      {
        "from": "wardrobe.videoGenreCatalog",
        "to": "genres",
        "turn": {
          "type": "arrayToMap",
          "key": "genreCode",
          "value": "descr"
        }
      }
    ]
  }
]
```

As you can see, it call another mystique file called "12Custom". Let's define that mystique file
The 13Custom ruleset file is as below
**Ruleset**
```json
[
  {
    "from": "name",
    "to": "name",
    "turn": {
      "type": "stringUtils",
      "action": "substringAfterLast",
      "separator": "/"
    }
  },
  {
    "from": "genre",
    "to": "genre",
    "turn": {
      "type": "getFromDeps",
      "key": "genres"
    }
  }
]
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

> The same can be found as **stringUtils13** in the JsonMystique [BDD](../json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Structure of the StringUtils MystTurn

The below is the basic structure of the json

```json
{
	"from": ["from1", "from2"],
	"to": ["to1", "to2"],
	"optional": null / true / false,
	"turn": null / {
		"type": null / "stringUtils",
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
		"action": "trim"/"trimToEmpty"/"substringAfterLast",
		"separator": "sep"
	}
}
```

Hop over here for the [documentation index](_Sidebar.md)