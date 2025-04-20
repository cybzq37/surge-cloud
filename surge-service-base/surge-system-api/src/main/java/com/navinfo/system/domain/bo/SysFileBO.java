package com.surge.system.domain.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件信息
 *
 * @author lichunqing
 */
@Data
public class SysFileBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * oss主键
     */
    private Long ossId;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件地址
     */
    private String url;

}
