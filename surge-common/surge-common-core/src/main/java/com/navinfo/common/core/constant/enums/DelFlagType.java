package com.surge.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 * 针对一套 用户体系
 *
 * @author lichunqing
 */
@Getter
@AllArgsConstructor
public enum DelFlagType {

    /**
     * 未删除
     */
    NOT_DELETED("0"),

    /**
     * 已删除
     */
    DELETED("1"),

    ;

    private final String flag;

    public String getFlag() {
        return flag;
    }
}
