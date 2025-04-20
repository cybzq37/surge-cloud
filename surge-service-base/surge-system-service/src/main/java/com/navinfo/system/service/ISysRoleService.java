package com.surge.system.service;


import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.system.domain.entity.SysRole;

import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 *
 * @author lichunqing
 */
public interface ISysRoleService {

    SysRole queryById(Long roleId);

    SysRole queryByCode(String roleCode);

    PageInfo<SysRole> queryPage(SysRole role, PageInfo<SysRole> pageQuery);

    List<SysRole> queryList(SysRole role);

    List<SysRole> queryByUserId(Long userId);

    Set<String> queryRoleCodeByUserId(Long userId);

    boolean checkIfRoleNameExist(SysRole role);

    boolean checkIfRoleCodeExist(SysRole role);

    void createRole(SysRole role);

    void updateRole(SysRole role);

    void deleteRoleById(Long roleId);

}
