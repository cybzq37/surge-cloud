package com.surge.common.core.result;

public interface IError {

    int getCode();
    String name();
    String getMessage();
}
