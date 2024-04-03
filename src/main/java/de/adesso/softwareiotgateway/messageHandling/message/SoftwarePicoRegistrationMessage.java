package de.adesso.softwareiotgateway.messageHandling.message;

import de.adesso.softwareiotgateway.messageHandling.MessageType;

public record SoftwarePicoRegistrationMessage(String softwarePicoUri) implements Message {
    @Override
    public MessageType getMessageType() {
        return MessageType.REGISTER_SP;
    }
}
