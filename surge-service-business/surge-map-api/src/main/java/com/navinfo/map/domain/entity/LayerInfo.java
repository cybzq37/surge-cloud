package com.surge.map.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonRawValue;
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
import java.util.HashSet;
import java.util.Set;

@Tag(name = "图层信息")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName(value = "nc_map.layer_info", autoResultMap = true)
public class LayerInfo extends BaseEntity {

    @JsonView({Query.class, Update.class})
    @Schema(description = "Id", type = "Long")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "父级Id", type = "Long")
    private Long pid;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "图层名称", type = "String")
    @NotBlank(message = "图层名称不能为空")
    private String name;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "数据类型：point-点，linestring-线, polygon-面", type = "String")
    @NotBlank(message = "数据类型不能为空")
    private String dataType;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "图层数据样式定义", type = "JSON")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JsonNode dataStyle;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "图层数据属性定义", type = "JSON")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JsonNode dataSchema;

    @JsonView({Create.class, Query.class, Update.class})
    @JsonRawValue
    @Schema(description = "数据边界", type = "Text")
    private String dataExtent;

    @JsonView({Create.class, Query.class, Update.class})
    @JsonRawValue
    @Schema(description = "数据中心点", type = "Text")
    private String dataCentral;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "图层排序", type = "Integer")
    private Integer sort;

    @JsonView({Query.class, Update.class})
    @Schema(description = "是否发布: 0-草稿 1-发布", type = "Long")
    private Integer releaseFlag;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "组织机构Id集合", type = "Text")
    private String orgId;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "组织机构Id集合", type = "List")
    @TableField(exist = false)
    private Set<Long> orgIds = new HashSet<>();

}
