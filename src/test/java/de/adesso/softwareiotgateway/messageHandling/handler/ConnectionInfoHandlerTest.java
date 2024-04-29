package de.adesso.softwareiotgateway.messageHandling.handler;

import de.adesso.softwareiotgateway.service.RecoveryService;
import de.adesso.softwareiotgateway.messageHandling.SoftwareIotGatewayMessageType;
import de.adesso.softwareiotgateway.messageHandling.message.ConnectionInfoMessage;
import de.adesso.softwareiotgateway.messageHandling.message.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConnectionInfoHandlerTest {

    static RecoveryService recoveryServiceMock;
    static ConnectionInfoHandler connectionInfoHandlerToTest;

    @BeforeAll
    static void inti(){
        recoveryServiceMock = mock(RecoveryService.class);
        connectionInfoHandlerToTest = new ConnectionInfoHandler(recoveryServiceMock);
    }

    @BeforeEach
    void resetMocks(){
        reset(recoveryServiceMock);
    }

    @Test
    void supportsTrue(){

        Message m = mock(Message.class);
        when(m.getMessageType()).thenReturn(SoftwareIotGatewayMessageType.CONNECTION_INFO);

        assertTrue(connectionInfoHandlerToTest.supports(m));
    }

    @Test
    void supportsFalse(){

        for(SoftwareIotGatewayMessageType mt : SoftwareIotGatewayMessageType.values()){
            if(!mt.equals(SoftwareIotGatewayMessageType.CONNECTION_INFO)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                assertFalse(connectionInfoHandlerToTest.supports(m));
            }

        }

    }

    @Test
    void handleSupported(){

        String hardwarePicoUri = "1/abc";
        String softwarePicoUri = UUID.randomUUID().toString();
        String connectionStatus = "OK";
        ConnectionInfoMessage m = new ConnectionInfoMessage(hardwarePicoUri, softwarePicoUri, connectionStatus);

        connectionInfoHandlerToTest.handle(m);

        verify(recoveryServiceMock, times(1)).recover(connectionStatus, hardwarePicoUri, softwarePicoUri);

    }

    @Test
    void handleUnsupported(){

        for(SoftwareIotGatewayMessageType mt : SoftwareIotGatewayMessageType.values()){
            if(!mt.equals(SoftwareIotGatewayMessageType.CONNECTION_INFO)){
                Message m = mock(Message.class);
                when(m.getMessageType()).thenReturn(mt);

                connectionInfoHandlerToTest.handle(m);

                verify(recoveryServiceMock, never()).recover(any(), any(), any());
            }

        }

    }

}