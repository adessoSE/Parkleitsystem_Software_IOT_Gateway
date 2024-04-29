package de.adesso.softwareiotgateway.service.queuing;

import de.adesso.softwareiotgateway.service.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static de.adesso.softwareiotgateway.configuration.HardwarePicoUtils.equalIds;
import static de.adesso.softwareiotgateway.configuration.HardwarePicoUtils.idAlreadyInList;

@Service
public class QueuingService {
    private final Set<String> hardwarePicos;
    private final Set<String> softwarePicos;
    private final Set<String> duplicateHardwarePicos;
    private final Map<String, String> pendingRecoveries;
    private final Map<String, String> pairedPicos;
    private final Logger logger = LoggerFactory.getLogger(QueuingService.class);

    @Autowired
    public QueuingService(@Qualifier("hardwarePicos") Set<String> hardwarePicos, @Qualifier("softwarePicos") Set<String> softwarePicos,
                          @Qualifier("duplicateHardwarePicos") Set<String> duplicateHardwarePicos, Map<String, String> pendingRecoveries, Map<String, String> pairedPicos) {
        this.hardwarePicos = hardwarePicos;
        this.softwarePicos = softwarePicos;
        this.duplicateHardwarePicos = duplicateHardwarePicos;
        this.pendingRecoveries = pendingRecoveries;
        this.pairedPicos = pairedPicos;
    }

    public void queueHardwarePico(String uri){
        if(!idAlreadyInList(pairedPicos.keySet().stream().toList(), uri)) {
            hardwarePicos.add(uri);
            logger.info("[Queued Hardware-Pico URI=" + uri + "]");
        }
        else {
            duplicateHardwarePicos.add(uri);
            logger.info("[Queued Hardware-Pico URI=" + uri + " for Recovery]");
        }
    }

    public void queueSoftwarePico(String uri){
        if(!pairedPicos.containsValue(uri)) {
            softwarePicos.add(uri);
            logger.info("[Queued Software-Pico URI=" + uri + "]");
        }
    }

    public boolean pairingQueuesEmpty(){
        return hardwarePicos.isEmpty() || softwarePicos.isEmpty();
    }

    public Pair<String, String> getFirstElements(){
        if(!pairingQueuesEmpty()) {
            return new Pair<>(hardwarePicos.stream().toList().get(0), softwarePicos.stream().toList().get(0));
        }
        return null;
    }

    public void addPairing(Pair<String, String> pair){
        pairedPicos.put(pair.getFirst(), pair.getSecond());
        hardwarePicos.remove(pair.getFirst());
        softwarePicos.remove(pair.getSecond());
    }

    public boolean hasDuplicateHardwarePicos(){
        return !duplicateHardwarePicos.isEmpty();
    }

    public Pair<String, String> getFirstElementsWaitingForRecovery(){
        if(hasDuplicateHardwarePicos()) {
            String hardwarePicoUri = duplicateHardwarePicos.stream().toList().get(0);
            duplicateHardwarePicos.remove(hardwarePicoUri);
            Pair<String, String> p =  new Pair<>(hardwarePicoUri, getSoftwareUriForHardwareUri(hardwarePicoUri));
            pendingRecoveries.put(p.getSecond(), p.getFirst());
            return p;
        }
        return null;
    }

    public String getSoftwareUriForHardwareUri(String hardwarePicoUri){
        for(String s : pairedPicos.keySet()){
            if(equalIds(s, hardwarePicoUri)){
                return pairedPicos.get(s);
            }
        }
        return null;
    }

    public String getHardwarePicoToRecover(String softwarePico){
        return pendingRecoveries.get(softwarePico);
    }


}
