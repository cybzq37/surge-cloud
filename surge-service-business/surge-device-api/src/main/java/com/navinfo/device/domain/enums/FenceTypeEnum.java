package com.surge.device.domain.enums;

import java.util.Objects;

/**
 * 围栏类型
 */
public enum FenceTypeEnum {
    /**
     * 线
     */
    LINE(1),
    /**
     * 面
     */
    POLYGON(2),
    ;

    private Integer type;

    FenceTypeEnum(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public static FenceTypeEnum valueOf(Integer type) {
        FenceTypeEnum[] fenceTypeEnums = FenceTypeEnum.values();
        for (FenceTypeEnum fenceTypeEnum : fenceTypeEnums) {
            if (Objects.equals(fenceTypeEnum.getType(), type)) {
                return fenceTypeEnum;
            }
        }
        throw new IllegalArgumentException("未知围栏类型");
    }
}
