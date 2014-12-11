package uber.foodtruck.parser;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

import uber.foodtruck.utils.FoodTruckUtils;

public class MapInfoDistanceParser {

	//distance_classification:{local_road_distance_meters:3993.020751953125,highway_distance_meters:0}
	private final static String DISTANCE_CLASSIFICATION = "distance_classification";
	private final static String SEPARATOR = ":";
	private final static Double EPSILON = 0.00000001;
	
	public static Double parseForDistance(String webInfo) {
		if(FoodTruckUtils.isEmpty(webInfo) || !webInfo.contains(DISTANCE_CLASSIFICATION)) {
			return -1.0;
		}
		
		String[] distanceMarkers = webInfo.split(DISTANCE_CLASSIFICATION);
		Double minDis = -1.0;

		if(distanceMarkers.length > 1) {
			for(int i = 1;i < distanceMarkers.length;i ++) {
				try {
					Double curDis = 0.0;

					String[] distances = distanceMarkers[i].split(SEPARATOR, 4);
					if(distances != null && distances.length >= 3) {
						try {
							String[] value = distances[2].split(",");
							if(value != null && value.length > 0) {
								curDis += Double.parseDouble(value[0]);
							}
						} catch (NumberFormatException e) {}
					}
					
					if(distances != null && distances.length >= 4) {
						try {
							String[] value = distances[3].split("}");
							if(value != null && value.length > 0) {
								curDis += Double.parseDouble(value[0]);
							}						
						} catch (NumberFormatException e) {}

					}
					
					if(minDis < 0 || (minDis > curDis && Math.abs(curDis) > EPSILON)) {
						minDis = curDis;
					}
					
				} catch (InputMismatchException e) {
					e.printStackTrace();
				} catch (NoSuchElementException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
		}
		
		return minDis;
	}
}
