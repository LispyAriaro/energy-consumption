# energy-consumption

This service helps store the energy consumption of villages measured with the use of 
counters. It is assumed that each counter can make HTTP API calls to this service.
The stored data for all the villages can then be made available on request.

This service is implemented in Java with the Spring boot 2.1.6 framework.

The data submitted to this service is best stored using a time-series database because 
data persisted in the past cannot be updated or deleted.

One more reason, a time series database was chosen is because the energy consumption
report is needed for a time period(last 24 hours). This implies that the data persisted
before that time window is effectively useless.

The InfluxDb has been chosen to implement this application. It is a popular time-series
database, has good performance and  has well written documentation.

The model of the system relies on:
- the counter and
- the counter energy consumption

The counter is the entity that can send energy consumption data to this service.
The data sent by the counter can only increase i.e every successive API call it makes
will have an 'amount' value that is equal or higher than whatever was sent in the past.

The energy consumption report for any one counter when required is the difference of
the latest entry made by the counter and the first entry made by the counter within
the time duration specified in the API request.

InfluxDb's retention policy helps to automatically delete data before the last 24 hours.

Javax validation is used to validate the POST body sent in the API requests.

With more time, proper testing would have been done using an Influx Db cloud instance to
perform integration through calling the services API endpoints.

I used Jetty instead of Tomcat as the application server because Jetty is performs a little 
better than Tomcat from performance benchmarks. 
See here: https://www.baeldung.com/spring-boot-servlet-containers


How to encrypt the InfluxDb database user password
----------
java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.2/jasypt-1.9.2.jar  org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="root" password=supersecretz algorithm=PBEWithMD5AndDES

Eventhough the Jasypt spring boot starter is version: 2.1.1,
the jasypt jar in maven is version 1.9.2

How to run the service
----------
mvn -Djasypt.encryptor.password=supersecretz spring-boot:run
