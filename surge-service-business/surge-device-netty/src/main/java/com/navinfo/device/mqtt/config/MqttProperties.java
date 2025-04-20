package com.surge.device.mqtt.config;

import lombok.Data;

@Data
public class MqttProperties {

    private Long mfid;
    private String protocol;
    private String brokerUrl;
    private String clientId;
    private String username;
    private String password;

}
