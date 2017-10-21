The features of deps are as below

* Some transformations require external/domain dependencies
* Some dependecnies are available in the input json and may often be needed for processing some other part of the json
* This attribute lets us populate these dependencies
* The attribute is a Json Array, with each item in an array denoting a [turn](Attribute-Turn.md)
* Thus we basically tranform some parts of the input json but instead of storing this into the output field, we keep it as dependencies to process other parts of the json
* Dependencies are percolated throughout the transformation session. They can be basically seen as global variables available for any turn during the transformation session

## Structure of Dependencies

Before, we look at the structure of the deps in tarot, let's understand the structure of the dependency that will be percolated in the system.

The dependency is a json object with each field referring to a dependency. The field can be any JSON Element (Primitive, Object or Array).

eg. Below we load all the languages we can speak and the colors we like as dependency

```json
{
  "languages": [
    {
      "code": "eng",
      "lang": "English"
    },
    {
      "code": "hin",
      "lang": "Hindi"
    }
  ],
  "colors": [
    "Red",
    "Blue"
  ]
}
```

So the language and colors can be accessed anywhere during the transformation session


## Structure of Deps

* Deps is a Json Array
* Each item within the array is a [tarot/rule](Home.md#the-rulesettarot-structure)
* So the deps, basically defines rules to transform parts of the input json via the Tarots
* These transformed objects are populated in the dependency json and not the output json
* For each Tarot, the rules specified in the deps are executed before executing the tarot

More details of deps can be seen @ [GetFromDeps](MystTurn-GetFromDeps.md)

## How does one read values from deps - @deps notation

Let's say while execution of a tarot, one the source fields needs to be picked from dependencies and not the input json (remember not all dependencies are created from input json, we can insert domain dependencies while invoking the transform method on Json Mystique), how does one do it?

In the above example, let say we want to read the first language. The from for the above should be defined as below :

`"from": ["@deps", "languages", 0]`

Observe that the first string in the fully qualified path denoted by the array of strings is "@deps". This tells mystique to look for the source fields in dependencies and not in the input json

### What if my field is named @deps

Json Mystique looks for "@deps" as the first string in the fully qualified path, anywhere else when it is encountered, it is considered as a field name.
However, if you want to naviagate through a json whose first field is "@deps", json mystique will not be able to retrieve that field.

Though, json mystique tries its best to keep the fully qualified path unambiguous, there are certain areas where we need to sacrifice unambiguity to handle complexity (complex use cases)
