# energy-consumption

This service helps store the energy consumption of villages measured with the use of 
counters. It is assumed that each counter can make HTTP API calls to this service.
The stored data for all the villages can then be made available on request.

The data submitted to this service is best stored using a time-series database because 
data persisted in the past cannot be updated or deleted.

One more reason, a time series database was chosen is because the energy consumption
report is needed for a time period(24 hours). This implies that the data persisted
before that time window is effectively useless.

The InfluxDb has been chosen to implement this application. It is a popular time-series
database, has good performance and  has well written documentation.






How to encrypt the InfluxDb database user password
----------
java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.2/jasypt-1.9.2.jar  org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="root" password=supersecretz algorithm=PBEWithMD5AndDES

Eventhough the Jasypt spring boot starter is version: 2.1.1,
the jasypt jar in maven is version 1.9.2

How to run the service
----------
mvn -Djasypt.encryptor.password=supersecretz spring-boot:run
