package uber.foodtruck.definitions;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class TestFoodTruckFeatureList {

	@Test
	public void testFoodTruckFeatureList() {
		LongLat ll = new LongLat(1.1, 2.2);
		FoodTruckFeature foodTruckFeatue = new FoodTruckFeature("id", "company", "address",
				ll);
		
		List<FoodTruckFeature> list = new ArrayList<FoodTruckFeature>();
		list.add(foodTruckFeatue);
		
		FoodTruckFeatureList foodTruckList = new FoodTruckFeatureList("id", list);
		
		Assert.assertTrue(foodTruckList.toString().contains("company"));
	}
}
