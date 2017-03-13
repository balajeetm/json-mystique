The features of aces are as below

This transformed output can now be used to during the execution of the tarot

* Aces define the part of the json that needs to be pre-processed for later use
* Is a json Object with each field being a [turn](https://github.com/balajeetm/json-mystique/wiki/Attribute-Turn)
* Json Mystique parses the ace and executes these turns and updates the ace object with the transformed object returned from the turn

### Order of Execution of Turns within Tarot
* First, the turns for the dependencies are executed and the global dependency object is updated
* Next, the turns within the aces are executed and the aces Json Object is updated with appropriate transformed values
* Lastly, the turns specified within the Tarot are executed

## Difference between Ace and Dependency

Its common to question why we need aces when deps can also pre-process part of the input json.
The answer to that is quite simple.

Dependency is a global object available throughout the transformation session. The lifetime of a dependency, once populated is until the complete transformation is complete, i.e, until all the tarots are executed and the output json populated.

Aces however can be defined as a local dependency whose lifetime is only until the execution of the Tarot where the ace is defined

Why would this be needed?

## @ace notation - How do you retrieve a field from aces

Let's say while execution of a tarot, one the source fields needs to be picked from aces and not the input json (remember not all dependencies are created from input json, we can insert domain dependencies while invoking the transform method on Json Mystique) or dependencies, how does one do it?

Let's get back to the road trip example.

The wardrobe consists of spectacles and spectacle cases as below. As we can see in the array of spectacles there is only one spectacle

**inputJson**

```json
{
  "wardrobe": {
    "spectacleCase": {
      "name": "leatherCase",
      "size": "large"
    },
    "spectacles": [
      {
        "type": "frameless",
        "lens": "bifocal"
      }
    ]
  }
}
```

Now I want to pick the readingGlass called as spectacles in the input and place it in the leather case as below:

**outputJson**

```json
{
  "backpack": {
    "leatherCase": {
      "type": "frameless",
      "lens": "bifocal"
    }
  }
}
```

So we have squeezed our items in the backpack for lack of space. So we need to pick the spectacles and place it in the leather case, the rule set would be as below. Let's use ace to do this. Let's preprocess the spectacles and keep the reading glasses and later place it into the leather case.

So the rule set file will be as below

**ruleset**

```json
[{
	"from": ["@ace(readingGlasses)", "[0]"],
	"to": ["backpack", "leatherCase"],
	"aces" : {
		"readingGlasses": {
			"from": ["wardrobe", "spectacles"]
		}
	}
}]
```

**Note**
We have preprocessed the spectacles in the input and stored it in a temporary ace variable called "readingGlasses". We have defined a turn for that which is straight forward. This will pick the entire array with one spectacle in it

Observe that we have used the **"@ace"** notation here. This tells mystique to pick the source field from the ace rather than input json or dependencies. But this ace is an array. So, after picking the source field, we go further granular and pick the first item from the array.

### What if my field is named @ace

Json Mystique looks for "@ace" as the first string in the fully qualified path, anywhere else when it is encountered, it is considered as a field name.
However, if you want to naviagate through a json whose first field is "@ace", json mystique will not be able to retrieve that field.

Though, json mystique tries its best to keep the fully qualified path unambiguous, there are certain areas where we need to sacrifice unambiguity to handle complexity (complex use cases)


## @value notation - What if the value of the output field is not static but dependent on a field in the input json

Observe here in the previous example, we had hard coded the case name to "leatherCase". This is wrong. We actually want to place it into the leatherCase because the spectacleCase name is leatherCase. Which means the name of a field in the output is dependent on the input field which is spectacle case. This is where the **"@value"** notation will come in handy

Let's modify the rule file as below

**ruleset**

```json
[{
	"from": ["@ace(readingGlasses)", "[0]"],
	"to": ["backpack", "@value(case, name)"],
	"aces" : {
		"readingGlasses": {
			"from": ["wardrobe", "spectacles"]
		},
		"case": {
			"from": ["wardrobe", "spectacleCase"]
		}
	}
}]
```

If you observe what is done, we created another ace which kept a local copy of the spectacleCase and stored it in a variable called case.

Then, in our fully qualified path that denotes the destination field ("to"), we used the @value notation.

The @value notation tells JsonMystique to parse the ace. It first the splits the string within the brackets of "@value" based on character ','. Each string now qualifies the fields within the ace. In this case, it looks for a variable named "case" within aces and from the "case" field, gets the attribute by name "name".

## Difference between @ace and @value

The "@ace" notation tells JsonMystique to look for a source field in aces. It basically just tells JsonMystique where to pick the source from. So
* @deps, means look for the source in dependencies
* @ace, means look for the source in aces
* if none specified, look in the input json

The "@value" notation however makes JsonMystique to pick the field from aces, parse it and get the fieldValue which can then be used to specify a string in the fully qualified path of a field.

As previously said, "@value" is generally used when a fieldname in the output is dependent on the input
