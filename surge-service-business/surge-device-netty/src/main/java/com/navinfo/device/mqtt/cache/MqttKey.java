package com.surge.device.mqtt.cache;

import lombok.Data;

@Data
public class MqttKey {

    private String protocol;
    private Long mfid;
    private String topic;

    public MqttKey(String protocol, Long mfid, String topic) {
        this.protocol = protocol;
        this.mfid = mfid;
        this.topic = topic;
    }
}
