package com.surge.common.core.result;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorGlobal implements IError {

    success(0, "success"),
    failed(99999, "failed");

    private final int code;
    private final String message;

}
