package de.adesso.softwareiotgateway;

import de.adesso.softwareiotgateway.configuration.MqttProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(MqttProperties.class)
@EnableScheduling
public class SoftwareIotGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftwareIotGatewayApplication.class, args);
    }

}
