package com.surge.device.domain.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class KafkaSbtz implements Serializable {

    static final long serialVersionUID = 1L;

    private String mfid;    //厂商Id

    private String epid;    //设备Id

    private String deviceName; // 设备名称

    private String deviceModel; // 设备型号

    private String deviceType; // 设备类型

    private String ownerName;

    private String ownerId;

    private int ownerIdType;

    private int region;

    private int operation;

    /**
     * 设备扩展信息
     */
    private JsonNode extend;

}