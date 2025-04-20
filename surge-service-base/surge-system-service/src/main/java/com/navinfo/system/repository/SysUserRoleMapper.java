package com.surge.system.repository;

import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.system.domain.entity.SysUserRole;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author lichunqing
 */
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRoleMapper, SysUserRole, SysUserRole> {

    List<Long> selectUserIdsByRoleId(Long roleId);

}
