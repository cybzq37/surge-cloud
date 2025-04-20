package com.surge.map.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.surge.common.core.domain.BaseEntity;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Query;
import com.surge.common.json.view.Update;
import com.surge.common.core.mybatis.handler.JsonbTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tag(name = "导入数据信息信息")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName(value = "nc_map.data_entry", autoResultMap = true)
public class DataEntry extends BaseEntity {


    @JsonView({Query.class, Update.class})
    @Schema(description = "图层数据ID", type = "Long")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "数据名称", type = "String")
    @NotBlank(message = "数据名称不能为空")
    private String name;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "数据类型", type = "Integer")
    @NotBlank(message = "数据类型不能为空")
    private Integer dataType;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "数据源类型 1:数据库 2:离线文件 3:远程文件", type = "Integer")
    @NotBlank(message = "数据源类型不能为空")
    private Integer sourceType;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "数据源Id", type = "Integer")
    private Long sourceId;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "文件类型", type = "Integer")
    private Integer fileType;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "数据字段类型", type = "Json")
    @NotBlank(message = "数据字段类型")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JsonNode dataSchema;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "数据存储信息", type = "Json")
    @NotBlank(message = "数据存储信息")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JsonNode dataAccess;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "数据存储信息", type = "Json")
    @NotBlank(message = "数据存储信息")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JsonNode dataStyle;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "数据边界", type = "String")
    private String dataExtent;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "数据中心点", type = "String")
    private String dataCentral;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "epsg编码", type = "Integer")
    private Integer epsg;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "坐标系", type = "String")
    private String coordinateSystem;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "错误日志", type = "String")
    private String errorLog;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "状态 0：完成  1：进行中  2:失败", type = "Integer")
    private Integer status;

    private Integer delFlag;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "备注", type = "String")
    private String remark;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "组织机构Id集合", type = "Text")
    private String orgId;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "组织机构Id集合", type = "List")
    @TableField(exist = false)
    private Set<Long> orgIds = new HashSet<>();


    @JsonView({Query.class})
    @Schema(description = "数据集", type = "List")
    @TableField(exist = false)
    private List<DataEntrySet> dataEntrySets = new ArrayList<>();
}