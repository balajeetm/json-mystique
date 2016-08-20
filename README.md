[![Build Status](https://travis-ci.org/balajeetm/json-mystique.svg?branch=master)](https://travis-ci.org/balajeetm/json-mystique)

# Json Mystique
Utility for Json Conversion and json transformation in Java

Json mystique is json transformation library written in and for Java. The library takes an input json as a string or jsonElement and transforms the same to a json string or JsonElement.
The transformation is performed via a ruleSet file that specifies the mappings in a json format

## Features and Primary Use Cases
* The library is useful for transforming from one json to another, even when the input and output jsons are non identical
* Multiple fields from the input can be operated on to map to a single field in the output
* Custom convertors can be easily plugged to handle custom domain logic

## Dependencies
* The library uses maven as its build management
* The library primarily depends on 
    * Spring-Context for Spring based wiring and 
    * [Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson) for json String to Java pojo transformations

The actual versions can be found in the pom file
