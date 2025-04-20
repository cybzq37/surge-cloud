package com.surge.device.domain.bean;

import lombok.Data;

@Data
public class JsonRootBean {

    /**
     * 返回码
     * 0: 成功
     */
    private String code;

    /**
     * 接口执行情况说明信息
     */
    private String msg;

    /**
     * 信息结构体
     */
    private Object data;
}
