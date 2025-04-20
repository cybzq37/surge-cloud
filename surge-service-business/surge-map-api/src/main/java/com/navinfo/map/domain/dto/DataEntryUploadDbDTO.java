package com.surge.map.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DataEntryUploadDbDTO {

    @Schema(description = "数据名称", required = true)
    private String name;

    @Schema(description = "数据库表名", required = true)
    private String tableName;


    @Schema(description = "epsg")
    private Integer epsg;

    @Schema(description = "坐标系")
    private String coordinateSystem;

}
