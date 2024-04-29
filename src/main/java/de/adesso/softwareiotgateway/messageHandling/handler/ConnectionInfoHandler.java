package de.adesso.softwareiotgateway.messageHandling.handler;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.MessageHandler;
import de.adesso.softwareiotgateway.service.RecoveryService;
import de.adesso.softwareiotgateway.messageHandling.SoftwareIotGatewayMessageType;
import de.adesso.softwareiotgateway.messageHandling.message.ConnectionInfoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionInfoHandler implements MessageHandler {

    private final RecoveryService recoveryService;

    @Autowired
    public ConnectionInfoHandler(RecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }

    @Override
    public <T extends Message> void handle(T message) {
        if(supports(message)){
            ConnectionInfoMessage connectionInfoMessage = (ConnectionInfoMessage) message;
            recoveryService.recover(connectionInfoMessage.connectionStatus(), connectionInfoMessage.softwarePicoUri());
        }
    }

    @Override
    public <T extends Message> boolean supports(T message) {
        return message.getMessageType().equals(SoftwareIotGatewayMessageType.CONNECTION_INFO.name());
    }
}
