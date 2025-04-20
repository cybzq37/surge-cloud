package com.surge.device.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.surge.common.core.domain.BaseEntity;
import com.surge.common.core.mybatis.handler.GeometryTypeHandler;
import com.surge.common.core.mybatis.handler.JsonTypeHandler;
import com.surge.common.core.mybatis.handler.JsonbTypeHandler;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Query;
import com.surge.common.json.view.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import kotlin.collections.ArrayDeque;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "电子围栏表")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("nc_device.electric_fence")
public class ElectricFence extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id", type = "Long")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "组织机构Id", type = "String")
    @NotBlank(message = "组织机构Id")
    private String orgId;

    @Schema(description = "围栏名称", type = "String")
    @NotBlank(message = "围栏名称不能为空")
    private String name;

    @Schema(description = "围栏类型: 1=线，2=面", type = "String")
    @NotBlank(message = "围栏类型不能为空")
    private Integer fenceType;

    @Schema(description = "围栏范围，geojson格式", type = "String")
    @TableField(typeHandler = GeometryTypeHandler.class)
    private String geom;

    @JsonView({Create.class, Query.class, Update.class})
    @TableField(exist = false, typeHandler = JsonTypeHandler.class)
    @Schema(description = "几何数据", type = "Json")
    private JsonNode geoJson;


    @Schema(description = "围栏规则：1=轨迹偏离，2=禁止进入，3=禁止离开", type = "String")
    @NotBlank(message = "围栏规则不能为空")
    private Integer ruleType;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "时间范围规则", type = "Json")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JsonNode timeRange;

    @Schema(description = "zoom级别", type = "Integer")
    private Integer zoom;

    @Schema(description = "封面", type = "String")
    private String cover;

    @Schema(description = "围栏状态: 0正常, 1停用", type = "String")
    private Integer status;

    @Schema(description = "删除标志: 0代表存在, 1代表删除）", type = "String")
    private Integer delFlag;

    private String remark;

    @JsonIgnore
    @JsonProperty
    public Integer getDelFlag() {
        return delFlag;
    }

    @TableField(exist = false)
    public List<DeviceInstance> deviceInstances = new ArrayList<>();

}
