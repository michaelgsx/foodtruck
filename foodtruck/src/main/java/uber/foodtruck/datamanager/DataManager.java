package uber.foodtruck.datamanager;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;

import uber.foodtruck.definitions.FoodTruckFeatureList;
import uber.foodtruck.utils.FoodTruckToolsUtils;
import uber.foodtruck.utils.FoodTruckUtils;

public class DataManager {

	private static Logger log = Logger.getLogger(DataManager.class.getName());

	private AtomicReference<FoodTruckFeatureList> foodTruckFeatureRef =
			new AtomicReference<FoodTruckFeatureList>();
	
	private String foodTruckFreatureFileName = null;
	
	public DataManager() {}

	public String getFoodTruckFreatureFileName() {
		return foodTruckFreatureFileName;
	}

	public void setFoodTruckFreatureFileName(String foodTruckFreatureFileName) {
		this.foodTruckFreatureFileName = foodTruckFreatureFileName;
	}

	public String getFoodTruckFeatureListByString() {
		FoodTruckFeatureList foodTruckFeatureList = foodTruckFeatureRef.get();
		if(foodTruckFeatureList != null) {
			return foodTruckFeatureList.printPrettyJson();
		}
		return null;
	}
	
	public FoodTruckFeatureList getFoodTruckFeatureList() {
		return foodTruckFeatureRef.get();
	}

	public void setFoodTruckFeatureList(String content) {
		if(!FoodTruckUtils.isEmpty(content)) {
			FoodTruckFeatureList foodTruckFeatureList = FoodTruckFeatureList.create(content);
			if(foodTruckFeatureList	!= null) {
				foodTruckFeatureRef.set(foodTruckFeatureList);
			}
		}
	}

	public void open() {
		log.info("DataManager.open()");
		if(!FoodTruckUtils.isEmpty(foodTruckFreatureFileName)) {
			String content = FoodTruckToolsUtils.readFromFile(foodTruckFreatureFileName);
			setFoodTruckFeatureList(content);
		}
		log.info("DataManager.opened");

	}
	
	public void close() {		
		log.info("DataManager.close()");
	}
	
}
