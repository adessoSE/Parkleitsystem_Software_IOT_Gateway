package de.adesso.softwareiotgateway.messageHandling.handler;

import de.adesso.softwareiotgateway.messageHandling.message.Message;

public interface MessageHandler {

    <T extends Message> void handle(T message);

    <T extends Message> boolean supports(T message);

}
