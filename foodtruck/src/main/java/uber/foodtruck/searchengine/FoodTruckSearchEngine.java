package uber.foodtruck.searchengine;

import org.json.simple.JSONArray;

public interface FoodTruckSearchEngine {

	public JSONArray findFoodTrucksByLL(String ll, Double distance);

}
