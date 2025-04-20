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

@Tag(name = "摄像头")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@TableName("nc_device.hik_camera")
public class HikCamera implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "index_code", type = IdType.ASSIGN_ID)
    private String indexCode;
    private String name;
    private String regionIndexCode;
    private String regionName;
    private String regionPath;
    private String regionPathName;
    private Integer cameraType;
    private String cameraTypeName;
    private String parentIndexCode;
    private String externalIndexCode;
    private String capability;
    private String decodeTag;
    private String comId;
    private String resourceType;
    private String cameraRelateTalk;
    private Integer cascadeType;

}