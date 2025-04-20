package com.surge.common.core.constant.enums;

import com.surge.common.core.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 * 针对多套 用户体系
 *
 * @author lichunqing
 */
@Getter
@AllArgsConstructor
public enum UserType {

    /**
     * pc端
     */
    PC("pc"),

    /**
     * app端
     */
    APP("app");

    private final String userType;

    public static UserType getUserType(String str) {
        for (UserType value : values()) {
            if (StringUtils.contains(str, value.getUserType())) {
                return value;
            }
        }
        throw new RuntimeException("'UserType' not found By " + str);
    }
}
