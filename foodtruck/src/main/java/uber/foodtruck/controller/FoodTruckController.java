package uber.foodtruck.controller;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uber.foodtruck.datamanager.DataManager;
import uber.foodtruck.searchengine.FoodTruckSearchEngine;
import uber.foodtruck.searchengine.FoodTruckSearchEngineImpl;

@Controller
public class FoodTruckController {

	private static Logger log = Logger.getLogger(FoodTruckController.class
			.getName());

	private DataManager dataManager;

	public DataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	public void open() {
		log.info("FoodTruckController.open()");
	}

	public void close() {
		log.info("FoodTruckController.close()");
	}

	@RequestMapping(value = "/requestbyll/{ll}/{distanceStr}", method = RequestMethod.GET)
	public @ResponseBody String findFoodTrucksByLL(
			@PathVariable String ll, @PathVariable String distanceStr) {
		log.info("FoodTruckController.findFoodTrucksByLL start.");
		long startTimeStemp = System.currentTimeMillis();

		FoodTruckSearchEngine searchEngine = new FoodTruckSearchEngineImpl(
				dataManager);
		
		Double distance = null;
		try {
			distance = Double.parseDouble(distanceStr);
		} catch(NumberFormatException e) {
			log.error("FoodTruckController.findFoodTrucksByLL input distance should be double.");
			return null;
		}
		JSONArray results = searchEngine.findFoodTrucksByLL(ll, distance);

		long endTimeStemp = System.currentTimeMillis();
		log.info("FoodTruckController.findFoodTrucksByLL total processing time "
				+ (endTimeStemp - startTimeStemp) + "ms");

		if(results == null) {
			return null;
		} else {
			return results.toJSONString();
		}	
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public @ResponseBody String welcome() {

		log.info("welcome hitted. ");
		return "welcome to use foodtruck webapp.";
	}
}
