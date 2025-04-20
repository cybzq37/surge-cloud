package com.surge.device.mqtt.cache;

import java.util.concurrent.ConcurrentHashMap;

public class MqttCache {

    public static ConcurrentHashMap<MqttKey, String> MSG_BUCKET = new ConcurrentHashMap<>();

}