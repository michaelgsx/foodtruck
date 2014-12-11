package uber.foodtruck.definitions;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class FoodTruckFeature extends BaseFeature {

	static {
		registerObjectMapper(FoodTruckFeature.class);
	}
	
	public static FoodTruckFeature create(String jsonStr) {
		return (FoodTruckFeature) create(jsonStr, FoodTruckFeature.class);
	}
	
	@JsonProperty(ID_KEY)
	private final String id;
	
	@JsonProperty(COMPANY_KEY)
	private final String company;
	
	public LongLat getLl() {
		return ll;
	}

	public void setLl(LongLat ll) {
		this.ll = ll;
	}

	public String getId() {
		return id;
	}

	public String getCompany() {
		return company;
	}

	public String getLocation() {
		return location;
	}

	@JsonProperty(LOCATION_KEY)
	private final String location;
	
	@JsonProperty(LL_KEY)
	private LongLat ll;
	
	@JsonCreator
	public FoodTruckFeature(
			@JsonProperty(ID_KEY) String id,
			@JsonProperty(COMPANY_KEY) String company,
			@JsonProperty(LOCATION_KEY) String location, 
			@JsonProperty(LL_KEY) LongLat ll
			){
		this.id = id;
		this.company = company;
		this.location = location;
		this.ll = ll;
	}

	@JsonIgnore
	@Override
	public boolean isValid() {
		return id != null;
	}
}
