package de.adesso.softwareiotgateway.service.pairing;


import de.adesso.softwareiotgateway.configuration.HardwarePicoUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueuingServiceTest {

    static Set<String> hardwarePicosMock;
    static Set<String> softwarePicosMock;
    static Set<String> hardwarePicosWaitingForRecoveryMock;
    static Map<String, String> pairedPicosMock;
    static QueuingService queuingServiceToTest;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void init(){
        hardwarePicosMock = (Set<String>) mock(Set.class);
        softwarePicosMock = (Set<String>) mock(Set.class);
        hardwarePicosWaitingForRecoveryMock = (Set<String>) mock(Set.class);
        pairedPicosMock = (Map<String, String>) mock(Map.class);
        queuingServiceToTest = spy(new QueuingService(hardwarePicosMock, softwarePicosMock, hardwarePicosWaitingForRecoveryMock, pairedPicosMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(hardwarePicosMock, softwarePicosMock, hardwarePicosWaitingForRecoveryMock, pairedPicosMock, queuingServiceToTest);
    }

    @Test
    void queueHardwarePico(){

        String hardwarePicoUri = "1/abc";

        MockedStatic<HardwarePicoUtils> hardwarePicoUtilsMock = mockStatic(HardwarePicoUtils.class);

        hardwarePicoUtilsMock.when(() -> HardwarePicoUtils.idAlreadyInList(anyList(), anyString())).thenReturn(false);

        queuingServiceToTest.queueHardwarePico(hardwarePicoUri);

        verify(hardwarePicosMock, times(1)).add(hardwarePicoUri);

        hardwarePicoUtilsMock.close();

    }

    @Test
    void queueHardwarePicoDuplicate(){

        String hardwarePicoUri = "1/abc";

        MockedStatic<HardwarePicoUtils> hardwarePicoUtilsMock = mockStatic(HardwarePicoUtils.class);

        hardwarePicoUtilsMock.when(() -> HardwarePicoUtils.idAlreadyInList(anyList(), anyString())).thenReturn(true);

        queuingServiceToTest.queueHardwarePico(hardwarePicoUri);

        verify(hardwarePicosWaitingForRecoveryMock, times(1)).add(hardwarePicoUri);

        hardwarePicoUtilsMock.close();

    }

    @Test
    void queueSoftwarePico(){

        String softwarePicoUri = UUID.randomUUID().toString();

        when(pairedPicosMock.containsValue(anyString())).thenReturn(false);

        queuingServiceToTest.queueSoftwarePico(softwarePicoUri);

        verify(softwarePicosMock, times(1)).add(softwarePicoUri);

    }

    @Test
    void queueSoftwarePicoDuplicate(){

        String softwarePicoUri = UUID.randomUUID().toString();

        when(pairedPicosMock.containsValue(anyString())).thenReturn(true);

        queuingServiceToTest.queueSoftwarePico(softwarePicoUri);

        verify(softwarePicosMock, never()).add(anyString());

    }

    @Test
    void paringQueuesEmptyBothEmpty(){

        when(hardwarePicosMock.isEmpty()).thenReturn(true);
        when(softwarePicosMock.isEmpty()).thenReturn(true);

        assertTrue(queuingServiceToTest.pairingQueuesEmpty());

    }

    @Test
    void pairingQueuesEmptyHardwarePicoQueueEmpty(){

        when(hardwarePicosMock.isEmpty()).thenReturn(true);
        assertTrue(queuingServiceToTest.pairingQueuesEmpty());

    }

    @Test
    void pairingQueuesEmptySoftwarePicoQueueEmpty(){

        when(hardwarePicosMock.isEmpty()).thenReturn(true);
        assertTrue(queuingServiceToTest.pairingQueuesEmpty());

    }

    @Test
    void pairingQueuesEmptyNoQueueEmpty(){

        assertFalse(queuingServiceToTest.pairingQueuesEmpty());

    }

    @Test
    void getFirstElements(){

        String hardwarePicoUri = "1/abc";
        String softwarePicoUri = UUID.randomUUID().toString();

        when(queuingServiceToTest.pairingQueuesEmpty()).thenReturn(false);

        when(hardwarePicosMock.stream()).thenReturn(Stream.of(hardwarePicoUri));
        when(softwarePicosMock.stream()).thenReturn(Stream.of(softwarePicoUri));

        assertEquals(new Pair<>(hardwarePicoUri, softwarePicoUri), queuingServiceToTest.getFirstElements());

    }

    @Test
    void getFirstElementsForEmptyQueues(){

        when(queuingServiceToTest.pairingQueuesEmpty()).thenReturn(true);

        assertNull(queuingServiceToTest.getFirstElements());

    }

    @Test
    void addPairing(){

        String hardwarePicoUri = "1/abc";
        String softwarePicoUri = UUID.randomUUID().toString();
        Pair<String, String> p = new Pair<>(hardwarePicoUri, softwarePicoUri);

        queuingServiceToTest.addPairing(p);

        verify(pairedPicosMock, times(1)).put(hardwarePicoUri, softwarePicoUri);
        verify(hardwarePicosMock, times(1)).remove(hardwarePicoUri);
        verify(softwarePicosMock, times(1)).remove(softwarePicoUri);

    }

    @Test
    void hasHardwarePicosWaitingForRecovery(){

        when(hardwarePicosWaitingForRecoveryMock.isEmpty()).thenReturn(false);

        assertTrue(queuingServiceToTest.hasHardwarePicosWaitingForRecovery());

    }

    @Test
    void hasHardwarePicosWaitingForRecoveryFalse(){

        when(hardwarePicosWaitingForRecoveryMock.isEmpty()).thenReturn(true);

        assertFalse(queuingServiceToTest.hasHardwarePicosWaitingForRecovery());

    }

    @Test
    void getSoftwarePicoForHardwarePico(){

        String softwarePicoUri = UUID.randomUUID().toString();
        String hardwarePicoUri = "1/abc";

        when(pairedPicosMock.keySet()).thenReturn(Set.of(hardwarePicoUri));
        when(pairedPicosMock.get(hardwarePicoUri)).thenReturn(softwarePicoUri);

        MockedStatic<HardwarePicoUtils> hardwarePicoUtilsMock = mockStatic(HardwarePicoUtils.class);
        hardwarePicoUtilsMock.when(() -> HardwarePicoUtils.equalIds(anyString(), anyString())).thenReturn(true);

        assertEquals(softwarePicoUri, queuingServiceToTest.getSoftwareUriForHardwareUri("1/abc"));

        hardwarePicoUtilsMock.close();

    }

    @Test
    void getSoftwarePicoForHardwarePicoNotFound(){

        String softwarePicoUri = UUID.randomUUID().toString();
        String hardwarePicoUri = "1/abc";

        when(pairedPicosMock.keySet()).thenReturn(Set.of(hardwarePicoUri));
        when(pairedPicosMock.get(hardwarePicoUri)).thenReturn(softwarePicoUri);

        MockedStatic<HardwarePicoUtils> hardwarePicoUtilsMock = mockStatic(HardwarePicoUtils.class);
        hardwarePicoUtilsMock.when(() -> HardwarePicoUtils.equalIds(anyString(), anyString())).thenReturn(false);

        assertNull(queuingServiceToTest.getSoftwareUriForHardwareUri("1/abc"));

        hardwarePicoUtilsMock.close();

    }

    @Test
    void getFirstElementsWaitingForRecovery(){

        String hardwarePicoUri = "1/abc";
        String softwarePicoUri = UUID.randomUUID().toString();

        when(queuingServiceToTest.hasHardwarePicosWaitingForRecovery()).thenReturn(true);
        when(hardwarePicosWaitingForRecoveryMock.stream()).thenReturn(Stream.of(hardwarePicoUri));
        when(queuingServiceToTest.getSoftwareUriForHardwareUri(hardwarePicoUri)).thenReturn(softwarePicoUri);

        assertEquals(new Pair<>(hardwarePicoUri, softwarePicoUri), queuingServiceToTest.getFirstElementsWaitingForRecovery());
        verify(hardwarePicosWaitingForRecoveryMock, times(1)).remove(hardwarePicoUri);

    }

    @Test
    void getFirstElementsWaitingForRecoveryNothingInQueue(){

        when(queuingServiceToTest.hasHardwarePicosWaitingForRecovery()).thenReturn(false);

        assertNull(queuingServiceToTest.getFirstElementsWaitingForRecovery());

    }

}