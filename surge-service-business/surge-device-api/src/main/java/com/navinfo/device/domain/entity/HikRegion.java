package com.surge.device.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Tag(name = "组织区域")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@TableName("nc_device.hik_region")
public class HikRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "index_code", type = IdType.ASSIGN_ID)
    private String indexCode;
    private String name;
    private String regionPath;
    private String parentIndexCode;
    private Integer available;
    private Integer leaf;
    private Integer cascadeType;
    private String cascadeCode;
    private Integer catalogType;
    private String externalIndexCode;
    private String parentExternalIndexCode;
    private Integer sort;
    private Date createTime;
    private Date updateTime;
    // 系统组织机构Id
    private String orgId;

}