package de.adesso.softwareiotgateway.service;

import de.adesso.communication.messaging.UniversalSender;
import de.adesso.softwareiotgateway.service.queuing.QueuingService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RecoveryService {
    private final UniversalSender universalSender;
    private final QueuingService queuingService;
    private final Logger logger = LoggerFactory.getLogger(RecoveryService.class);

    @Autowired
    public RecoveryService(UniversalSender universalSender, QueuingService queuingService) {
        this.universalSender = universalSender;
        this.queuingService = queuingService;
    }

    @Scheduled(fixedRate = 1000)
    public void checkForDuplicates(){
        while(queuingService.hasDuplicateHardwarePicos()){
            Pair<String, String> toRecover = queuingService.getFirstElementsWaitingForRecovery();
            JSONObject infoMessage = new JSONObject().put("messageType", "info");
            universalSender.send(toRecover.getSecond(), infoMessage);
        }
    }

    public void recover(String status, String softwarePicoUri){
        String hardwarePicoUri = queuingService.getHardwarePicoToRecover(softwarePicoUri);
        if(status.equals("LOST")) {
            JSONObject rebindMessage = new JSONObject().put("messageType", "rebind").put("hardwarePicoUri", hardwarePicoUri);
            universalSender.send(softwarePicoUri, rebindMessage);
            logger.info("[Recovered Software-Pico URI=" + softwarePicoUri + " and Hardware-Pico URI=" + hardwarePicoUri + "]");
        }
        else {
            logger.info("[Pico URI=" + hardwarePicoUri + " was a duplicate]");
        }
    }
}
