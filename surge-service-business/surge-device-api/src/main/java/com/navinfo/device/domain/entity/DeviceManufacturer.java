package com.surge.device.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.surge.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Tag(name = "设备厂商")
@Data
@EqualsAndHashCode
@TableName("nc_device.device_manufacturer")
public class DeviceManufacturer extends BaseEntity {

    @Schema(description = "制造商唯一mifd", type = "Long")
    @TableId(value = "mfid", type = IdType.ASSIGN_ID)
    private Long mfid;

    @Schema(description = "组织机构Id", type = "String")
    private String orgId;

    @TableField(exist = false)
    private String orgName;

    @Schema(description = "制造商编码", type = "String")
    private String code;

    @Schema(description = "制造商名称", type = "String")
    private String name;

    @Schema(description = "联系人", type = "String")
    private String contactName;

    @Schema(description = "联系方式", type = "String")
    private String contactPhone;

    @Schema(description = "排序", type = "String")
    private Integer sort;

    private String remark;
}
