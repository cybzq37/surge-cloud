package com.surge.device.mqtt.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.surge.common.core.utils.JsonUtils;
import com.surge.device.api.RemoteKafkaRtpdSyncService;
import com.surge.device.api.RemoteTraceDataProcessorApi;
import com.surge.device.domain.kafka.KafkaRtpd;
import com.surge.device.mqtt.cache.MqttCache;
import com.surge.device.mqtt.cache.MqttKey;
import com.surge.device.mqtt.model.LtPosData;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MqttDataSyncTask {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @DubboReference
    public RemoteKafkaRtpdSyncService remoteKafkaRtpdSyncService;

    /**
     * 循环mqtt bucket 每分钟执行一次，推送到kafka
     * @throws JsonProcessingException
     */
    @Scheduled(cron="*/10 * * * * *")
    public void mqttDataSyncTask() throws JsonProcessingException {
        List<MqttKey> keysToRemove = new ArrayList<>();
        MqttCache.MSG_BUCKET.forEach((key, value) -> {
            LtPosData ltPosData = JsonUtils.parseObject(value, LtPosData.class);
            List<KafkaRtpd> kafkaRtpdList = new ArrayList<>();
            ltPosData.getData().forEach(data -> {
                KafkaRtpd kafkaRtpd = new KafkaRtpd();
                kafkaRtpd.setMfid(key.getMfid() + "");
                kafkaRtpd.setEpid(ltPosData.getDevNo());
                kafkaRtpd.setLon(data.getLon());
                kafkaRtpd.setLat(data.getLat());
                kafkaRtpd.setSpeed(data.getSpeed());
                kafkaRtpd.setCog(data.getHeading());
                kafkaRtpd.setElevation(data.getAlt());
                kafkaRtpd.setTimestamp(data.getTime());
                kafkaRtpd.setStatus(1); // 在线
                kafkaRtpdList.add(kafkaRtpd);
                keysToRemove.add(key);
            });
            remoteKafkaRtpdSyncService.syncKafkaRtpdList(kafkaRtpdList);
            keysToRemove.forEach(MqttCache.MSG_BUCKET::remove); // 删除数据
        });
    }

}
