/**
 * @author Santo Grillo and inspiration from: http://technology.finra.org/code/serialize-deserialize-interfaces-in-java.html
 * 
 * 
 */

package util;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class InterfaceAdapter implements JsonSerializer, JsonDeserializer {

	private static final String CLASSNAME = "CLASSNAME";
	private static final String DATA = "DATA";

	@Override
	public Object deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {

		JsonObject jsonObject = arg0.getAsJsonObject();
		JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
		String className = prim.getAsString();
		Class clazz = getObjectClass(className);
		return arg2.deserialize(jsonObject.get(DATA), clazz);
	}

	@Override
	public JsonElement serialize(Object arg0, Type arg1, JsonSerializationContext arg2) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(CLASSNAME, arg0.getClass().getName());
            jsonObject.add(DATA, arg2.serialize(arg0));
            return jsonObject;
        }

	/******
	 * Helper method to get the className of the object to be deserialized
	 *****/
	public Class getObjectClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
			throw new JsonParseException(e.getMessage());
		}
	}
}