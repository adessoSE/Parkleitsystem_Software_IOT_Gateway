package de.adesso.softwareiotgateway.messageHandling;

import de.adesso.communication.messageHandling.Message;
import de.adesso.communication.messageHandling.MessageFactory;
import de.adesso.communication.messageHandling.error.JsonMessageNotSupportedException;
import de.adesso.softwareiotgateway.messageHandling.message.ConnectionInfoMessage;
import de.adesso.softwareiotgateway.messageHandling.message.HardwarePicoRegistrationMessage;
import de.adesso.softwareiotgateway.messageHandling.message.SoftwarePicoRegistrationMessage;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SoftwareIotGatewayMessageFactory implements MessageFactory {

    @Override
    public Message fromJson(JSONObject jsonMessage) throws JsonMessageNotSupportedException {
        if(!supports(jsonMessage)) throw new JsonMessageNotSupportedException(jsonMessage);
        SoftwareIotGatewayMessageType softwareIotGatewayMessageType = SoftwareIotGatewayMessageType.valueOf(jsonMessage.getString("messageType").toUpperCase());
        return switch (softwareIotGatewayMessageType){
            case REGISTER_HP -> {
                String uri = jsonMessage.getString("uri");
                yield new HardwarePicoRegistrationMessage(uri);
            }
            case REGISTER_SP -> {
                String uri = jsonMessage.getString("uri");
                yield new SoftwarePicoRegistrationMessage(uri);
            }
            case CONNECTION_INFO -> {
                String softwarePicoUri = jsonMessage.getString("softwarePicoUri");
                String connectionStatus = jsonMessage.getString("connectionStatus");
                yield new ConnectionInfoMessage(softwarePicoUri, connectionStatus);
            }
        };
    }

    @Override
    public boolean supports(JSONObject jsonMessage) {
        try {
            return jsonMessage.has("messageType") &&
                    Arrays.stream(SoftwareIotGatewayMessageType.values()).toList().contains(
                            SoftwareIotGatewayMessageType.valueOf(jsonMessage.getString("messageType").toUpperCase()));
        }
        catch (Exception e){
            return false;
        }
    }

}
