The features of **from** are as below

* The attribute "from" can either appear in a tarot/rule json or within the turn json object
* The attribute can refer to fields from
 * input json
 * dependencies (created from input json + domain dependencies inserted while invoking transform)
 * aces (preprocessed data)
* The attribute can refer to single or multiple fields from any combination of above said parts
* The referred fields are passed to the turn, in the order in which they are specified
* Each field, similar to "to", are denoted by a fully qualified path which is an [array of strings](https://github.com/balajeetm/json-mystique/wiki/Attribute-To#why-is-the-fully-qualified-path-an-array-of-strings)
* Multiple fields can be denoted by using an array of fully qualified paths, i.e, an array of array of strings 
  
  For eg  `"from": [ ["personal", "name"], ["personal", "id"] ]`. This picks two fields from the input source json
* Items in an array are denoted by the [array index notation](https://github.com/balajeetm/json-mystique/wiki/Attribute-To#array-index-notation---n)
* If a particular turn needs to be executed for each item of an input source array field, use the [for each](https://github.com/balajeetm/json-mystique/wiki/Attribute-From#for-each-notation) notation

## For Each Notation

The for each notation is used to execute a turn for each item within the input source array field

For eg. Let's get back to the road trip we were preparing for.
Let's assume the wardrobe now has 3 shirts and I would want to pack every shirt in the wardrobe

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
        "color": "green",
        "size": "42"
      }
    ]
  }
}
```

However, we do not care about the sizes for now and we identify each shirt just by its color. So our output json will look like below

**outputJson**
```json
{
  "backpack": {
    "shirts": [
      "red",
      "blue",
      "green"
    ]
  }
}
```

We can use the "for each" notation for this purpose. Since all we want to do is, for each shirt in the wardrobe, pick the shirt with only the color information and place it in the shirts section of the backpack

The ruleset file for the above is
**Ruleset**
```json
[{
	"from": ["wardrobe", "shirt", "[*]"],
	"to": ["backpack", "shirts"],
	"turn": {
		"from": ["color"]
	}
}]
```

> **Note** - You can see the usage of from within turn.
For every shirt picked from the wardrobe, now the shirt json becomes the source element. If we want to granularly pick attributes out of this source json that the turn operates on, we can use the "from" attribute within the turn.
Since we are interested in only the color, we picked only the color

**Observe** - That since the "foreach" operates on every item in an array, the return type of this transformation is an array. Every transformation performed by the turn is put into an array and copied to the destination as specified by the "to" attribute

> The same can be found as **forEach03** in the JsonMystique [BDD](https://github.com/balajeetm/json-mystique/blob/master/src/test/java/com/futuresight/util/mystique/JsonMystiqueBDDTest.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself