package uber.foodtruck.searchengine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import uber.foodtruck.datamanager.DataManager;
import uber.foodtruck.definitions.FoodTruckFeature;
import uber.foodtruck.definitions.FoodTruckFeatureList;
import uber.foodtruck.definitions.LongLat;
import uber.foodtruck.utils.FoodTruckUtils;

public class FoodTruckSearchEngineImpl implements FoodTruckSearchEngine {
	private final static Logger log = Logger
			.getLogger(FoodTruckSearchEngineImpl.class.getName());
	private final int MAX_THREAD = 100;

	private DataManager dataManager;
	private ExecutorService pool;

	public FoodTruckSearchEngineImpl(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	public JSONArray findFoodTrucksByLL(String ll, Double distance) {
		if (FoodTruckUtils.isEmpty(ll) || distance == null) {
			log.error("FoodTruckSearchEngineImpl.findFoodTrucksByLL: no input data");
			return null;
		}

		String[] llComp = ll.split(",");
		if (llComp == null || llComp.length < 2) {
			log.error("FoodTruckSearchEngineImpl.findFoodTrucksByLL: input ll format error");
			return null;
		}

		Double lat;
		Double lng;
		try {
			lat = Double.parseDouble(llComp[0]);
			lng = Double.parseDouble(llComp[1]);
		} catch (NumberFormatException e) {
			log.error("FoodTruckSearchEngineImpl.findFoodTrucksByLL: input ll format error");
			e.printStackTrace();
			return null;
		}

		log.info(ll + "=lat:" + lat + ";lng:" + lng + ";distance:" + distance);

		JSONArray foodTrucks = getCloseFoodTrucks(lng, lat, distance);
		return foodTrucks;
	}

	@SuppressWarnings("unchecked")
	private JSONArray getCloseFoodTrucks(Double lng, Double lat,
			Double distance) {

		FoodTruckFeatureList foodTruckFeatureList = dataManager
				.getFoodTruckFeatureList();
		if (foodTruckFeatureList == null
				|| foodTruckFeatureList.getFoodTruckFeatureList() == null) {
			log.warn("FoodTruckSearchEngineImpl.getCloseFoodTrucks: no food truck info");
			return null;
		}

		if (lng == null || lat == null) {
			log.warn("FoodTruckSearchEngineImpl.getCloseFoodTrucks: no customer location info");
			return null;
		}

		List<FoodTruckFeature> foodTruckFeatures = foodTruckFeatureList
				.getFoodTruckFeatureList();

		if (foodTruckFeatures.size() == 0) {
			log.warn("FoodTruckSearchEngineImpl.getCloseFoodTrucks: no food truck at all");
			return null;
		}

		JSONArray results = new JSONArray();

		log.info("FoodTruckSearchEngineImpl.getCloseFoodTrucks : start making thread calls");
		int threadNumber = foodTruckFeatures.size();
		try {

			for (int i = 0; i < threadNumber; i += MAX_THREAD) {
				pool = Executors.newFixedThreadPool(MAX_THREAD);
				Set<Future<ThreadResults>> set = new HashSet<Future<ThreadResults>>();
				for (int j = i; j < i + MAX_THREAD && j < threadNumber; j++) {
					FoodTruckFeature foodTruckFeature = foodTruckFeatures
							.get(j);
					if (foodTruckFeature == null) {
						continue;
					}

					LongLat longLat = foodTruckFeature.getLl();
					if (longLat == null) {
						continue;
					}

					Double foodTruckLng = longLat.getLng();
					Double foodTruckLat = longLat.getLat();
					if (foodTruckLng == null || foodTruckLat == null) {
						continue;
					}

					Callable<ThreadResults> callable = new WebCallThread(lng,
							lat, foodTruckLng, foodTruckLat, distance,
							foodTruckFeature.getLocation(),
							foodTruckFeature.getCompany());
					Future<ThreadResults> future = pool.submit(callable);
					set.add(future);
				}

				for (Future<ThreadResults> future : set) {

					if (future == null) {
						continue;
					}
					ThreadResults result;
					try {
						result = future.get(100, TimeUnit.SECONDS);

						if (result == null) {
							continue;
						}

						JSONObject jsonResult = 
								FoodTruckUtils.createJson(result.getDistance(), 
										result.getAddress(), result.getProvider());
						results.add(jsonResult);

					} catch (InterruptedException e) {
						log.error("FoodTruckSearchEngineImpl.getCloseFoodTrucks: "
								+ e.toString());
					} catch (ExecutionException e) {
						log.error("FoodTruckSearchEngineImpl.getCloseFoodTrucks: "
								+ e.toString());
					} catch (TimeoutException e) {
						future.cancel(true);
					}
				}
				shutdownAndAwaitTermination();
			}
		} catch (IllegalArgumentException e) {
			log.error("FoodTruckSearchEngineImpl.getCloestFoodTruck: "
					+ e.toString());
		} finally {
			shutdownAndAwaitTermination();
		}

		log.info("FoodTruckSearchEngineImpl.getCloestFoodTruck : end getting external threads");

		return results;

	}

	public void shutdownAndAwaitTermination() {
		if (pool != null) {
			shutdownAndAwaitTermination(pool);
		}
	}

	private void shutdownAndAwaitTermination(ExecutorService pool) {
		log.info("shutdownAndAwaitTermination");
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(100, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(100, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			pool.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	class ThreadResults {
		private final String address;
		private final String provider;
		private final Double distance;

		public ThreadResults(Double distance, String address, String provider) {
			this.distance = distance;
			this.address = address;
			this.provider = provider;
		}

		public Double getDistance() {
			return distance;
		}

		public String getAddress() {
			return address;
		}

		public String getProvider() {
			return provider;
		}
	}

	class WebCallThread implements Callable<ThreadResults> {

		private final double lng1;
		private final double lng2;
		private final double lat1;
		private final double lat2;
		private final double distance;
		private final String address;
		private final String provider;

		public WebCallThread(double lng1, double lat1, double lng2,
				double lat2, double distance, String address, String provider) {
			this.lng1 = lng1;
			this.lng2 = lng2;
			this.lat1 = lat1;
			this.lat2 = lat2;
			this.distance = distance;
			this.address = address;
			this.provider = provider;
		}

		public ThreadResults call() {

			double curDis = FoodTruckUtils.calculateDistance(lat1, lng1, lat2,
					lng2);

			if (curDis < distance) {
				return new ThreadResults(curDis, address, provider);
			}
			return null;
		}
	}
}
