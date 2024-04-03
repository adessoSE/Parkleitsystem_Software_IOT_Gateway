package de.adesso.softwareiotgateway.messageHandling.message;

import de.adesso.softwareiotgateway.messageHandling.MessageType;

public record HardwarePicoRegistrationMessage(String hardwarePicoUri) implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.REGISTER_HP;
    }
}
