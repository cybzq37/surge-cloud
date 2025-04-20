package com.surge.common.core.result;

import com.baomidou.mybatisplus.core.injector.methods.Delete;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Query;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author lichunqing
 */
@Data
@NoArgsConstructor
@JsonView({Create.class, Query.class, Delete.class})
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

    public static <T> R<T> ok() {
        return result(ErrorGlobal.success.getCode(), ErrorGlobal.success.getMessage(), null);
    }

    public static <T> R<T> ok(T data) {
        return result(ErrorGlobal.success.getCode(), ErrorGlobal.success.getMessage(), data);
    }

    public static <T> R<T> ok(String msg) {
        return result(ErrorGlobal.success.getCode(), msg, null);
    }

    public static <T> R<T> ok(String msg, T data) {
        return result(ErrorGlobal.success.getCode(), msg, data);
    }

    public static <T> R<T> fail() {
        return result(ErrorGlobal.failed.getCode(), ErrorGlobal.failed.getMessage(), null);
    }

    public static <T> R<T> fail(String msg) {
        return result(ErrorGlobal.failed.getCode(), msg, null);
    }

    public static <T> R<T> fail(T data) {
        return result(ErrorGlobal.failed.getCode(), ErrorGlobal.failed.getMessage(), data);
    }

    public static <T> R<T> fail(String msg, T data) {
        return result(ErrorGlobal.failed.getCode(), msg, data);
    }

    public static <T> R<T> fail(int code, String msg) {
        return result(code, msg, null);
    }

    public static <T> R<T> fail(IError error) {
        return result(error.getCode(), error.getMessage(), null);
    }

    private static <T> R<T> result(int code, String msg, T data) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }


}
