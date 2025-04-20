package com.surge.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Update;
import com.surge.common.json.view.Query;
import com.surge.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Tag(name = "字典实体")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("nc_sys.sys_dict")
public class SysDict extends BaseEntity {

    @Schema(description = "id", type = "Long")
    @JsonView({Update.class, Query.class})
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "父级Id", type = "Long")
    @JsonView({Create.class, Update.class, Query.class})
    private Long pid;

    @Schema(description = "字典类型", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型长度不能超过100个字符")
    private String type;

    @Schema(description = "字典编码", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    @NotBlank(message = "字典编码不能为空")
    @Size(min = 0, max = 100, message = "字典键值长度不能超过100个字符")
    private String code;

    @Schema(description = "字典标签", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    @NotBlank(message = "字典标签不能为空")
    @Size(min = 0, max = 100, message = "字典标签长度不能超过100个字符")
    private String label;

    @Schema(description = "字典键值", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    @NotBlank(message = "字典键值不能为空")
    @Size(min = 0, max = 100, message = "字典键值长度不能超过100个字符")
    private String value;

    @Schema(description = "是否默认", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    private String defaultFlag;

    @Schema(description = "字典排序", type = "Integer")
    @JsonView({Create.class, Update.class, Query.class})
    private Integer sort;

    @Schema(description = "状态", type = "Integer")
    @JsonView({Create.class, Update.class, Query.class})
    private String status;

    @Schema(description = "附加数据", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    private String append;

    @Schema(description = "备注", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    private String remark;

}
