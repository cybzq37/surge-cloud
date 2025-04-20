package com.surge.device.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Tag(name = "围栏和设备关联关系表")
@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("nc_device.electric_fence_device")
public class ElectricFenceDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id", type = "Long")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "围栏id", type = "Long")
    @TableField(value = "fence_id")
    private Long fenceId;

    @Schema(description = "设备id", type = "Long")
    @TableField(value = "device_id")
    private Long deviceId;

}