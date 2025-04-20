package com.surge.device.kafka.consumer;

import com.surge.common.core.utils.JsonUtils;
import com.surge.device.api.RemoteDeviceInstanceService;
import com.surge.device.api.RemoteKafkaSbtzSyncService;
import com.surge.device.domain.kafka.KafkaSbtz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class SbtzAckConsumer {

    @DubboReference
    public RemoteDeviceInstanceService remoteDeviceInstanceService;

    @DubboReference
    public RemoteKafkaSbtzSyncService remoteKafkaSbtzSyncService;

    @KafkaListener(
            topics = "t_gisportal_sbtz",
            groupId = "g_gisportal",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenWithManualAck(@Payload String message) {
        try {
            List<KafkaSbtz> list = JsonUtils.parseArray(message, KafkaSbtz.class);
            remoteKafkaSbtzSyncService.syncKafkaSbtzList(list);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kafka设备台账同步错误", e.getMessage());
        }
    }

}
