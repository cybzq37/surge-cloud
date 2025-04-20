package com.surge.system.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.surge.common.mybatis.pagination.PageInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.system.domain.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper extends BaseMapperPlus<SysRoleMapper, SysRole, SysRole> {

//    @DataPermission({
//        @DataColumn(key = "deptName", value = "d.dept_id"),
//        @DataColumn(key = "userName", value = "us.user_id")
//    })
    PageInfo<SysRole> selectRolePage(@Param("page") PageInfo<SysRole> page, @Param(Constants.WRAPPER) Wrapper<SysRole> queryWrapper);

//    @DataPermission({
//        @DataColumn(key = "deptName", value = "d.dept_id"),
//        @DataColumn(key = "userName", value = "us.user_id")
//    })
    List<SysRole> selectRoleList(@Param(Constants.WRAPPER) Wrapper<SysRole> queryWrapper);


    List<SysRole> selectRolesByUserId(Long userId);


    List<Long> selectRoleListByUserId(Long userId);


    List<SysRole> selectRolesByUsername(String username);

}
