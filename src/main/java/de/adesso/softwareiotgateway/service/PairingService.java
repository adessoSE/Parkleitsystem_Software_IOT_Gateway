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
public class PairingService {
    private final UniversalSender universalSender;
    private final QueuingService queuingService;
    private final Logger logger = LoggerFactory.getLogger(PairingService.class);

    @Autowired
    public PairingService(UniversalSender universalSender, QueuingService queuingService) {
        this.universalSender = universalSender;
        this.queuingService = queuingService;
    }

    @Scheduled(fixedRate = 1000)
    public void checkForPairings(){
        while(!queuingService.pairingQueuesEmpty()){
            Pair<String, String> toPair = queuingService.getFirstElements();
            sendBindMessage(toPair);
            queuingService.addPairing(toPair);
        }
    }

    void sendBindMessage(Pair<String, String> pair){
        JSONObject bindMessage = new JSONObject().put("messageType" , "bind").put("hardwarePicoUri", pair.getFirst());
        universalSender.send(pair.getSecond(), bindMessage);
        logger.info("[Bound Software-Pico URI=" + pair.getSecond() + " to Hardware-Pico URI=" + pair.getFirst() + "]");
    }



}
