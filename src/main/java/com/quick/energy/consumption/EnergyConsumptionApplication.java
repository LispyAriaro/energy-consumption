package com.quick.energy.consumption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.quick.energy.consumption"})
@SpringBootApplication
public class EnergyConsumptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergyConsumptionApplication.class, args);
	}

}
