package uber.foodtruck.definitions;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class LongLat extends BaseFeature {

	static {
		registerObjectMapper(LongLat.class);
	}
	
	public static LongLat create(String jsonStr) {
		return (LongLat) create(jsonStr, LongLat.class);
	}
	
	@JsonProperty(LONG_KEY)
	private Double lng;
	
	@JsonProperty(LAT_KEY)
	private Double lat;
	
	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	@JsonCreator
	public LongLat(
			@JsonProperty(LONG_KEY) Double lng,
			@JsonProperty(LAT_KEY) Double lat
			){
		this.lng = lng;
		this.lat = lat;
	}

	@JsonIgnore
	@Override
	public boolean isValid() {
		return true;
	}
}