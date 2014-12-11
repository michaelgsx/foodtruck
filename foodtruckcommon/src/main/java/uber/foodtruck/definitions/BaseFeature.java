package uber.foodtruck.definitions;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.DeserializationConfig;

import uber.foodtruck.utils.FoodTruckUtils;

public abstract class BaseFeature implements Constants {

	private final static Logger logger = 
					Logger.getLogger(BaseFeature.class);
	
	public static void initObjectMapper(ObjectMapper objectMapper) {
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setVisibilityChecker(objectMapper.getSerializationConfig()
				.getDefaultVisibilityChecker()
				.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
				.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
				.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		//objectMapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
	}
	
	private final static Map<Class<? extends BaseFeature>, ObjectMapper> objectMapperMap =
			new HashMap<Class<? extends BaseFeature>, ObjectMapper>();
	
	public static void registerObjectMapper(Class<? extends BaseFeature> clazz) {
		if(!objectMapperMap.containsKey(clazz)) {
			ObjectMapper objectMapper = new ObjectMapper();
			initObjectMapper(objectMapper);
			objectMapperMap.put(clazz, objectMapper);
		}
	}
	
	public String toString() {
		String retVal = null;
		try {
			retVal = objectMapperMap.get(this.getClass()).writeValueAsString(this);
		} catch (Exception e) {
			logger.error(e);
		}
		
		return retVal;
	}
	
	public String printPrettyJson() {
		String retVal = null;
		try {
			retVal = objectMapperMap.get(this.getClass()).defaultPrettyPrintingWriter()
					.writeValueAsString(this);
		} catch (Exception e) {
			logger.error(e);
		}
		return retVal;
	}
	
	public static BaseFeature create(String json, Class<? extends BaseFeature> cls) {
		BaseFeature retVal = null;
		
		if(FoodTruckUtils.isEmpty(json)) {
			return null;
		}
		try {
			ObjectMapper objectMapper = objectMapperMap.get(cls);
			if(objectMapper == null) {
				registerObjectMapper(cls);
			}
			retVal = objectMapperMap.get(cls).readValue(json, cls);
		} catch (Exception e) {
			String errMsg = cls.getName() + ".create - Could not create for " + json;
			logger.error(errMsg);
		}
		return retVal;
	}
	
	abstract public boolean isValid();		
			
}
