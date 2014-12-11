package uber.foodtruck.definitions;

import java.util.List;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class FoodTruckFeatureList extends BaseFeature {

	static {
		registerObjectMapper(FoodTruckFeatureList.class);
	}
	
	public static FoodTruckFeatureList create(String jsonStr) {
		return (FoodTruckFeatureList) create(jsonStr, FoodTruckFeatureList.class);
	}
	
	@JsonProperty(ID_KEY)
	private final String id;
	
	@JsonProperty(LIST_KEY)
	private final List<FoodTruckFeature> foodTruckFeatureList;
	
	public List<FoodTruckFeature> getFoodTruckFeatureList() {
		return foodTruckFeatureList;
	}

	@JsonCreator
	public FoodTruckFeatureList(
			@JsonProperty(ID_KEY) String id,
			@JsonProperty(LIST_KEY) List<FoodTruckFeature> foodTruckFeatureList
			){
		this.id = id;
		this.foodTruckFeatureList = foodTruckFeatureList;
	}

	@JsonIgnore
	@Override
	public boolean isValid() {
		return id != null;
	}
}