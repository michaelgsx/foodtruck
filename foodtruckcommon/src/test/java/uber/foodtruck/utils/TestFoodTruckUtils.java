package uber.foodtruck.utils;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class TestFoodTruckUtils {

	@Test
	public void testIsEmpty() {
		Assert.assertTrue(FoodTruckUtils.isEmpty(null) == true);
		Assert.assertTrue(FoodTruckUtils.isEmpty("") == true);
		Assert.assertTrue(FoodTruckUtils.isEmpty("what") == false);
	}
	
	@Test
	public void testCreateJson() {
		JSONObject json = FoodTruckUtils.createJson(1.1, "myadd", "provider");
		String jsonStr = json.toJSONString();
		Assert.assertTrue(jsonStr.equals("{\"distance\":1.1,\"address\":"
				+ "\"myadd\",\"provider\":\"provider\"}"));
	}
}
