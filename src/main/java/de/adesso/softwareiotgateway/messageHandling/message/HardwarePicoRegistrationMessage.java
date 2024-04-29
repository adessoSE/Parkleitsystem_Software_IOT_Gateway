package de.adesso.softwareiotgateway.messageHandling.message;

import de.adesso.communication.messageHandling.Message;
import de.adesso.softwareiotgateway.messageHandling.SoftwareIotGatewayMessageType;

public record HardwarePicoRegistrationMessage(String hardwarePicoUri) implements Message {

    @Override
    public String getMessageType() {
        return SoftwareIotGatewayMessageType.REGISTER_HP.name();
    }
}
