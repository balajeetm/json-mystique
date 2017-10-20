The features of "turn" are as below

* The turn specifies the transformation required to be carried out to turn or transform the input field/fields, before they can be copied to the destination.
* So far, we only looked at simple copy of fields from the input. Sometimes, they need to be transformed. 

  For eg. Two input fields may need to be concatenated. Or a string may need to be transformed to integer or vice versa or some domain logic may need to be applied

  The turn specifies this transformation
* The turn is a json object. The structure of the json object is defined below
* If a turn object is not specified, it is presumed to be a turn of type ["copy"]("TODO")
* The primary responsibility of the turn is to transform the input and produce a transformed object

## The Turn Structure

Turn can contain any number of fields as the need demands. But few fields will exist in every turn. They are as below

* type
 * Is a string that defines the type of the turn and thus practically, the transformation logic that needs to be applied
 * Each variation of this type has been detailed in the [Turns]("TODO") section
 * If the type is not specified, it is assumed to be a turn of type ["copy"]("TODO")

* from
 * As explained before, this is to granularly choose a field from the input json
 * Is a fully qualified path denoted by an array of strings
 * More details can be found [here](https://github.com/balajeetm/json-mystique/wiki/Attribute-From)

  For eg. Let's assume the input json is as below

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
  and the from defined is `"from":["wardrobe", "shirt"]`
  This will yield the the json

  ```json
  {
    "color": "red",
    "size": "40"
  }
  ```

  So if the "from" of the turn is defined as `"from": ["color"]`
  It will pick "red"
  This is for granular filtering.

  ##### Note
  If multiple fields are specified in the from of "rule". Eg. `"from": "[["wardrobe", "shirt"], ["wardrobe", "pant"]]"`
  Specifying a granular "from" in turn, will look for the field inside every field chosen in the by the rule.
  That is, keeping the same eg. It will choose values "red" & "blue" respectively

* to
 * Though the primarly responsibility of the turn is to produce a transformed object, the turn can also put the object in the prescribed destination. This is done via the "to" attribute that defines the destination
 * Is a fully qualified path denoted by an array of strings
 * More details can be found [here](https://github.com/balajeetm/json-mystique/wiki/Attribute-To)

* optional
 * This field bears the same meaning as the optional in the tarot/rule
 * More details can be found [here](https://github.com/balajeetm/json-mystique/wiki/Attribute-Optional)

* default
 * As already said, not all transformations will return a non null value
 * Sometimes, one would wish to default a value when null value is produced in the transformation
 * This "default" attribute specifies a default
 * This is a json object
 * The structure of default is as below

### The structure of Default

Default is a json object that can contain the below attributes
* value
 * The value attribute can be any any json element - a primitive (string, number, boolean), json object or a json array
 * If the value attribute is specified in the default, the value of the default is promptly copied to the destination if the result of the transformation is a null value
 
  eg.
  Let's get back to packing shirts from wardrobe. We saw the shirts had attributes "color" and "size". However, white shirts do not have color specified but when we pack it into the backpack we want to default the color to white if none is specified
    
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
          "size": "40"
        }
      ]
    }
  }
  ```
  
  **outputJson**
  ```json
  {
    "backpack": {
      "shirts": [
        "red",
        "white"
      ]
    }
  }
  ```
  
  The ruleset file for the above is
  **Ruleset**
  ```json
  [{
  	"from": ["wardrobe", "shirt", "[*]"],
  	"to": ["backpack", "shirts"],
  	"turn": {
  		"from": ["color"],
  		"default": {
  			"value": "white"
  		}
  	}
  }]
  ```

* turn
 * This field is considered only if the value attribute is not specified
 * This is a json object that specifies a turn
 * Thus a turn, can internally call another turn in case a null value is returned

  eg.
  While packing items for the road trip, we want to carry our reading glasses. So we check the wardrobe to see if the spare glasses are present, if not, we pack the reading glasses. So the input and output json for this would be

  **inputJson**
  ```json
  {
    "wardrobe": {
      "readingGlass": {
        "type": "frameless",
        "lens": "bifocal"
      }
    }
  }
  ```
  
  **outputJson**
  ```json
  {
    "backpack": {
      "glass": {
        "type": "frameless",
        "lens": "bifocal"
      }
    }
  }
  ```
  
  The ruleset file for the above is
  **Ruleset**
  ```json
  [{
 	"from": ["wardrobe"],
 	"to": ["backpack", "glass"],
 	"turn": {
 		"from": ["spareGlasses"],
 		"default": {
 			"turn": {
 				"from": ["readingGlass"]
 			}
 		}
 	}
 }]
  ```

  > The same can be found as **default04** in the JsonMystique [BDD](https://github.com/balajeetm/json-mystique/blob/master/src/test/java/com/futuresight/util/mystique/JsonMystiqueBDDTest.java) (Behavior Driven Development) Unit test. Please checkout the codebase and run the BDD as a JUNIT test to see for yourself

Apart from the above, there can be many other attributes, each attributes defined depends on the type of turn used.
For more details on turn refer [Turn]("TODO")