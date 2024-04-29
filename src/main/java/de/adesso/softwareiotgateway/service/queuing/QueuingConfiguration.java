package de.adesso.softwareiotgateway.service.queuing;

import de.adesso.softwareiotgateway.service.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class QueuingConfiguration {

    @Bean
    public Set<String> hardwarePicos(){
        return new HashSet<>();
    }

    @Bean
    public Set<String> softwarePicos(){
        return new HashSet<>();
    }

    @Bean
    public Set<String> duplicateHardwarePicos(){
        return new HashSet<>();
    }

    @Bean
    public Map<String, String> pendingRecoveries(){
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, String> pairedPicos(){
        return new ConcurrentHashMap<>();
    }

}
