package uber.foodtruck.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class FoodTruckToolsUtils {

	private final static Logger logger = Logger
			.getLogger(FoodTruckToolsUtils.class);
	private final static int MAX_RETRY = 3;

	public static String getWebInfo(String webUrlString) {
		for (int retry = 0; retry < MAX_RETRY; retry++) {
			try {
				String response = getURLtext(webUrlString);
				if (response == null) {
					continue;
				}
				return response;
			} catch (Exception e) {
				logger.warn("timeout for the distance call and retry");
				e.printStackTrace();
			}
		}

		logger.error("timeout for the distance call");
		return null;
	}

	private static final String USER_AGENT = "Mozilla/5.0";

	private static String getURLtext(String zielurl) {
		String result = ""; // default empty string
		try {

			URL obj = new URL(zielurl);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + zielurl);
			System.out.println("Response Code : " + responseCode);
			if (responseCode != 200) {
				return null;
			}

			InputStream is = con.getInputStream();
			result = inputStreamToString(is).toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 

		return result;
	}

	// Fast Implementation
	private static StringBuilder inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();

		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(rd != null) {
				try {
					rd.close();
				} catch (IOException e) {}
			}
		}

		return total;
	}

	public static String readFromFile(String filename) {
		String line;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(filename)));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();
	}

	public static void writeToFile(String filename, String content) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename)));
			writer.write(content);
			writer.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
