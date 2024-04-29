package de.adesso.softwareiotgateway.messageHandling.handler;

import de.adesso.softwareiotgateway.messageHandling.SoftwareIotGatewayMessageType;
import de.adesso.softwareiotgateway.messageHandling.message.HardwarePicoRegistrationMessage;
import de.adesso.softwareiotgateway.messageHandling.message.Message;
import de.adesso.softwareiotgateway.service.queuing.QueuingService;
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
        when(m.getMessageType()).thenReturn(SoftwareIotGatewayMessageType.REGISTER_HP);

        assertTrue(hardwarePicoRegistrationHandlerToTest.supports(m));
    }

    @Test
    void supportsFalse(){

        for(SoftwareIotGatewayMessageType mt : SoftwareIotGatewayMessageType.values()){
            if(!mt.equals(SoftwareIotGatewayMessageType.REGISTER_HP)){
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

        for(SoftwareIotGatewayMessageType mt : SoftwareIotGatewayMessageType.values()){
            if(!mt.equals(SoftwareIotGatewayMessageType.REGISTER_HP)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                hardwarePicoRegistrationHandlerToTest.handle(m);

                verify(queuingServiceMock, never()).queueHardwarePico(any());
            }

        }

    }

}