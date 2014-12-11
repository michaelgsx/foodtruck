ReadMe for Food Truck Project

1.	Background
	This project is designed to locate the nearest food truck with the provider name. This project is a Maven project. Maven will be required for the compilation. 
	
2.	Design and Implementation
	In this project, there are four packages involved — foodtruck, foodtruckcommon, foodtruckjmx and foodtrucktools. After unzip the package, we can run “mvn clean install” to build the project. All four packages will be built based on the pom.xml. 
	a.	FoodTruckCommon package
	This package is used to create all the Json data definitions for the information required in this project. For example, we need to collect the information of a food truck, such as location, address and provider. We can create a Json based class by which we can easily turn into and print out a Json formatted dataset. 
	In the implementation, the Json mapper could be a performance bottleneck. Therefore, I create one Json mapper for each class, instead of one Json mapper for all.

	b.	FoodTruckTools package
	This package is used to collect the information from the website, parse it and store it into an output file. Suppose we have a fleet of VMs for this web service. It is inefficient to ask each VM to download the food truck information online and update the service by itself. To avoid such a waste of the resources, we can use one machine, called data processor, to download and parse the information, then copy it to all the VMs and use a JMX call to update the service. To make different implementations for this purpose, I create an interface for the parsing task. At the beginning, I did not know how to get all the 600 foodtruck information. When I use the GET for the link of https://data.sfgov.org/Economy-and-Community/Mobile-Food-Facility-Permit/rqzj-sfat?, I only can have 20 food truck information due to the dynamic table generation. It is my first implementation. Then after research, I found the correct link to get all the food truck info is https://data.sfgov.org/resource/rqzj-sfat.json. Therefore, I made my second corrected parsing implemention. To show the entire mental procedure, I leave both implementation in this package.  
	The installation file is built and put at 
				foodtrucktools/target/foodtrucktools-tools-install.zip
	command:
				bin/foodtruckinfotrack.sh output.dat

	c.	FoodTruckJMX package
	As mentioned above, after the data processor collected all the food truck information, we can copy the output file to all the VMs. Then a JMX call is triggered to update the service data. This procedure can be designed as a Cron job. So that we can update the service every 1 or 2 minutes, without severely interfering the service performance. 
	The installation file is built and put at
				foodtruckjmx/target/foodtruckjmx-jmx-client-install.zip
	command:
				bin/updateFoodTruckInfo.sh /app/foodtruck/locations.dat
				bin/printFoodTruckInfo.sh 

	d.	FoodTruck package
	This package is used to create .war file 
				at foodtruck/target/foodtruck-service.war as the service.
	To build this service, I used SpringMVC framework with a set of Java Beans. Each bean is unique for the service. To calculate the distance between the customer and the foodtruck, I use a function to calculate the distance based on the latitude and longtitude. It is not necessary to use multi-thread for the calculation. But since each distance calcuation is independent, I designed it as a multi-thread procedure. To allow the JMX call to update the food truck information data in the service, I have an AtomicReference in DataManager class to contain the data set so that there will be no synchronization issue.  

3.	Setting and Config
	
	a. 	Set up the service:
		i. 	unzip foodtruck.zip
		ii. 	mvn clean install ---- in the foodtruck folder
		iii. 	cp foodtruck/locations.dat to /app/foodtruck/locations.dat
		iv. 	cp foodtruck/foodtruck/target/foodtruck.war /Library/Tomcat/webapp/

	b.	Set up the script to get the food truck information online. 
		i. 	unzip foodtruck/foodtrucktools/target/foodtrucktools-tools-install.zip
		ii. 	cd  foodtruck/foodtrucktools/target/foodtrucktools
		iii. 	./bin/foodtruckinfotrack.sh /app/foodtruck/locations.dat

	c.	Set up the script to update the service
		i. 	unzip foodtruck/foodtruckjmx/target/foodtruckjmx-install.zip
		ii. 	cd foodtruck/foodtruckjmx/target/foodtruckjmx
		iii. 	./bin/updateFoodTruckInfo.sh /app/foodtruck/locations.dat

	d.	Optional
		i.	set up a cronjob so that we can automatically update the service every 1-2 minutes. 

4.	Test
	a.	TestNG
	TestNG is used to build the test cases. 
	For the unit test, I can provide data by filling up a set of data files. 
	For the integration test, in which I have to all the outside service, I set up (enable = false) after the integration test is done. 
	The major test cases are covered in my implementation. But not 100% test cases are covered due to the tight schedule. I can make a more robust code base by adding more test cases when I have timed
	One thing that is always in my mind is the Null check. We always should have a Null check when given an Object. Large percent of the service exceptions are NullPointerExceptions.
	b.	Test URL
				http://localhost:8080/foodtruck-service/requestbyll/37.7251817007288,-122.388192688444/3800.0
				a result json will be output.  

5.	Other things about me
	As I have proved that I can write a service from the ground. 
	Big data is widely used for all the aspects of the webservice. I have worked 3 years as data analyst with FDA endorsed code at mumoc.googlecode.com. I have the knowledge of the machine learning, hadoop, hive and statistics. I am suitable not only the service backend coding, but also the data modeling.  
	Since I have worked in the CS field for more than 12 years, I can learn anything quickly.  
	The last thing I want to mention is that I am very suitable for a long time, heavy workload.

Thank you for your consideration. 

Songxiang Gu 
	
