package com.surge.system.error;

import com.surge.common.core.result.IError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorSystem implements IError {


    user_not_exist(10001, "用户不存在"),
    user_blocked(10002, "用户被禁用"),
    ;

    private final int code;
    private final String message;

}
