package com.surge.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态
 *
 * @author lichunqing
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    OK("0", "正常"),
    DISABLE("1", "冻结"),
    DELETED("2", "删除");

    private final String code;
    private final String info;

}
