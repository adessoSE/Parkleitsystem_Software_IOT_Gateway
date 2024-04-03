package de.adesso.softwareiotgateway.service;

import de.adesso.softwareiotgateway.communication.Receiver;
import de.adesso.softwareiotgateway.messageHandling.MessageFactory;
import de.adesso.softwareiotgateway.messageHandling.handler.MessageHandler;
import de.adesso.softwareiotgateway.messageHandling.message.Message;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final List<MessageHandler> messageHandlers;
    private final Receiver receiver;

    @Autowired
    public MessageService(@Qualifier("universalReceiver") Receiver receiver, List<MessageHandler> messageHandlers) {
        this.messageHandlers = messageHandlers;
        this.receiver = receiver;
    }

    @PostConstruct
    public void subscribeToInbound(){
        receiver.subscribe("software-iot-gateway", this::handle);
    }

    public void handle(JSONObject jsonMessage){
        Message message = MessageFactory.fromJson(jsonMessage);
        MessageHandler handler = findSupportingMessageHandler(message);
        handler.handle(message);
    }

    MessageHandler findSupportingMessageHandler(Message message){
        return messageHandlers.stream().filter(messageHandler -> messageHandler.supports(message)).findFirst().orElseThrow();
    }
}
