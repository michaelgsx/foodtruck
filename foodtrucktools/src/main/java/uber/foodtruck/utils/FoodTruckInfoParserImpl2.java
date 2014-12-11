package uber.foodtruck.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uber.foodtruck.definitions.FoodTruckFeature;
import uber.foodtruck.definitions.LongLat;

public class FoodTruckInfoParserImpl2 implements FoodTruckInfoParser {
	
	private final static Logger logger = Logger
			.getLogger(FoodTruckInfoParserImpl2.class);
	private final static String STATUS_APPROVED = "APPROVED";
	
	public FoodTruckInfoParserImpl2(){}

	public List<FoodTruckFeature> parseWebInfo(String jsonInfo) {
		List<FoodTruckFeature> foodTruckFeatures = 
				new ArrayList<FoodTruckFeature>();
		if(jsonInfo == null) {
			return foodTruckFeatures;
		}
				
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray)parser.parse(jsonInfo);
			if(jsonArray == null) {
				throw new ParseException(0, "input format error");
			}
		} catch(ParseException e) {
			logger.error("FoodTruckInfoParserImpl2.parseWebInfo: input format error"); 
			return foodTruckFeatures;
		}

		for(int i = 0;i < jsonArray.size(); i ++) {
			JSONObject jUnit = null;
			try {
				jUnit = (JSONObject) jsonArray.get(i);
				if(jUnit == null) {
					continue;
				}
				
				String status = (String)jUnit.get("status");
				if(status == null || !STATUS_APPROVED.equals(status.trim().toUpperCase())) {
					continue;
				}
				
				String lat = (String)jUnit.get("latitude");
				String lng = (String)jUnit.get("longitude");
				String address = (String)jUnit.get("locationdescription");
				String company = (String)jUnit.get("applicant");
				try {
					
					Double longtitude = Double.parseDouble(lng);
					Double lattitude = Double.parseDouble(lat);
					LongLat longLat = new LongLat(longtitude, lattitude);
					FoodTruckFeature foodTruckFeature = new FoodTruckFeature(
							String.valueOf(i), company, address, longLat);
					foodTruckFeatures.add(foodTruckFeature);
				} catch(NumberFormatException e) {
					logger.error("FoodTruckInfoParserImpl2.parseWebInfo: " + i + 
							"---" +  e.toString()); 
					logger.error("FoodTruckInfoParserImpl2.parseWebInfo: " + i + 
							"---" +  jUnit.toString()); 
					continue;
				}
			} catch(NullPointerException e) {
				logger.error("FoodTruckInfoParserImpl2.parseWebInfo: " + i + 
						"---" +  e.toString()); 
				logger.error("FoodTruckInfoParserImpl2.parseWebInfo: " + i + 
						"---" +  jUnit.toString()); 
				continue;
			}
			
		}

		return foodTruckFeatures;
	}
}
