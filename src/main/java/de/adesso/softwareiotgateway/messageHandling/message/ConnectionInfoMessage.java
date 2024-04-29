package de.adesso.softwareiotgateway.messageHandling.message;

import de.adesso.communication.messageHandling.Message;
import de.adesso.softwareiotgateway.messageHandling.SoftwareIotGatewayMessageType;


public record ConnectionInfoMessage(String softwarePicoUri, String connectionStatus) implements Message {

    @Override
    public String getMessageType() {
        return SoftwareIotGatewayMessageType.CONNECTION_INFO.name();
    }
}
