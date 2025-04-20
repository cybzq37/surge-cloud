package com.surge.device.kafka.consumer;

import com.surge.common.core.utils.JsonUtils;
import com.surge.device.api.RemoteKafkaRtpdSyncService;
import com.surge.device.domain.kafka.KafkaRtpd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RtpdAckConsumer {

    @DubboReference
    public RemoteKafkaRtpdSyncService remoteKafkaRtpdSyncService;

    @KafkaListener(
            topics = "t_gisportal_rtpd",
            groupId = "g_gisportal",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenWithManualAck(@Payload String message) {
        try {
            List<KafkaRtpd> kafkaRtpdList = JsonUtils.parseArray(message, KafkaRtpd.class);
            remoteKafkaRtpdSyncService.syncKafkaRtpdList(kafkaRtpdList);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kafka设备实时定位信息同步错误", e.getMessage());
        }
    }

}
