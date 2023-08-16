package com.ceyloncab.paymentmgtservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class PaymentmgtserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentmgtserviceApplication.class, args);
	}

}
