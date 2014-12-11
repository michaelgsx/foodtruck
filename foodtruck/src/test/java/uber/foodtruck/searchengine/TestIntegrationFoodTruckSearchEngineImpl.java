package uber.foodtruck.searchengine;

import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import uber.foodtruck.datamanager.DataManager;

public class TestIntegrationFoodTruckSearchEngineImpl {

	private DataManager dataManager;
	private FoodTruckSearchEngine foodTruckSearchEngine;

	@BeforeMethod
	public void init() {
		dataManager = new DataManager();
		dataManager
				.setFoodTruckFreatureFileName("src/test/resources/foodtruck_data.dat");
		dataManager.open();
		foodTruckSearchEngine = new FoodTruckSearchEngineImpl(dataManager);
	}

	@Test(enabled = true)
	public void testFindFoodTrucksByLL() {
		JSONArray results = foodTruckSearchEngine.findFoodTrucksByLL(
				"37.7685432890242,-122.408492892439", 1000.0);
		Assert.assertEquals(results.size(), 6);

		results = foodTruckSearchEngine.findFoodTrucksByLL(null, null);
		Assert.assertTrue(results == null);

		results = foodTruckSearchEngine.findFoodTrucksByLL("", 1000.0);
		Assert.assertTrue(results == null);
	}

}
