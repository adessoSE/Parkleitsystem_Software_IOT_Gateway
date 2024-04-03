package de.adesso.softwareiotgateway.communication;

import org.json.JSONObject;

public interface Sender {

    void send(String uri, JSONObject j);

}
