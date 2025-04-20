package com.surge.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.core.domain.BaseEntity;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Query;
import com.surge.common.json.view.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "组织机构实体")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("nc_sys.sys_org")
public class SysOrg extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id", type = "Long")
    @JsonView({Update.class, Query.class})
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "父级id", type = "Long")
    @JsonView({Create.class, Update.class, Query.class})
    private Long pid;

    @Schema(description = "祖级Id集合", type = "String")
    @JsonView({Query.class})
    private String ancestors;

    @Schema(description = "部门名称", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 0, max = 30, message = "部门名称长度不能超过30个字符")
    private String name;

    @Schema(description = "显示顺序", type = "Integer")
    @JsonView({Create.class, Update.class, Query.class})
    private Integer sort;

    @Schema(description = "部门状态: 0正常, 1停用", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    private String status;

    @TableLogic
    @Schema(description = "删除标志: 0代表存在, 1代表删除）", type = "String")
    @JsonView({Query.class})
    private String delFlag;

    @JsonIgnore
    @JsonProperty
    public String getDelFlag() {
        return delFlag;
    }

    @JsonView({Query.class})
    @TableField(exist = false)
    private List<SysOrg> children = new ArrayList<>();

}
