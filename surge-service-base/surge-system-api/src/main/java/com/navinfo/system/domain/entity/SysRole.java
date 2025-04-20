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

@Tag(name = "角色实体")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("nc_sys.sys_role")
public class SysRole extends BaseEntity {

    @Schema(description = "id", type = "Long")
    @JsonView({Query.class, Update.class})
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "角色名称", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    private String name;

    @Schema(description = "角色编码", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    @NotBlank(message = "角色编码不能为空")
    @Size(min = 0, max = 100, message = "角色编码长度不能超过100个字符")
    private String code;

    @Schema(description = "角色排序", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    private Integer sort;

    @Schema(description = "角色状态（0正常 1停用）", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    private String status;

    @Schema(description = "删除标志（0代表存在 1代表禁用 2代表删除）", type = "String")
    @TableLogic
    private String delFlag;

    @JsonIgnore
    @JsonProperty
    public String getDelFlag() {
        return delFlag;
    }

    @Schema(description = "备注", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    private String remark;

    @Schema(description = "角色管理菜单Id集合(数组)", type = "List")
    @JsonView({Create.class, Query.class, Update.class})
    @TableField(exist = false)
    private List<Long> menuIds = new ArrayList<>(64);

}
