package com.surge.device.domain.bean;

import lombok.Data;

@Data
public class RootRegionData {

    /**
     * 区域唯一标识码
     */
    private String indexCode;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 父区域唯一标识码
     */
    private String parentIndexCode;

    /**
     * 树编号
     */
    private String treeCode;
}
