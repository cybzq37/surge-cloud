package com.surge.system.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceDTO {

    @Schema(description = "资源Id集合", required = true)
    private List<Long> resourceIds = new ArrayList<>();

    @Schema(description = "资源类型 1：entry图层  2：deviceType设备", required = true)
    @NotNull(message = "数据源类型不能为空")
    private Integer resourceType;

    @Schema(description = "组织机构Id", required = true)
    @NotNull(message = "组织机构不能为空")
    private Long orgId;
}
