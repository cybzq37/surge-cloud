package com.surge.map.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Query;
import com.surge.common.json.view.Update;
import com.surge.common.core.mybatis.handler.GeometryTypeHandler;
import com.surge.common.core.mybatis.handler.JsonTypeHandler;
import com.surge.common.core.mybatis.handler.JsonbTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "图层数据信息")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@TableName("nc_map.data_entry_set")
public class DataEntrySet implements Serializable {

    static final long serialVersionUID = 1L;

    @JsonView({Query.class, Update.class})
    @Schema(description = "图层数据ID", type = "Long")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "图层Id", type = "Long")
    @NotNull(message = "图层Id不能为空")
    private Long dataEntryId;

    @JsonView({Create.class, Query.class, Update.class})
    @TableField(exist = false, typeHandler = JsonTypeHandler.class)
    @Schema(description = "几何数据", type = "Json")
    private JsonNode geoJson;

//    @JsonView({Query.class})
    @TableField(typeHandler = GeometryTypeHandler.class)
    private String geom;

    @JsonView({Create.class, Query.class, Update.class})
    @Schema(description = "属性数据", type = "Json")
    @TableField(typeHandler = JsonbTypeHandler.class)
    private JsonNode fieldInfo;

}