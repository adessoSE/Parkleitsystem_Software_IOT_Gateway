package de.adesso.softwareiotgateway.messageHandling;

import de.adesso.softwareiotgateway.messageHandling.message.ConnectionInfoMessage;
import de.adesso.softwareiotgateway.messageHandling.message.HardwarePicoRegistrationMessage;
import de.adesso.softwareiotgateway.messageHandling.message.Message;
import de.adesso.softwareiotgateway.messageHandling.message.SoftwarePicoRegistrationMessage;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static de.adesso.softwareiotgateway.messageHandling.MessageFactory.fromJson;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageFactoryTest {

    @Test
    void fromJsonSPRegister(){

        String softwarePicoUri = UUID.randomUUID().toString();

        JSONObject jsonMessageMock = mock(JSONObject.class);
        when(jsonMessageMock.getString("messageType")).thenReturn("register_sp");
        when(jsonMessageMock.getString("uri")).thenReturn(softwarePicoUri);

        Message message = fromJson(jsonMessageMock);

        assertInstanceOf(SoftwarePicoRegistrationMessage.class, message);
        assertEquals(softwarePicoUri, ((SoftwarePicoRegistrationMessage) message).softwarePicoUri());
    }

    @Test
    void fromJsonHPRegister(){

        String hardwarePicoUri = "1/abc";

        JSONObject jsonMessageMock = mock(JSONObject.class);
        when(jsonMessageMock.getString("messageType")).thenReturn("register_hp");
        when(jsonMessageMock.getString("uri")).thenReturn(hardwarePicoUri);

        Message message = fromJson(jsonMessageMock);

        assertInstanceOf(HardwarePicoRegistrationMessage.class, message);
        assertEquals(hardwarePicoUri, ((HardwarePicoRegistrationMessage) message).hardwarePicoUri());
    }

    @Test
    void fromJsonConnectionInfo(){

        String hardwarePicoUri = "1/abc";
        String softwarePicoUri = UUID.randomUUID().toString();
        String connectionStatus = "OK";

        JSONObject jsonMessageMock = mock(JSONObject.class);
        when(jsonMessageMock.getString("messageType")).thenReturn("connection_info");
        when(jsonMessageMock.getString("hardwarePicoUri")).thenReturn(hardwarePicoUri);
        when(jsonMessageMock.getString("softwarePicoUri")).thenReturn(softwarePicoUri);
        when(jsonMessageMock.getString("connectionStatus")).thenReturn(connectionStatus);

        Message message = fromJson(jsonMessageMock);

        assertInstanceOf(ConnectionInfoMessage.class, message);
        assertEquals(hardwarePicoUri, ((ConnectionInfoMessage) message).hardwarePicoUri());
        assertEquals(softwarePicoUri, ((ConnectionInfoMessage) message).softwarePicoUri());
        assertEquals(connectionStatus, ((ConnectionInfoMessage) message).connectionStatus());
    }


}