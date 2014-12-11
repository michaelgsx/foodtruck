package uber.foodtruck.utils;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import uber.foodtruck.definitions.FoodTruckFeature;

public class TestFoodTruckInfoParserImpl2 {
	
	@Test (enabled = true)
	public void testParser2() {
		FoodTruckInfoParser foodTruckInfoParser = new FoodTruckInfoParserImpl2();
		String content = FoodTruckToolsUtils.readFromFile("src/test/resources/truckfood_json.txt");
		List<FoodTruckFeature> truckFeatures = foodTruckInfoParser.parseWebInfo(content);
		Assert.assertTrue(truckFeatures.size() == 481);
	}
}
