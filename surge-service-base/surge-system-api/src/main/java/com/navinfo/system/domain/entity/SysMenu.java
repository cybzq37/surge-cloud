package com.surge.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
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

@Tag(name = "菜单实体")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("nc_sys.sys_menu")
public class SysMenu extends BaseEntity {
    

    @Schema(description = "id", type = "Long")
    @JsonView({Update.class, Query.class})
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "pid", type = "Long")
    @JsonView({Create.class, Update.class, Query.class})
    private Long pid;

    @Schema(description = "资源名称", type = "Long")
    @JsonView({Create.class, Update.class, Query.class})
    @NotBlank(message = "资源名称不能为空")
    @Size(min = 0, max = 50, message = "资源名称长度不能超过50个字符")
    private String name;

    @Schema(description = "显示顺序", type = "Integer")
    @JsonView({Create.class, Update.class, Query.class})
    private Integer sort;

    @Schema(description = "路由地址", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    @Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
    private String path;

    @Schema(description = "组件路径", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    @Size(min = 0, max = 200, message = "组件路径不能超过255个字符")
    private String component;

    @Schema(description = "路由参数", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    private String queryParams;

    @Schema(description = "是否外链（0是 1否）", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    private String linkFlag;

    @Schema(description = "菜单类型（P平台 C目录 M菜单 N子菜单 F按钮）", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    @NotBlank(message = "菜单类型不能为空")
    private String type;

    @Schema(description = "显示状态（0显示 1隐藏）", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    private String visible;

    @Schema(description = "菜单状态（0正常 1停用）", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    private String status;

    @Schema(description = "菜单标识，控制权限使用", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    private String code;

    @Schema(description = "菜单图标", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    private String icon;

    @Schema(description = "备注", type = "String")
    @JsonView({Create.class, Update.class, Query.class})
    private String remark;

    @JsonView({Query.class})
    @TableField(exist = false)
    private List<SysOrg> children = new ArrayList<>();

}
