JsonLever is an utility packaged with JsonMystique to easily operate on jsons

## Usage Guide

### Step 1
As previously stated, Json Mystique uses spring for dependency injection and wiring.
Json Mystique objects thus should be added to the component scan path so that they can be boot loaded
So if you are using Spring already, add the below Json Mystique config to the class path as below

`@Import(value={JsonMystiqueConfig.class})`

If you are using xml as a spring configuration file, add the below

`<context:component-scan base-package="com.futuresight.util.mystique" />`

### Step 2
Autowire the JsonMystique class into the class where you wish to perform the json transform. This can be done as below

`@Autowired`
`private JsonLever jsonLever;`

Or if your class had access to application context

`JsonLever jsonLever = context.getBean(JsonLever.class);`

## Utilities

Every method is an utility in this class.
Please refer the [class](https://github.com/balajeetm/json-mystique/blob/master/src/main/java/com/futuresight/util/mystique/JsonLever.java) for more details




