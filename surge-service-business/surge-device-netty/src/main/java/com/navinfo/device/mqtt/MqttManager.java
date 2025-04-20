package com.surge.device.mqtt;

import com.surge.device.mqtt.config.MqttConfiguration;
import com.surge.device.mqtt.config.MqttProperties;
import com.surge.station.api.RemoteSbcEquipService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class MqttManager {

    @Autowired
    private MqttConfiguration mqttConfiguration;

    @DubboReference
    private RemoteSbcEquipService remoteSbcEquipService;

    private ExecutorService executor = Executors.newCachedThreadPool();

    @PostConstruct
    public void start() {
        log.info("Starting mqtt manager");
        MqttProperties ljProjerties = mqttConfiguration.getLj();
        // 龙集招商执行
        if(ljProjerties != null) {
            List<String> rtkIds = remoteSbcEquipService.queryMqttRtkIdList();
            List<String> topics = new ArrayList<>();
            for (String rtkId : rtkIds) {
                String topic = "longtan/rtk/" + rtkId + "/up/location";
                topics.add(topic);
            }
            MqttSubscriber subscriber = new MqttSubscriber(ljProjerties, topics);
            executor.submit(subscriber);
        }
    }
}
