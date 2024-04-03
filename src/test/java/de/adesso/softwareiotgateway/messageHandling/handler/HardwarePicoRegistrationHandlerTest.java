package de.adesso.softwareiotgateway.messageHandling.handler;

import de.adesso.softwareiotgateway.service.pairing.PairingService;
import de.adesso.softwareiotgateway.messageHandling.MessageType;
import de.adesso.softwareiotgateway.messageHandling.message.HardwarePicoRegistrationMessage;
import de.adesso.softwareiotgateway.messageHandling.message.Message;
import de.adesso.softwareiotgateway.service.pairing.QueuingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HardwarePicoRegistrationHandlerTest {

    static QueuingService queuingServiceMock;
    static HardwarePicoRegistrationHandler hardwarePicoRegistrationHandlerToTest;

    @BeforeAll
    static void init(){
        queuingServiceMock = mock(QueuingService.class);
        hardwarePicoRegistrationHandlerToTest = new HardwarePicoRegistrationHandler(queuingServiceMock);
    }

    @BeforeEach
    void resetMocks(){
        reset(queuingServiceMock);
    }

    @Test
    void supportsTrue(){

        Message m = mock(Message.class);
        when(m.getMessageType()).thenReturn(MessageType.REGISTER_HP);

        assertTrue(hardwarePicoRegistrationHandlerToTest.supports(m));
    }

    @Test
    void supportsFalse(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.REGISTER_HP)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                assertFalse(hardwarePicoRegistrationHandlerToTest.supports(m));
            }

        }

    }

    @Test
    void handleSupported(){

        String hardwarePicoUri = "1/abc";
        HardwarePicoRegistrationMessage m = new HardwarePicoRegistrationMessage(hardwarePicoUri);

        hardwarePicoRegistrationHandlerToTest.handle(m);

        verify(queuingServiceMock, times(1)).queueHardwarePico(hardwarePicoUri);

    }

    @Test
    void handleUnsupported(){

        for(MessageType mt : MessageType.values()){
            if(!mt.equals(MessageType.REGISTER_HP)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                hardwarePicoRegistrationHandlerToTest.handle(m);

                verify(queuingServiceMock, never()).queueHardwarePico(any());
            }

        }

    }

}