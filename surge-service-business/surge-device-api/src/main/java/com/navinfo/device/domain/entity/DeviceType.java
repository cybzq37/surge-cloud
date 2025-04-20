package com.surge.device.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;
import com.surge.common.core.mybatis.handler.JsonbTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Tag(name = "设备类型")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@TableName("nc_device.device_type")
public class DeviceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id", type = "Long")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "设备类型目录(一级分类)", type = "String")
    private String catelog;

    @Schema(description = "设备类型编码, 唯一(二级分类)", type = "String")
    private String code;

    @Schema(description = "设备类型名称", type = "String")
    private String name;

    @Schema(description = "设备类型型号", type = "String")
    private String model;

    @Schema(description = "显示顺序", type = "Integer")
    private Integer sort;

    @Schema(description = "设备类型元配置信息(Json Schema)", type = "JSON")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JsonNode fieldSchema;

    @Schema(description = "图层数据样式定义", type = "JSON")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JsonNode dataStyle;

}