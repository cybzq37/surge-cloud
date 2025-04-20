package com.surge.device.api;

import com.surge.device.domain.kafka.KafkaRtpd;

import java.util.List;

/**
 * 轨迹数据消费接口
 *
 * @author lichunqing
 */
public interface RemoteTraceDataProcessorApi {

    /**
     * 消费轨迹数据
     * @param kafkaRtpdList
     */
    void process(List<KafkaRtpd> kafkaRtpdList);


}
