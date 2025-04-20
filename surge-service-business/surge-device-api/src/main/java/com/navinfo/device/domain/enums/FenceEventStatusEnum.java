package com.surge.device.domain.enums;

import lombok.Getter;

/**
 * 围栏规则
 */
@Getter
public enum FenceEventStatusEnum {

    /**
     * 告警中
     */
    ALARMING(1),
    /**
     * 已处理（人工处理的告警信息）
     */
    PROCESSED(2),
    /**
     * 已解除（系统自动处理的告警信息）
     */
     CANCELED(3),
    ;

    private Integer code;

    FenceEventStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
