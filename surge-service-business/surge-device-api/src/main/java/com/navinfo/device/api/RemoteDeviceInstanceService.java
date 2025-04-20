package com.surge.device.api;

import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.kafka.KafkaRtpd;
import com.surge.device.domain.kafka.KafkaSbtz;

import java.util.List;

public interface RemoteDeviceInstanceService {

    /**
     * 同步海康设备用
     * @param deviceTypeCode
     * @param deviceInstance
     */
    void saveOrUpdateByEpid(String deviceTypeCode, DeviceInstance deviceInstance);

    void saveOrUpdate(DeviceInstance deviceInstance);

}
