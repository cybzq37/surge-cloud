package com.surge.system.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.mybatis.annotation.DataColumn;
import com.surge.common.mybatis.annotation.DataPermission;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.system.domain.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author lichunqing
 */
public interface SysUserMapper extends BaseMapperPlus<SysUserMapper, SysUser, SysUser> {

    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.id")
    })
    PageInfo<SysUser> selectPageUserList(@Param("page") PageInfo<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 根据条件分页查询用户列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.id")
    })
    List<SysUser> selectUserList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.id")
    })
    PageInfo<SysUser> selectAllocatedList(@Param("page") PageInfo<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission({
        @DataColumn(key = "deptName", value = "d.dept_id"),
        @DataColumn(key = "userName", value = "u.id")
    })
    PageInfo<SysUser> selectUnallocatedList(@Param("page") PageInfo<SysUser> page, @Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);


    SysUser selectByUserId(Long userId);

    SysUser selectByUsername(String userName);

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    SysUser selectUserByPhonenumber(String phonenumber);

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    SysUser selectUserByEmail(String email);


}
