package com.surge.device.mqtt;

import com.surge.device.mqtt.cache.MqttCache;
import com.surge.device.mqtt.cache.MqttKey;
import com.surge.device.mqtt.config.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;
import java.util.UUID;

@Slf4j
public class MqttSubscriber implements Runnable, MqttCallback {

    private MqttProperties mqttProperties;
    private List<String> topics;
    private MqttClient client;

    public MqttSubscriber(MqttProperties mqttProperties, List<String> topics) {
        this.mqttProperties = mqttProperties;
        this.topics = topics;
    }

    @Override
    public void run() {
        while (true) {
            try {
                client = new MqttClient(mqttProperties.getBrokerUrl(),
                        mqttProperties.getClientId() + "_" + UUID.randomUUID().toString().substring(0, 8),
                        new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setAutomaticReconnect(true); // 断线重连
                connOpts.setCleanSession(false); // 保留会话，断线重连后可恢复订阅
                connOpts.setConnectionTimeout(10);
                connOpts.setKeepAliveInterval(20);
                connOpts.setUserName(mqttProperties.getUsername());
                connOpts.setPassword(mqttProperties.getPassword().toCharArray());
                client.setCallback(this);
                client.connect(connOpts);
                for (String topic : topics) {
                    client.subscribe(topic, 0); //
                    log.info("Subscribed to topic: {} on broker: {}", topic, mqttProperties.getBrokerUrl());
                }

                // 保持当前线程，直到连接断开
                while (client.isConnected()) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 异常后暂停一段时间，再尝试重连
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                disconnectClient();
            }
        }
    }

    private void disconnectClient() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }
        } catch (MqttException e) {
            log.error("Disconnect failed: {}", e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.error("Connection lost on broker: " + mqttProperties.getBrokerUrl() + ", reason: " + throwable.getMessage());
        // 如果启用了自动重连，这里通常仅用于日志记录
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        MqttKey mqttKey = new MqttKey(mqttProperties.getProtocol(), mqttProperties.getMfid(), topic);
        MqttCache.MSG_BUCKET.put(mqttKey, new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // 订阅模式下通常不需要实现此方法
    }
}
