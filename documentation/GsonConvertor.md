Gson Convertor implements all the methods defined by JsonConvertor interface, inherently using the [Gson](https://github.com/google/gson) library

The Java Object used during Serialisation/ Deserialisation must be a simple POJO which acts as data carriers. The fields of this POJO can be
 * Object Primitives (String, Integer, etc)
 * Any Simple JAVA POJO
 * JsonElement (defined in Gson) or its extentions (JsonObject, JsonArray, etc)

 Hop over here for the [documentation index](_Sidebar.md)