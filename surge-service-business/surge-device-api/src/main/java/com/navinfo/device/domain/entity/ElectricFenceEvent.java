package com.surge.device.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Tag(name = "电子围栏事件表")
@Data
@NoArgsConstructor
@EqualsAndHashCode
@TableName("nc_device.electric_fence_event")
public class ElectricFenceEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id", type = "Long")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "围栏id", type = "Long")
    private Long fenceId;

    @Schema(description = "设备id", type = "Long")
    private Long deviceId;

    @Schema(description = "组织机构id", type = "Long")
    private String orgId;

    @Schema(description = "事件类型：1=轨迹偏离，2=禁止进入，3=禁止离开", type = "String")
    @NotBlank(message = "事件类型不能为空")
    private Integer eventType;

    @Schema(description = "轨迹上报的时间", type = "timestamp")
    private Date traceTime;

    @Schema(description = "事件开始时间", type = "timestamp")
    private Date startTime;

    @Schema(description = "事件结束时间", type = "timestamp")
    private Date endTime;

    @Schema(description = "告警状态：1=告警中，2=已处理，3=已解除", type = "String")
    private Integer status;

    @Schema(description = "删除标志（0代表存在 1代表禁用 2代表删除）", type = "String")
    @TableLogic
    private Integer delFlag;

    @JsonIgnore
    @JsonProperty
    public Integer getDelFlag() {
        return delFlag;
    }

    @Schema(description = "最近一次轨迹上报的时间", type = "timestamp")
    private Date lastTraceTime;

}
