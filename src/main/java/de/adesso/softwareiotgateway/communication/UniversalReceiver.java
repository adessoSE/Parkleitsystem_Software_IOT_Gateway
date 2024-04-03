package de.adesso.softwareiotgateway.communication;

import de.adesso.softwareiotgateway.communication.cloud.CloudReceiver;
import de.adesso.softwareiotgateway.communication.hardware.HardwareReceiver;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class UniversalReceiver implements Receiver{

    private final List<CloudReceiver> cloudReceiverList;
    private final List<HardwareReceiver> hardwareReceiverList;

    @Autowired
    public UniversalReceiver(List<CloudReceiver> cloudReceiverList, List<HardwareReceiver> hardwareReceiverList) {
        this.cloudReceiverList = cloudReceiverList;
        this.hardwareReceiverList = hardwareReceiverList;
    }

    @Override
    public void subscribe(String uri, Consumer<JSONObject> jsonConsumer) {
        for (CloudReceiver c : cloudReceiverList) {
            c.subscribe(uri, jsonConsumer);
        }
        for (HardwareReceiver h : hardwareReceiverList) {
            h.subscribe(uri, jsonConsumer);
        }
    }
}
