package de.adesso.softwareiotgateway.messageHandling.message;

import de.adesso.communication.messageHandling.Message;
import de.adesso.softwareiotgateway.messageHandling.SoftwareIotGatewayMessageType;

public record SoftwarePicoRegistrationMessage(String softwarePicoUri) implements Message {
    @Override
    public String getMessageType() {
        return SoftwareIotGatewayMessageType.REGISTER_SP.name();
    }
}
