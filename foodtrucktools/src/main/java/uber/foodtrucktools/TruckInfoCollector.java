package uber.foodtrucktools;

import java.util.List;

import uber.foodtruck.definitions.FoodTruckFeature;
import uber.foodtruck.definitions.FoodTruckFeatureList;
import uber.foodtruck.utils.FoodTruckInfoParser;
import uber.foodtruck.utils.FoodTruckInfoParserImpl2;
import uber.foodtruck.utils.FoodTruckToolsConstants;
import uber.foodtruck.utils.FoodTruckToolsUtils;

public class TruckInfoCollector {
		
	public static void main(String[] argv) {
		
		if(argv.length < 1) {
			System.out.println("Usage: <outputflie>");
			System.exit(1);
		}
		
		FoodTruckInfoParser foodTruckInfoParser = new FoodTruckInfoParserImpl2();
		
		String jsonInfo = FoodTruckToolsUtils.getWebInfo(FoodTruckToolsConstants.JSON_ENDPOINT);
		System.out.println("Web Service Response: " + jsonInfo);
		List<FoodTruckFeature> features = foodTruckInfoParser.parseWebInfo(jsonInfo);
		FoodTruckFeatureList foodTruckFeatureList = null;
		if(features != null) {
			foodTruckFeatureList = new FoodTruckFeatureList("food_truck_features", features);
			FoodTruckToolsUtils.writeToFile(argv[0], foodTruckFeatureList.printPrettyJson());
			return;
		}
		
		System.err.println("Can not output food truck location info.");
	}
}
