package com.surge.device.api;

import com.surge.device.domain.kafka.KafkaRtpd;

import java.util.List;

public interface RemoteKafkaRtpdSyncService {
    /**
     * 设备定位信息同步
     */
    void syncKafkaRtpdList(List<KafkaRtpd> rtpdList);
}
