package com.surge.system.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("nc_sys.sys_user_role")
public class SysUserRole {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;


    private Long userId;

    private Long roleId;

}
