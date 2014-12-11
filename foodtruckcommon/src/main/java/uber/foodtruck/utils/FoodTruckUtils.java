package uber.foodtruck.utils;

import org.json.simple.JSONObject;

public class FoodTruckUtils {
	
	public static boolean isEmpty(String input) {
		if(input == null || input.length() == 0) {
			return true;
		}
		return false;
	}
	
	
	//	dlon = lon2 - lon1 
	//	dlat = lat2 - lat1 
	//	a = (sin(dlat/2))^2 + cos(lat1) * cos(lat2) * (sin(dlon/2))^2 
	//	c = 2 * atan2( sqrt(a), sqrt(1-a) ) 
	//	d = R * c (where R is the radius of the Earth)
	private final static double R = (6378.1370 + 6356.7523) / 2.0;
	
	public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
		double dlon = lng2 - lng1;
		double dlat = lat2 - lat1;
		double a = Math.pow(Math.sin(dlat/2), 2) + 
					Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2), 2);
		double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) );
		return R * c;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject createJson(Double distance, String address, String provider) {
		JSONObject json = new JSONObject();
		json.put("distance", distance);
		json.put("address", address);
		json.put("provider", provider);
		return json;
	}

}
