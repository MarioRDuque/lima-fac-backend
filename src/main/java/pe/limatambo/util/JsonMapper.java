package pe.limatambo.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonMapper {
  
 private static final Logger loggerConstantes = LoggerFactory.getLogger(JsonMapper.class);
  
  public static <T extends Object> T parse(String json, Class<T> classObject) {
    ObjectMapper objectMapper = new ObjectMapper();
    T object = null;
    try {
      json = URLDecoder.decode(json, "UTF-8");
      object = objectMapper.readValue(json, classObject);
    } catch (IOException ex) {
      loggerConstantes.error(ex.getMessage());
    }
    return object;
  }
  
  @SuppressWarnings("unchecked")
  public static Map<String, Object> parseToMap(String json) {
    return parse(json, HashMap.class);
  } 
  
  public static <T> T parseList(Object list,TypeReference<?> typeReference){
    ObjectMapper mapper= new ObjectMapper();
    return  mapper.convertValue(list, typeReference);
  }
}
