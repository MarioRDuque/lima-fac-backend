package pe.limatambo.util;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class UtilsJSON {

	private static ObjectMapper om = new ObjectMapper();

	public static String objetoToJson(Object obj) {
		try {
			return om.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> jsonToList(Class<T> clase, Object json) {
		try {
			return (List<T>) om.readValue(objetoToJson(json),
					TypeFactory.defaultInstance().constructCollectionType(List.class, clase));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> jsonToMap(String json) {
		try {
			return om.readValue(json, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T jsonToObjeto(Class<T> clase, String json) {
		try {
			return (T) om.readValue(json, clase);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T jsonToObjeto(Class<T> clase, Object json) {
		try {
			return (T) om.readValue(objetoToJson(json), clase);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}