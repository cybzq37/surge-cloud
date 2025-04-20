package com.surge.common.core.exception;

import com.surge.common.core.result.ErrorGlobal;
import com.surge.common.core.result.IError;
import lombok.Getter;

/**
 * 基础异常
 *
 * @author lichunqing
 */
@Getter
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private IError error;

    public ServiceException(int code, String message) {
        this.code = code;
        this.message = message;
        this.error = null;
    }

    public ServiceException(String message) {
        this(ErrorGlobal.failed.getCode(), message);
    }

    public ServiceException(IError error) {
        this(error.getCode(), error.getMessage());
    }

    @Override
    public String getMessage() {
        String message = null;
        if (error != null) {
            message = MessageUtils.message(error.name(), null);
        }
        if (message == null) {
            message = this.message;
        }
        return message;
    }

}