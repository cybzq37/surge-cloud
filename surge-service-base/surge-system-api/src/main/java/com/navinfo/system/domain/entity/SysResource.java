package com.surge.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Tag(name = "资源关联关系")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@TableName(value = "nc_sys.sys_resource")
public class SysResource {

    @Schema(description = "图层数据ID", type = "Long")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "资源Id", type = "String")
    private String resourceId;

    @Schema(description = "资源类别", type = "Long")
    private Integer resourceType;

    @Schema(description = "组织机构Id", type = "String")
    private String orgId;

}