package de.adesso.softwareiotgateway.messageHandling.handler;

import de.adesso.softwareiotgateway.service.pairing.PairingService;
import de.adesso.softwareiotgateway.messageHandling.MessageType;
import de.adesso.softwareiotgateway.messageHandling.message.Message;
import de.adesso.softwareiotgateway.messageHandling.message.SoftwarePicoRegistrationMessage;
import de.adesso.softwareiotgateway.service.pairing.QueuingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SoftwarePicoRegistrationHandlerTest {

    static QueuingService queuingServiceMock;
    static SoftwarePicoRegistrationHandler softwarePicoRegistrationHandlerToTest;

    @BeforeAll
    static void init(){
        queuingServiceMock = mock(QueuingService.class);
        softwarePicoRegistrationHandlerToTest = new SoftwarePicoRegistrationHandler(queuingServiceMock);
    }

    @BeforeEach
    void resetMocks(){
        reset(queuingServiceMock);
    }

    @Test
    void supportsTrue(){

        Message m = mock(Message.class);
        when(m.getMessageType()).thenReturn(MessageType.REGISTER_SP);

        assertTrue(softwarePicoRegistrationHandlerToTest.supports(m));
    }

    @Test
    void supportsFalse(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.REGISTER_SP)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                assertFalse(softwarePicoRegistrationHandlerToTest.supports(m));
            }

        }

    }

    @Test
    void handleSupported(){

        String softwarePicoUri = UUID.randomUUID().toString();
        SoftwarePicoRegistrationMessage m = new SoftwarePicoRegistrationMessage(softwarePicoUri);

        softwarePicoRegistrationHandlerToTest.handle(m);

        verify(queuingServiceMock, times(1)).queueSoftwarePico(softwarePicoUri);

    }

    @Test
    void handleUnsupported(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.REGISTER_SP)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                softwarePicoRegistrationHandlerToTest.handle(m);

                verify(queuingServiceMock, never()).queueHardwarePico(any());
            }

        }

    }

}