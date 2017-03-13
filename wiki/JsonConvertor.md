Json Mystique defines a simple interface for simple serialisation and deserialisation of jsons. Its called the convertor interface.

The methods defined by it are as below
 * `<T> T deserialize(String objectString, Class<T> objectType) throws ConvertorException`
   
   De-serialises an input json string into a java POJO defined by the objectType

 * `<T> T deserialize(Object object, Class<T> objectType) throws ConvertorException`
   
   De-serialises an json strinfyable input Java POJO into another java POJO defined by the objectType

 * `<T> T deserialize(InputStream inputStream, Class<T> objectType) throws ConvertorException`
   
   De-serialises an json input stream into a java POJO defined by the objectType

 * `String serialize(Object value) throws ConvertorException`
   
   Serialises an input java pojo into a json string

 * `<T, U> T deserializeGroup(String objectString, Class<T> groupClass, Class<U> elementClass) throws ConvertorException`
   
   De-serialises an input json string into a group (Collection/Map) of java POJOs defined by the objectType. The group is defined by the group class


 * `<T> T deserializeGroup(InputStream inputStream, Class<T> groupClass, Class<?> elementClass)	throws ConvertorException`
   
   De-serialises an json input stream into a group (Collection/Map) of java POJOs defined by the objectType. The group is defined by the group class

 * `<T> List<T> deserializeList(InputStream inputStream, Class<T> elementClass) throws ConvertorException`
   
   Convenience method to de-serialise an json input stream into a List of java POJOs defined by the objectType

 * `<T> List<T> deserializeList(String jsonString, Class<T> elementClass) throws ConvertorException`
   
   Convenience method to de-serialise an json input string into a List of java POJOs defined by the objectType

 * `<T> List<T> deserializeList(Object object, Class<T> elementClass) throws ConvertorException`
   
   Convenience method to de-serialise an json stringifyable Object Collection into a List of java POJOs defined by the objectType

There are two variants / implementations of this JsonConvertor available off the shelf with Json Mystique. They are
 * [GsonConvertor](https://github.com/balajeetm/json-mystique/wiki/GsonConvertor)
 * [JacksonConvertor](https://github.com/balajeetm/json-mystique/wiki/JacksonConvertor)

