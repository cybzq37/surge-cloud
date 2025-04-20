package com.surge.device.domain.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 区域、监控信息
 */
@Data
public class RegionIndexBean {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "区域唯一标识码")
    private String indexCode;

    @Schema(description = "区域名称")
    private String name;

    @Schema(description = "区域完整路径")
    private String regionPath;

    @Schema(description = "父区域标识码")
    private String parentIndexCode;

    @Schema(description = "是否有权限操作")
    private boolean available;

    /**
     * true:是叶子节点，表示该区域下面未挂区域 false:不是叶子节点，表示该区域下面挂有区域
     */
    @Schema(description = "是否叶子节点")
    private boolean leaf;

    /**
     * 区域标识
     * 0：本级
     * 1：级联
     * 2：混合，下级推送给上级的本级点（杭州本级有滨江，然后下级滨江又把自己推送上来了，滨江是混合区域节点）
     * 入参cascadeFlag与返回值对应：
     * cascadeFlag=0：返回0、1、2
     * cascadeFlag=1：返回0、2 cascadeFlag=2：返回1、2
     */
    @Schema(description = "区域标识")
    private int cascadeType;

    /**
     * 级联平台标识，多个级联编号以@分隔，本级区域默认值“0”
     */
    @Schema(description = "级联平台标识")
    private String cascadeCode;

    @Schema(description = "区域类型")
    private int catalogType;

    @Schema(description = "外码")
    private String externalIndexCode;

    @Schema(description = "父外码")
    private String parentExternalIndexCode;

    @Schema(description = "同级区域顺序")
    private int sort;

    @Schema(description = "所属平台ID")
    private String isc;

    private Date createTime;

    private Date updateTime;


}
