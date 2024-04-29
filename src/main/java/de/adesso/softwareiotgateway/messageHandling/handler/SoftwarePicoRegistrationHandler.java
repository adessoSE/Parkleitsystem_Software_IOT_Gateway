package de.adesso.softwareiotgateway.messageHandling.handler;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.MessageHandler;
import de.adesso.softwareiotgateway.messageHandling.SoftwareIotGatewayMessageType;
import de.adesso.softwareiotgateway.messageHandling.message.SoftwarePicoRegistrationMessage;
import de.adesso.softwareiotgateway.service.queuing.QueuingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SoftwarePicoRegistrationHandler implements MessageHandler {

    private final QueuingService queuingService;

    @Autowired
    public SoftwarePicoRegistrationHandler(QueuingService queuingService) {
        this.queuingService = queuingService;
    }

    @Override
    public <T extends Message> void handle(T message) {
        if(supports(message)){
            queuingService.queueSoftwarePico(((SoftwarePicoRegistrationMessage) message).softwarePicoUri());
        }
    }

    @Override
    public <T extends Message> boolean supports(T message) {
        return message.getMessageType().equals(SoftwareIotGatewayMessageType.REGISTER_SP.name());
    }

}
