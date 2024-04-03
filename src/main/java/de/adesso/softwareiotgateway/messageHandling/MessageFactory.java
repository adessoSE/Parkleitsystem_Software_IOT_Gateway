package de.adesso.softwareiotgateway.messageHandling;

import de.adesso.softwareiotgateway.messageHandling.message.ConnectionInfoMessage;
import de.adesso.softwareiotgateway.messageHandling.message.HardwarePicoRegistrationMessage;
import de.adesso.softwareiotgateway.messageHandling.message.Message;
import de.adesso.softwareiotgateway.messageHandling.message.SoftwarePicoRegistrationMessage;
import org.json.JSONObject;

public class MessageFactory {

    public static Message fromJson(JSONObject jsonMessage){
        MessageType messageType = MessageType.valueOf(jsonMessage.getString("messageType").toUpperCase());
        return switch (messageType){
            case REGISTER_HP -> {
                String uri = jsonMessage.getString("uri");
                yield new HardwarePicoRegistrationMessage(uri);
            }
            case REGISTER_SP -> {
                String uri = jsonMessage.getString("uri");
                yield new SoftwarePicoRegistrationMessage(uri);
            }
            case CONNECTION_INFO -> {
                String hardwarePicoUri = jsonMessage.getString("hardwarePicoUri");
                String softwarePicoUri = jsonMessage.getString("softwarePicoUri");
                String connectionStatus = jsonMessage.getString("connectionStatus");
                yield new ConnectionInfoMessage(hardwarePicoUri, softwarePicoUri, connectionStatus);
            }
        };
    }

}
