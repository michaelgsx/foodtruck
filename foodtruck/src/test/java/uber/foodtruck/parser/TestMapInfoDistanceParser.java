package uber.foodtruck.parser;

import org.testng.Assert;
import org.testng.annotations.Test;

import uber.foodtruck.utils.FoodTruckToolsUtils;

public class TestMapInfoDistanceParser {

	@Test
	public void testMapInfoDistanceParser() {
		String webInfo = FoodTruckToolsUtils.readFromFile("src/test/resources/distance_web_data.dat");
		Double distance = MapInfoDistanceParser.parseForDistance(webInfo);
		Assert.assertTrue(Math.abs(distance - 3993) < 2);
		
		webInfo = null;
		distance = MapInfoDistanceParser.parseForDistance(webInfo);
		Assert.assertTrue(Math.abs(distance + 1) < 0.00002);
		
		webInfo = "";
		distance = MapInfoDistanceParser.parseForDistance(webInfo);
		Assert.assertTrue(Math.abs(distance + 1) < 0.00002);
	}
}
