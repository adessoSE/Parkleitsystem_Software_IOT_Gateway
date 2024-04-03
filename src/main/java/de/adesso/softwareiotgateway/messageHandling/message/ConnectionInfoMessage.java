package de.adesso.softwareiotgateway.messageHandling.message;

import de.adesso.softwareiotgateway.messageHandling.MessageType;

public record ConnectionInfoMessage(String hardwarePicoUri, String softwarePicoUri, String connectionStatus) implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.CONNECTION_INFO;
    }
}
