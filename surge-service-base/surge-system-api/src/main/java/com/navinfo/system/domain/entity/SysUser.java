package com.surge.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.json.plugins.sensitive.Sensitive;
import com.surge.common.json.plugins.sensitive.SensitiveStrategy;
import com.surge.common.core.domain.BaseEntity;
import com.surge.common.json.plugins.xss.Xss;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Query;
import com.surge.common.json.view.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("nc_sys.sys_user")
public class SysUser extends BaseEntity {

    @Schema(description = "id", type = "Long", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonView({Query.class, Update.class})
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户账号", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    private String username;


    @Schema(description = "真实姓名", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    @Xss(message = "真实姓名不能包含脚本字符")
    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 0, max = 30, message = "真实姓名长度不能超过30个字符")
    private String realname;


    @Schema(description = "用户类型（sys系统内置用户）", type = "String")
    @JsonView({Query.class})
    private String type;

    @Schema(description = "用户邮箱", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;


    @Schema(description = "用户手机", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String phone;


    @Schema(description = "用户性别（1男 2女 3未知）", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    private String sex;

    @Schema(description = "头像地址", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    private String avatar;

    @Schema(description = "密码", type = "String")
    @JsonView({Create.class, Update.class})
    @TableField(
        insertStrategy = FieldStrategy.NOT_EMPTY,
        updateStrategy = FieldStrategy.NOT_EMPTY,
        whereStrategy = FieldStrategy.NOT_EMPTY
    )
    private String password;

    @JsonIgnore
    @JsonProperty
    public String getPassword() {
        return password;
    }

    @Schema(description = "帐号状态（0正常 1停用）", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    private String status;

    @Schema(description = "删除标志（0代表存在 1代表删除）", type = "String")
    @TableLogic
    private String delFlag;

    @JsonIgnore
    @JsonProperty
    public String getDelFlag() {
        return delFlag;
    }

    @JsonView({Query.class})
    private String loginIp;

    @JsonView({Query.class})
    private Date loginDate;

    @Schema(description = "更新人", type = "String")
    @JsonView({Create.class, Query.class, Update.class})
    private String remark;

    @JsonView({Query.class})
    @TableField(exist = false)
    private List<SysOrg> orgs = new ArrayList<>(2);

    @Schema(description = "组织机构的Id集合(数组)", type = "List")
    @JsonView({Create.class, Query.class, Update.class})
    @TableField(exist = false)
    private List<Long> orgIds = new ArrayList<>(2);

    @JsonView({Query.class})
    @TableField(exist = false)
    private List<SysRole> roles = new ArrayList<>(2);

    @Schema(description = "角色的Id集合(数组)", type = "List")
    @JsonView({Create.class, Query.class, Update.class})
    @TableField(exist = false)
    private List<Long> roleIds = new ArrayList<>(2);

    @JsonView({Query.class})
    @TableField(exist = false)
    private List<SysMenu> menus = new ArrayList<>(64);

}
