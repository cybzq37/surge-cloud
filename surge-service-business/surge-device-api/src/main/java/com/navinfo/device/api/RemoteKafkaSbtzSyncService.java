package com.surge.device.api;

import com.surge.device.domain.kafka.KafkaSbtz;

import java.util.List;

public interface RemoteKafkaSbtzSyncService {


    /**
     * 设备台账信息同步
     */
    void syncKafkaSbtzList(List<KafkaSbtz> sbtzList);

}
