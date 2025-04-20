package com.surge.device.domain.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * 围栏规则
 */
@Getter
public enum FenceRuleEnum {


    /**
     * 禁止进入
     */
    NO_ENTRY(1, FenceTypeEnum.POLYGON),

    /**
     * 禁止离开
     */
    NO_LEAVING(2, FenceTypeEnum.POLYGON),

    /**
     * 轨迹偏离
     */
    TRAJECTORY_DEVIATION(3, FenceTypeEnum.LINE),
    ;

    private Integer code;

    private FenceTypeEnum fenceTypeEnum;

    FenceRuleEnum(Integer code, FenceTypeEnum fenceTypeEnum) {
        this.code = code;
        this.fenceTypeEnum = fenceTypeEnum;
    }

    public static FenceRuleEnum valueOf(Integer code) {
        FenceRuleEnum[] fenceRuleEnums = FenceRuleEnum.values();
        for (FenceRuleEnum fenceRuleEnum : fenceRuleEnums) {
            if (Objects.equals(fenceRuleEnum.getCode(), code)) {
                return fenceRuleEnum;
            }
        }
        throw new IllegalArgumentException("未知围栏规则类型");
    }

}
