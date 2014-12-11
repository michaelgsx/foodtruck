package uber.foodtruck.jmx;

import org.apache.log4j.Logger;

import uber.foodtruck.datamanager.DataManager;

public class UpdateManagerBeanContent implements UpdateManagerBean {
	
	private static Logger log = Logger.getLogger(UpdateManagerBeanContent.class.getName());

	private DataManager dataManager = null;
	
	public UpdateManagerBeanContent() {}
	
	public DataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	public void updateFoodTruckInfo(String content) {
		if(dataManager != null) {
			dataManager.setFoodTruckFeatureList(content);
		} else {
			log.error("UpdateManagerBeanContent.updateFoodTruckInfo: no valid data manager");
		}
		//TODO output the info to the local disk. 
		// so that the system can reload everything from the disk when restart. 
		log.info("UpdateManagerBeanContent.updateFoodTruckInfo: updated truck info");
	}

	public String printFoodTruckInfo() {
		log.info("connected");
		if(dataManager != null) {
			return dataManager.getFoodTruckFeatureListByString();
		} else {
			log.error("UpdateManagerBeanContent.updateFoodTruckInfo: no valid data manager");
		}
		return null;
	}
}
