# energy-consumption




How to encrypt the InfluxDb database user password
----------
java -cp ~/.m2/repository/org/jasypt/jasypt/1.9.2/jasypt-1.9.2.jar  org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="root" password=supersecretz algorithm=PBEWithMD5AndDES

Eventhough the Jasypt spring boot starter is version: 2.1.1,
the jasypt jar in maven is version 1.9.2

How to run the service
----------
mvn -Djasypt.encryptor.password=supersecretz spring-boot:run

