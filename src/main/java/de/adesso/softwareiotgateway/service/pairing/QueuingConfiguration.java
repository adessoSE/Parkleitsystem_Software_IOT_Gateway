package de.adesso.softwareiotgateway.service.pairing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class QueuingConfiguration {

    @Bean
    public Set<String> hardwarePicoUris(){
        return new HashSet<>();
    }

    @Bean
    public Set<String> softwarePicoUris(){
        return new HashSet<>();
    }

    @Bean
    public Set<String> hardwarePicoUrisWaitingForRecovery(){
        return new HashSet<>();
    }

    @Bean
    public Map<String, String> pairedPicos(){
        return new HashMap<>();
    }

}
