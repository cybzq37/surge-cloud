package com.surge.device.domain.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KafkaRtpd implements Serializable {

    static final long serialVersionUID = 1L;

    private String mfid;    //厂商Id
    private String epid;    //设备Id

    private Integer accuracy;
    private Double elevation;
    private Integer source;
    private Double speed;   //速度
    private Double cog;     // 航向角

    private Long timestamp;

    private Double lon;
    private Double lat;

    private Integer status; //1:在线 2:离线

}
