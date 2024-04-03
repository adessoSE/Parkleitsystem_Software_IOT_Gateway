package de.adesso.softwareiotgateway.service.pairing;

import de.adesso.softwareiotgateway.communication.cloud.CloudSender;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RecoveryService {
    private final CloudSender cloudSender;
    private final QueuingService queuingService;
    private final Logger logger = LoggerFactory.getLogger(RecoveryService.class);

    @Autowired
    public RecoveryService(CloudSender cloudSender, QueuingService queuingService) {
        this.cloudSender = cloudSender;
        this.queuingService = queuingService;
    }

    @Scheduled(fixedRate = 1000)
    public void checkForDuplicates(){
        while(queuingService.hasHardwarePicosWaitingForRecovery()){
            Pair<String, String> toRecover = queuingService.getFirstElementsWaitingForRecovery();
            JSONObject infoMessage = new JSONObject().put("messageType", "info").put("hardwarePicoUri", toRecover.getFirst());
            cloudSender.send(toRecover.getSecond(), infoMessage);
        }
    }

    public void recover(String status, String hardwarePicoUri, String softwarePicoUri){
        if(status.equals("LOST")) {
            JSONObject rebindMessage = new JSONObject().put("messageType", "rebind").put("hardwarePicoUri", hardwarePicoUri);
            cloudSender.send(softwarePicoUri, rebindMessage);
            logger.info("[Recovered Software-Pico URI=" + softwarePicoUri + " and Hardware-Pico URI=" + hardwarePicoUri + "]");
        }
        else {
            logger.info("[Pico URI=" + hardwarePicoUri + " was a duplicate]");
        }
    }
}
