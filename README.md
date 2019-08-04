# energy-consumption

This application helps store the energy consumption of villages measured with the use of 
counters. It is assumed that each counter can make HTTP API calls to this service.
The stored data for all the villages can then made available on request.

The data submitted to this service is best stored using a time-series database because
this in this domain data persisted in the past cannot be updated or deleted.

The InfluxDb has been chosen to implement this application. It is popular time-series
database, has good performance and  has well written documentation.



How to encrypt the InfluxDb database user password
----------
java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.2/jasypt-1.9.2.jar  org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="root" password=supersecretz algorithm=PBEWithMD5AndDES

Eventhough the Jasypt spring boot starter is version: 2.1.1,
the jasypt jar in maven is version 1.9.2

How to run the service
----------
mvn -Djasypt.encryptor.password=supersecretz spring-boot:run
