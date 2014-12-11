package uber.foodtruck.parser;

import org.testng.Assert;
import org.testng.annotations.Test;

import uber.foodtruck.definitions.LongLat;
import uber.foodtruck.utils.FoodTruckToolsUtils;

public class TestMapInfoLLParser {
	@Test
	public void testMapInfoDistanceParser() {
		String webInfo = FoodTruckToolsUtils.readFromFile("src/test/resources/ll_web_data.dat");
		LongLat ll = MapInfoLLParser.parseForLL(webInfo);
		Assert.assertTrue(Math.abs(ll.getLat() - 37.7674005) < 0.00002);
		Assert.assertTrue(Math.abs(ll.getLng() + 122.4221696) < 0.00002);

		webInfo = null;
		ll = MapInfoLLParser.parseForLL(webInfo);
		Assert.assertTrue(ll == null);
		
		webInfo = "";
		ll = MapInfoLLParser.parseForLL(webInfo);
		Assert.assertTrue(ll == null);
	}
}
