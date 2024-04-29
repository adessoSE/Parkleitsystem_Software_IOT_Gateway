package de.adesso.softwareiotgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("de.adesso")
public class SoftwareIotGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftwareIotGatewayApplication.class, args);
    }

}
