package uber.foodtruck.jmx.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

import uber.foodtruck.jmx.UpdateManagerBean;

public class JmxBase {

	private static final Logger logger = Logger.getLogger(JmxBase.class);

	private static String contentManagerMBeanObject = "foodtruck:name=updateManagerBean";
	private static List<String> objectName;
	private static JMXConnector jmxc;
	private static MBeanServerConnection mbsc;
	private static ObjectName mbeanName;
	private static UpdateManagerBean mbeanProxy;

	protected static void init() {
		objectName = new ArrayList<String>();
		try {
			objectName.add(contentManagerMBeanObject);
			connectToLocalJMXServer(9998);
			mbeanName = new ObjectName(contentManagerMBeanObject);
			mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName,
					UpdateManagerBean.class, true);

			if (mbeanProxy == null) {
				throw new NullPointerException(
						"No newMBeanProxy client return null.");
			}
		} catch (IOException e) {
			logger.error("Can not initialize the JMX bean.");
			close();
			System.exit(1);
		} catch (NullPointerException e) {
			logger.error(e.toString());
			close();
			System.exit(1);
		} catch (MalformedObjectNameException e) {
			logger.error(e.toString());
			close();
			System.exit(1);
		}
	}

	protected static void close() {
		if (jmxc != null) {
			try {
				jmxc.close();
			} catch (IOException e) {
			}
		}

	}

	private static void connectToLocalJMXServer(int port) throws IOException {
		JMXServiceURL url;
		url = new JMXServiceURL("service:jmx:jmxmp://localhost:" + port);
		jmxc = JMXConnectorFactory.connect(url, null);
		if (jmxc == null) {
			logger.error("Cannot create JMX connection");
			System.exit(1);
		}
		mbsc = jmxc.getMBeanServerConnection();
	}

	public static UpdateManagerBean getMBeanProxy() {
		return mbeanProxy;
	}
}
