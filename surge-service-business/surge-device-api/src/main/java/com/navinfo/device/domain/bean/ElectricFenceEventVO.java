package com.surge.device.domain.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ElectricFenceEventVO {

    private Long deviceId;
    private Long fenceId;
    private Integer eventType;
    private Date traceTime;
    private Date startTime;
    private Date endTime;
    private Date lastTraceTime;
    private String deviceName;
    private Long deviceTypeId;
    private String orgId;
    private String fenceName;
    private Integer fenceType;
    private Integer ruleType;
}
