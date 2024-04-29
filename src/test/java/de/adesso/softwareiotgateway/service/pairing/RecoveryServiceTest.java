package de.adesso.softwareiotgateway.service.pairing;

import de.adesso.softwareiotgateway.communication.cloud.CloudSender;
import de.adesso.softwareiotgateway.service.Pair;
import de.adesso.softwareiotgateway.service.queuing.QueuingService;
import de.adesso.softwareiotgateway.service.RecoveryService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecoveryServiceTest {

    static CloudSender cloudSenderMock;
    static QueuingService queuingServiceMock;
    static RecoveryService recoveryServiceToTest;

    @BeforeAll
    static void init(){
        cloudSenderMock = mock(CloudSender.class);
        queuingServiceMock = mock(QueuingService.class);
        recoveryServiceToTest = spy(new RecoveryService(cloudSenderMock, queuingServiceMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(cloudSenderMock, queuingServiceMock, recoveryServiceToTest);
    }

    @Test
    void checkForDuplicates(){

        Pair<String, String> p = new Pair<>("1/abc", UUID.randomUUID().toString());

        when(queuingServiceMock.hasDuplicateHardwarePicos()).thenReturn(true, false);
        when(queuingServiceMock.getFirstElementsWaitingForRecovery()).thenReturn(p);

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.forClass(JSONObject.class);

        recoveryServiceToTest.checkForDuplicates();

        verify(cloudSenderMock, times(1)).send(eq(p.getSecond()), captor.capture());

        JSONObject value = captor.getValue();
        assertEquals("info", value.getString("messageType"));
        assertEquals(p.getFirst(), value.getString("hardwarePicoUri"));

    }

    @Test
    void recoverRebind(){

        String hardwarePicoUri = "1/abc";
        String softwarePicoUri = UUID.randomUUID().toString();
        String status = "LOST";

        recoveryServiceToTest.recover(status, hardwarePicoUri, softwarePicoUri);

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.forClass(JSONObject.class);

        verify(cloudSenderMock, times(1)).send(eq(softwarePicoUri), captor.capture());

        JSONObject value = captor.getValue();
        assertEquals("rebind", value.getString("messageType"));
        assertEquals(hardwarePicoUri, value.getString("hardwarePicoUri"));

    }

    @Test
    void recoverDuplicate(){

        String hardwarePicoUri = "1/abc";
        String softwarePicoUri = UUID.randomUUID().toString();
        String status = "OK";

        verify(cloudSenderMock, never()).send(anyString(), any());

    }

}