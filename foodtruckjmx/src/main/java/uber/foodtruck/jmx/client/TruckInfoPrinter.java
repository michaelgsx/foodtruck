package uber.foodtruck.jmx.client;

import java.io.PrintStream;

public class TruckInfoPrinter extends JmxBase {
	
	private static void printContentInMBean() {
		PrintStream out = new PrintStream(System.out);
		String content = getMBeanProxy().printFoodTruckInfo();
		out.print(content);
	}
	
	public static void main(String[] args) {
		init();
		printContentInMBean();
		close();
	}
}
