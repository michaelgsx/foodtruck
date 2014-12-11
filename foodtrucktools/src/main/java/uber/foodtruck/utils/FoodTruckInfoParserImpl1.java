package uber.foodtruck.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uber.foodtruck.definitions.FoodTruckFeature;
import uber.foodtruck.definitions.LongLat;

public class FoodTruckInfoParserImpl1 implements FoodTruckInfoParser {
	
	private final static String LOCATION_NAME = "LocationDescription";
	private final static String LONGTITUDE_NAME = "Longitude";
	private final static String LANTITUDE_NAME = "Latitude";

	private final static String REPLACE_QUOTE = "&quot;";
	
	public FoodTruckInfoParserImpl1(){}
	
	public List<FoodTruckFeature> parseWebInfo(String webInfo) {
		List<FoodTruckFeature> foodTruckFeatures = 
				new ArrayList<FoodTruckFeature>();
		if(webInfo == null) {
			return foodTruckFeatures;
		}
		
		webInfo = webInfo.replaceAll(REPLACE_QUOTE, "\"");
		
		String json = "";
		String[] jsonStrings = webInfo.split("blist.viewCache\\['rqzj-sfat'\\] = JSON.parse\\(\\$.unescapeQuotes\\(\"");
		if(jsonStrings != null && jsonStrings.length >= 2) {
			String[] lines = jsonStrings[1].split("blist.viewCache\\['rqzj-sfat'\\]");
			if(lines != null) {
				json = lines[0].substring(0, lines[0].length() - 19);
				System.out.println(json);
			}
		}
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			if(!FoodTruckUtils.isEmpty(json)) {
				jsonObject = (JSONObject) jsonParser.parse(json);
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return foodTruckFeatures;
		}
		
		if(jsonObject == null) {
			return foodTruckFeatures;
		}
		JSONArray jsonColumns = (JSONArray)jsonObject.get("columns");
		if(jsonColumns == null) {
			return foodTruckFeatures;
		}
		
		Map<String, String> lngMap = new HashMap<String, String>();
		Map<String, String> latMap = new HashMap<String, String>();
		Map<String, String> addressMap = new HashMap<String, String>();

		for(int i = 0;i < jsonColumns.size(); i ++) {
			try {
				JSONObject jUnit = (JSONObject) jsonColumns.get(i);
				String name = (String)jUnit.get("name");
				if(LOCATION_NAME.equals(name)) {
					JSONObject jContents = (JSONObject)jUnit.get("cachedContents");
					JSONArray jLocations = (JSONArray)jContents.get("top");
					addressMap = getFoodTruckFeatures(jLocations, "count", "item");
				} else if(LONGTITUDE_NAME.equals(name)) {
					JSONObject jContents = (JSONObject)jUnit.get("cachedContents");
					JSONArray jLocations = (JSONArray)jContents.get("top");
					lngMap = getFoodTruckFeatures(jLocations, "count", "item");
				} else if(LANTITUDE_NAME.equals(name)) {
					JSONObject jContents = (JSONObject)jUnit.get("cachedContents");
					JSONArray jLocations = (JSONArray)jContents.get("top");
					latMap = getFoodTruckFeatures(jLocations, "count", "item");
				}
			} catch(NullPointerException e) {
				e.printStackTrace();
				continue;
			}
		}
		
		for(String key : addressMap.keySet()) {
			if(key != null) {
				try {
					Double longtitude = Double.parseDouble(lngMap.get(key));
					Double lattitude = Double.parseDouble(latMap.get(key));
					String address = addressMap.get(key);
					LongLat longLat = new LongLat(longtitude, lattitude);
					FoodTruckFeature foodTruckFeature = new FoodTruckFeature(key, null, address, longLat);
					foodTruckFeatures.add(foodTruckFeature);
				} catch(NumberFormatException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		
		return foodTruckFeatures;
	}
	
	private static Map<String, String> getFoodTruckFeatures(JSONArray jLocations, String idKey, String contentKey) {
		Map<String, String> contentMap = new HashMap<String, String>();

		if(jLocations == null || idKey == null || contentKey == null) {
			return contentMap;
		}
		
		for(int i = 0;i < jLocations.size();i ++) {
			JSONObject jObject = (JSONObject)jLocations.get(i);
			if(jObject != null) {
				String content = (String)jObject.get(contentKey);
				Long id = (Long)jObject.get(idKey);
				if(content != null && id != null) {
					contentMap.put(String.valueOf(id), content);
				}
			}
		}
		
		return contentMap;
	}
}
