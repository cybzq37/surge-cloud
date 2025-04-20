package com.surge.device.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.surge.common.core.mybatis.handler.GeometryTypeHandler;
import com.surge.common.core.mybatis.handler.JsonTypeHandler;
import com.surge.common.core.mybatis.handler.JsonbTypeHandler;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Query;
import com.surge.common.json.view.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Tag(name = "设备实例")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@TableName("nc_device.device_instance")
public class DeviceInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonView({Query.class, Update.class})
    @Schema(description = "平台生成的设备唯一ID", type = "Long")
    @TableId(value = "tid", type = IdType.ASSIGN_ID)
    private Long tid;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "厂商Id，device_manufacturer表mfid", type = "Long")
    private Long mfid;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "厂商内部设备编码Id", type = "String")
    private String epid;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "设备名称", type = "String")
    private String name;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "设备型号", type = "String")
    private String model;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "设备类型Id", type = "Long")
    private Long typeId;

    @TableField(exist = false)
    private String typeCode;

    // 设备类型名称
    @TableField(exist = false)
    private String typeName;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "属性数据", type = "Json")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JsonNode fieldInfo;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "位置信息更新事件", type = "Date")
    private Date lastPosTime;

    @TableField(typeHandler = GeometryTypeHandler.class)
    private String lastPosGeom;

    @JsonView({Create.class, Query.class, Update.class})
    @TableField(exist = false, typeHandler = JsonTypeHandler.class)
    @Schema(description = "几何数据", type = "Json")
    private JsonNode lastPosGeoJson;

    @JsonView({Query.class, Update.class})
    @Schema(description = "设备状态", type = "Integer")
    private Integer status;

    @JsonView({Query.class, Update.class})
    @Schema(description = "设备是否可见", type = "Integer")
    private Integer visible;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "组织机构Id集合", type = "Text")
    private String orgId;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "设备所属⽤⼾名称", type = "String")
    private String ownerName;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "设备所属⽤⼾标识", type = "String")
    private String ownerId;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "身份编码类型：1：⾝份证 2：⼿机号 3：⼚商平台账⼾ 4：其他", type = "String")
    private Integer ownerIdType;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "更新时间", type = "Date")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "0:未删除 1：已删除", type = "Integer")
    private Integer deleteFlag;

}
