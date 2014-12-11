package uber.foodtruck.utils;

import org.testng.Assert;
import org.testng.annotations.Test;

import uber.foodtruck.utils.FoodTruckToolsUtils;

public class TestFoodTruckToolsUtils {

	@Test (enabled = false)
	public void testGetWebInfo() {
		String webInfo = FoodTruckToolsUtils.getWebInfo(FoodTruckToolsConstants.TRUCK_LOCATIONS_CALL);
		Assert.assertTrue(webInfo.contains("locationid"));
	}
}
