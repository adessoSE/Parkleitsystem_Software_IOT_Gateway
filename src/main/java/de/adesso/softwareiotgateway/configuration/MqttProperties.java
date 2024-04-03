package de.adesso.softwareiotgateway.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("software-iot-gateway.mqtt")
public class MqttProperties {

    private String brokerIPAddress = "0.0.0.0";
    private int brokerPort = 1883;
    private String userName = "testUser";
    private String password = "testPwd";
    private String defaultTopic = "software-iot-gateway";

    public String getBrokerIPAddress() {
        return brokerIPAddress;
    }
    public void setBrokerIPAddress(String brokerIPAddress) {
        this.brokerIPAddress = brokerIPAddress;
    }
    public int getBrokerPort() {
        return brokerPort;
    }
    public void setBrokerPort(int brokerPort) {
        this.brokerPort = brokerPort;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getDefaultTopic() {
        return defaultTopic;
    }
    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }
}
