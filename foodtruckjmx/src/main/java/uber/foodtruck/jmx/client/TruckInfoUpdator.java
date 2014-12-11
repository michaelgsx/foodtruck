package uber.foodtruck.jmx.client;

import uber.foodtruck.utils.FoodTruckToolsUtils;
import uber.foodtruck.utils.FoodTruckUtils;

public class TruckInfoUpdator extends JmxBase {

	private static void updateContentInMBean(String content) {
		try {
			getMBeanProxy().updateFoodTruckInfo(content);
		} catch (Exception e) {
			System.err.println("Infomation updating failed.");
		}
	}

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("Usage: <input_info_file>");
			System.exit(1);
		}

		String content = FoodTruckToolsUtils.readFromFile(args[0]);

		if (FoodTruckUtils.isEmpty(content)) {
			System.err.println("Empty input file.");
		}
		init();
		updateContentInMBean(content);
		close();

	}
}
