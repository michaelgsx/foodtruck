package uber.foodtruck.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uber.foodtruck.controller.FoodTruckConstants;
import uber.foodtruck.definitions.LongLat;
import uber.foodtruck.utils.FoodTruckUtils;

public class MapInfoLLParser {
	
	public static LongLat parseForLL(String webInfo) {
		JSONParser jsonParser = new JSONParser();
		JSONObject response = null;
		
		if(FoodTruckUtils.isEmpty(webInfo)) {
			return null;
		}
		
		try {
			response = (JSONObject)jsonParser.parse(webInfo);
		} catch(ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		if(response != null) {
			String status = (String) response.get(FoodTruckConstants.LL_QUERY_STATUS);
			if(!FoodTruckConstants.LL_QUERY_OK.equals(status)) {
				return null;
			}
			
			try {
				JSONArray results = (JSONArray)response.get(FoodTruckConstants.LL_QUERY_RESULTS);
				JSONObject geometry = (JSONObject)((JSONObject)results.get(0)).get(FoodTruckConstants.LL_QUERY_GEOMETRY);
				JSONObject location = (JSONObject) geometry.get(FoodTruckConstants.LL_QUERY_LOCATION);
				Double lng = (Double) location.get(FoodTruckConstants.LL_QUERY_LONG);
				Double lat = (Double) location.get(FoodTruckConstants.LL_QUERY_LAT);
				return new LongLat(lng, lat);
			} catch(NullPointerException e) {
				e.printStackTrace();
				return null;
			}
		} 
		return null;
	}
}
