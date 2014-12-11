package uber.foodtruck.utils;

import java.util.List;

import uber.foodtruck.definitions.FoodTruckFeature;

public interface FoodTruckInfoParser {
	public List<FoodTruckFeature> parseWebInfo(String webInfo);
}
