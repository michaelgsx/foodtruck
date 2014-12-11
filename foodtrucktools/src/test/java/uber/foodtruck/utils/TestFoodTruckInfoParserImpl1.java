package uber.foodtruck.utils;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import uber.foodtruck.definitions.FoodTruckFeature;

public class TestFoodTruckInfoParserImpl1 {

	@Test (enabled = true)
	public void testParser1() {
		FoodTruckInfoParser foodTruckInfoParser = new FoodTruckInfoParserImpl1();
		String content = FoodTruckToolsUtils.readFromFile("src/test/resources/truckfood.txt");
		List<FoodTruckFeature> truckFeatures = foodTruckInfoParser.parseWebInfo(content);
		Assert.assertTrue(truckFeatures.size() == 20);
	}

}
