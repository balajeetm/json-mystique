Jackson Convertor implements all the methods defined by JsonConvertor interface, inherently using the [Jackson](https://github.com/FasterXML/jackson) library

The Java Object used during Serialisation/ Deserialisation must be a simple POJO which acts as data carriers. The fields of this POJO can be
 * Object Primitives (String, Integer, etc)
 * Any Simple JAVA POJO
 * JsonNode (defined in Jackson) or its extentions (JsonObject, JsonArray, etc)

 Hop over here for the [documentation index](_Sidebar.md)