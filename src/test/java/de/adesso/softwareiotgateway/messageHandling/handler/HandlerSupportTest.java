package de.adesso.softwareiotgateway.messageHandling.handler;

import de.adesso.softwareiotgateway.messageHandling.MessageFactory;
import de.adesso.softwareiotgateway.messageHandling.MessageType;
import de.adesso.softwareiotgateway.messageHandling.message.Message;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HandlerSupportTest {

    @Autowired
    List<MessageHandler> messageHandlers;

    @Test
    void allTypesSupported(){
        for(MessageType mt : MessageType.values()){
            Message m = () -> mt;
            boolean supported = false;
            for(MessageHandler h : messageHandlers){
                supported = supported || h.supports(m);
            }
            assertTrue(supported);
        }
    }

}
