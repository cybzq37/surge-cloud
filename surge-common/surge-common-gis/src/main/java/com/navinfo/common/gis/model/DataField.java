package com.surge.common.gis.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DataField {

    @Schema(description = "属性标识", required = true)
    public String name;

    @Schema(description = "属性名称", required = true)
    public String label;

    @Schema(description = "属性类型", required = true)
    public String type;

    @Schema(description = "属性长度", required = true)
    public Integer length;

    @Schema(description = "是否标识", required = true)
    public boolean identified;

    @Schema(description = "属性类型", required = true)
    public boolean required;

    @Schema(description = "默认值", required = true)
    public Object defaultValue;
}
