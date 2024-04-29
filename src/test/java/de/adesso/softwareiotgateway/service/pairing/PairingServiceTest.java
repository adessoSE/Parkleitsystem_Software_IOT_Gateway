package de.adesso.softwareiotgateway.service.pairing;

import de.adesso.softwareiotgateway.communication.cloud.CloudSender;
import de.adesso.softwareiotgateway.service.Pair;
import de.adesso.softwareiotgateway.service.PairingService;
import de.adesso.softwareiotgateway.service.queuing.QueuingService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PairingServiceTest {

    static CloudSender cloudSenderMock;
    static QueuingService queuingServiceMock;
    static PairingService pairingServiceToTest;


    @BeforeAll
    static void init(){
        cloudSenderMock = mock(CloudSender.class);
        queuingServiceMock = mock(QueuingService.class);
        pairingServiceToTest = spy(new PairingService(cloudSenderMock, queuingServiceMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(cloudSenderMock, queuingServiceMock, pairingServiceToTest);
    }

    @Test
    @SuppressWarnings("unchecked")
    void checkForPairings(){

        Pair<String, String> p = (Pair<String, String>) mock(Pair.class);

        when(queuingServiceMock.getFirstElements()).thenReturn(p);
        when(queuingServiceMock.pairingQueuesEmpty()).thenReturn(false, true);
        doNothing().when(pairingServiceToTest).sendBindMessage(p);
        doNothing().when(queuingServiceMock).addPairing(p);

        pairingServiceToTest.checkForPairings();

        verify(pairingServiceToTest, times(1)).sendBindMessage(p);
        verify(queuingServiceMock, times(1)).addPairing(p);

    }

    @Test
    @SuppressWarnings("unchecked")
    void sendBindMessage(){

        String hardwarePicoUri = "1/abc";
        String softwarePicoUri = UUID.randomUUID().toString();

        Pair<String, String> p = (Pair<String, String>) mock(Pair.class);

        when(p.getFirst()).thenReturn(hardwarePicoUri);
        when(p.getSecond()).thenReturn(softwarePicoUri);

        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.forClass(JSONObject.class);

        pairingServiceToTest.sendBindMessage(p);

        verify(cloudSenderMock, times(1)).send(eq(softwarePicoUri), captor.capture());

        JSONObject value = captor.getValue();
        assertEquals("bind", value.getString("messageType"));
        assertEquals(hardwarePicoUri, value.getString("hardwarePicoUri"));


    }


}