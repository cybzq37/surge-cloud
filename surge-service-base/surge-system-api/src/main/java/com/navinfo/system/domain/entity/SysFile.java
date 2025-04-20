package com.surge.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.surge.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Tag(name = "文件实体")
@Data
@EqualsAndHashCode
@TableName("nc_sys.sys_file")
public class SysFile extends BaseEntity {

    @Schema(description = "id", type = "Long")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "文件存储名称", type = "String")
    private String fileName;

    @Schema(description = "文件原名称", type = "String")
    private String originalName;

    @Schema(description = "文件后缀名", type = "String")
    private String fileSuffix;

    @Schema(description = "文件存储地址", type = "String")
    private String filePath;

}
