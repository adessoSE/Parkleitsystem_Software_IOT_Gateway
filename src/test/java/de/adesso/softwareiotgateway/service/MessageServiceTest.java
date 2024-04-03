package de.adesso.softwareiotgateway.service;

import de.adesso.softwareiotgateway.communication.Receiver;
import de.adesso.softwareiotgateway.messageHandling.MessageFactory;
import de.adesso.softwareiotgateway.messageHandling.MessageType;
import de.adesso.softwareiotgateway.messageHandling.handler.MessageHandler;
import de.adesso.softwareiotgateway.messageHandling.message.Message;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    static List<MessageHandler> messageHandlerListMock;
    static Receiver receiverMock;
    static MessageService messageServiceToTest;

    @BeforeAll
    @SuppressWarnings("unchecked")
    static void init(){
        messageHandlerListMock = (List<MessageHandler>) mock(List.class);
        receiverMock = mock(Receiver.class);
        messageServiceToTest = spy(new MessageService(receiverMock, messageHandlerListMock));
    }

    @BeforeEach
    void resetMocks(){
        reset(messageServiceToTest, receiverMock, messageHandlerListMock);
    }

    @Test
    void subscribeToInbound(){

        doNothing().when(receiverMock).subscribe(any(), any());

        messageServiceToTest.subscribeToInbound();

        verify(receiverMock, times(1)).subscribe(eq("software-iot-gateway"), any());

    }

    @Test
    void findSupportingMessageHandler(){

        Message m = mock(Message.class);
        MessageHandler h = mock(MessageHandler.class);
        when(messageHandlerListMock.stream()).thenReturn(Stream.of(h));
        when(h.supports(m)).thenReturn(true);

        MessageHandler k = messageServiceToTest.findSupportingMessageHandler(m);

        assertEquals(h, k);
    }

    @Test
    void findSupportingMessageHandlerNotFound(){

        Message m = mock(Message.class);

        when(messageHandlerListMock.stream()).thenReturn(Stream.empty());

        assertThrows(NoSuchElementException.class, () -> messageServiceToTest.findSupportingMessageHandler(m));
    }

    @Test
    void handle(){

        Message m = mock(Message.class);
        MessageHandler h = mock(MessageHandler.class);

        MockedStatic<MessageFactory> messageFactoryMock = mockStatic(MessageFactory.class);
        messageFactoryMock.when(() -> MessageFactory.fromJson(any())).thenReturn(m);

        doReturn(h).when(messageServiceToTest).findSupportingMessageHandler(m);


        messageServiceToTest.handle(new JSONObject());

        verify(h, times(1)).handle(m);

        messageFactoryMock.close();
    }

    @Test
    void handleInvalidMessage(){

        JSONObject jsonMessage = new JSONObject();

        MockedStatic<MessageFactory> messageFactoryMock = mockStatic(MessageFactory.class);
        messageFactoryMock.when(() -> MessageFactory.fromJson(any())).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> messageServiceToTest.handle(jsonMessage));

        messageFactoryMock.close();
    }

}