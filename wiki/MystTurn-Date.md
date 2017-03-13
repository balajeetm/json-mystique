The features of the Date MystTurn is as below:

* If the type is specified, the value should be equal to "dateConvertor"
* The Date MystTurn, transforms the input field presuming its a date
* The Date MystTurn can operate on single field only
* Date MystTurn takes additional attributes such as "action", "informat", "outformat"

## Additional attributes for Date MystTurn

* action
 * This attribute defines the action that needs to be performed on the source field assuming its a date
 * The attribute is a string
 * The attribute can take one of the below values
    * now - If the action is "now", copies the current date to the destination in the required format
    * transform - If the action is "transform", it copies the input source field date to the required format

* informat
 * This attribute defines the format in which the date is specified in the source field
 * This attribute is of type string
 * The attribute can take one of the below values
    * default - If the informat is not specified or is specified as "default", it is presumed the source field is timestamp representation of date either in number or string format
    * long - If the informat is specified as "long", it is presumed the source field is timestamp representation of date in number format
    * string - If the informat is specified as "string", it is presumed the source field is timestamp representation of date in string format
    * pattern - If the informat is any other string, it is presumed to be a [SimpleDateFormat](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html) representation. So the source field is parsed using the [SimpleDateFormat](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html) representation, to determine the actual date

* outformat
 * This attribute defines the format in which the date should be transformed to before copying it to destination
 * This attribute is of type string
 * The attribute can take one of the below values
    * long - If the outformat is specified as "long", the date is transformed to a timestamp and stored as long value
    * string - If the outformat is specified as "string", the date is transformed to a timestamp and stored as string value
    * pattern - If the outformat is any other string, it is presumed to be a [SimpleDateFormat](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html) representation. So the date is parsed using the [SimpleDateFormat](https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html) representation and stored as a string

## Time for a road trip 
Let's jump back to packing items for our road trip

We would finally want to store the time at which we packed the backpack and also store the travel date

**inputJson
We will pick these fields from ace in this example

The ruleset file for the above is
**Ruleset**
```json
[{
	"to": ["backpack", "dates", "packedAt"],
	"turn": {
		"type": "dateConvertor",
		"action": "now",
		"outformat": "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
	}
}, {
	"from": ["@ace(travelDate)"],
	"to": ["backpack", "dates", "travelDate"],
	"turn": {
		"type": "dateConvertor",
		"action": "transform",
		"informat": "yyyy-MM-dd",
		"outformat": "dd-MM-yyyy"
	},
	"aces": {
		"travelDate": "2016-08-20"
	}
}]
```

**outputJson**
```json
{
  "backpack": {
    "dates": {
      "packedAt": "2016-08-23T12:19:04.294+0530",
      "travelDate": "20-08-2016"
    }
  }
}
```

So we can see that we get the appropriate packing and travel dates in the format we require

> The same can be found as **date09** in the JsonMystique [BDD](https://github.com/balajeetm/json-mystique/blob/master/json-mystique-libs/json-mystique/src/test/java/com/balajeetm/mystique/core/JsonMystiquePositiveBDD.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

## Structure of the Date MystTurn

The below is the basic structure of the json

```json
{
	"from": ["from1", "from2"],
	"to": ["to1", "to2"],
	"optional": null / true / false,
	"turn": null / {
		"type": null / "dateConvertor",
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
		"action": "now"/"transform",
		"informat": "default"/"long"/"string"/"<pattern>",
		"outformat": "long"/"string"/"<pattern>"
	}
}
```