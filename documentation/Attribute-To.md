The features of **"to"** are as below

* The attribute "to" can either appear in a tarot/rule json or within the turn json object.
* This attribute refers to a single field in the output json that needs to be populated
* The output field is denoted as a fully qualified path, as an array of json strings

  For eg.
  `"to": ["author", "name"]` refers to the name fields in the below json
  ```json
  {
    "author": {
      "name": "Bala"
    }
  }
  ```
* Items in an array are denoted by the [array index notation](Attribute-To.md#array-index-notation---n)
* If [array index notation](Attribute-To.md#array-index-notation---n) is used to denote a path in the output json, for eg. index 3, and the items do not exist until that item (i.e, the items in the array from index 0 through 2 is non existent), JsonMystique, automatically inserts null items into the array until the indexed item

### Why is the fully qualified path an array of strings/numbers?

As per the json [specification](http://www.json.org/), the field name is mandated to a be string. But the string can contain any character, including special characters. It just needs to be a valid string.

In order to fully quantify a path, we could have any delimiter. For eg. if we chose to use `"."`(dot) in the above example, the name field would be denote as below
`"to": "author.name"`

The looks wonderful. However, if the input json is as below
  ```json
  {
    "author": {
      "name": {
        "first.name": "bala"
      }
    }
  }
  ```

We would refer to the "first name" as below
`"to": "author.first.name"`

The same rule would also denote the "name field" in the below json
```json
{
  "author": {
    "name": {
      "first": {
        "name": "bala"
      }
    }
  }
}
```

Would it be any better if we chose to use a non frequent character like "carot"(^) instead of a frequently used "."(dot)?

**Ofcourse NOT**. We would only be delaying the problem. Since the field name can be any valid string, We decided to use an array of strings to fully quantify a field.

Sounds reasonable. How do we full quantify an item within an array?

### Array Index Notation - "n"
The array index notation is used to denote "n"th element inside an array.
If you want pick the "n"th item in an array, just add in the index "n" in the array that qualify the path. Remember, an array index is a positive integer and not a string 

If you use this notation to denote a path in the output json, for eg. index 3, JsonMystique, automatically inserts null items into the array until the indexed item, if the items do not exist until that item

For eg. For the input json below
  ```json
  {
    "employee": {
      "roles": [
        "developer",
        "scrummaster",
        "architect"
      ]
    }
  }
  ```

The fully quantified path
`["employee", "roles", 2]` denotes the role architect.

### Why is the index of the array specified as a separate string - ["array[n]"] vs ["array", n]

One might wonder, why unlike in programming languages, where the nth element in an array is specified as "array[n]", in json mystique notation, the nth elements requires you to specify the field denoting the array in the separate string and the index as a separate string.

The reason is exactly same as why we chose the array of strings to fully qualify the field. In json the field name is a valid string. It can be any string and can include any special characters. There might be field names with square braces. Just to avoid such conflicts and ensure the identification of a field is always **unambiguous**, it is chosen to specify the index as a separate string. But remember, field names can never be integers. Even if the fieldname is a valid number, it would be a string and not an integer so we have used integer notations to refer to the array index. 

Like they say, simple is better than complex but remember - slightly complex is better than ambiguous