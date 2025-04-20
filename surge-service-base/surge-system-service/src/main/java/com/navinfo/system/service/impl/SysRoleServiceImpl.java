package com.surge.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.mybatis.pagination.PageInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.surge.common.core.constant.UserConstant;
import com.surge.common.core.utils.StreamUtils;
import com.surge.common.core.utils.StringUtils;

import com.surge.system.domain.entity.SysRole;
import com.surge.system.domain.entity.SysRoleMenu;
import com.surge.system.domain.entity.SysUserRole;
import com.surge.system.repository.SysRoleMapper;
import com.surge.system.repository.SysRoleMenuMapper;
import com.surge.system.repository.SysUserRoleMapper;
import com.surge.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SysRoleServiceImpl implements ISysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;

    @Override
    public SysRole queryById(Long roleId) {
        return sysRoleMapper.selectById(roleId);
    }
    @Override
    public SysRole queryByCode(String code) {
        QueryWrapper<SysRole> wrapper = Wrappers.query();
        wrapper.eq("code", code);
        return sysRoleMapper.selectOne(wrapper);
    }

    @Override
    public List<SysRole> queryByUserId(Long userId) {
        return sysRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    public PageInfo<SysRole> queryPage(SysRole role, PageInfo<SysRole> pageQuery) {
        QueryWrapper<SysRole> wrapper = Wrappers.query();
        wrapper.eq("r.del_flag", UserConstant.ROLE_NORMAL)
                .like(StringUtils.isNotBlank(role.getName()), "r.name", role.getName())
                .eq(StringUtils.isNotBlank(role.getCode()), "r.code", role.getCode())
                .eq(StringUtils.isNotBlank(role.getStatus()), "r.status", role.getStatus())
                .orderByAsc("r.sort");
        PageInfo<SysRole> page = sysRoleMapper.selectRolePage(pageQuery, wrapper);
        return page;
    }

    @Override
    public List<SysRole> queryList(SysRole role) {
        QueryWrapper<SysRole> wrapper = Wrappers.query();
        wrapper.eq("r.del_flag", UserConstant.ROLE_NORMAL)
                .like(StringUtils.isNotBlank(role.getName()), "r.name", role.getName())
                .eq(StringUtils.isNotBlank(role.getCode()), "r.code", role.getCode())
                .eq(StringUtils.isNotBlank(role.getStatus()), "r.status", role.getStatus())
                .orderByAsc("r.sort");
        return sysRoleMapper.selectRoleList(wrapper);
    }

    @Override
    public Set<String> queryRoleCodeByUserId(Long userId) {
        List<SysRole> roles = sysRoleMapper.selectRolesByUserId(userId);
        return StreamUtils.toSet(roles, SysRole::getCode);
    }

    @Override
    public boolean checkIfRoleNameExist(SysRole role) {
        return sysRoleMapper.exists(new LambdaQueryWrapper<SysRole>()
            .eq(SysRole::getName, role.getName()));
    }

    @Override
    public boolean checkIfRoleCodeExist(SysRole role) {
        return sysRoleMapper.exists(new LambdaQueryWrapper<SysRole>()
            .eq(SysRole::getCode, role.getCode()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(SysRole role) {
        // 新增角色信息
        sysRoleMapper.insert(role);
        // 新增角色与资源信息
        if(CollectionUtils.isEmpty(role.getMenuIds())) {
            return;
        }
        List<SysRoleMenu> list = new ArrayList<>(role.getMenuIds().size());
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            sysRoleMenuMapper.insertBatch(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRole role) {
        Long count = sysUserRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, role.getId()));
        if (UserConstant.ROLE_DISABLE.equals(role.getStatus()) && count > 0) {
            throw new ServiceException("角色已分配，不能禁用!");
        }
        // 修改角色信息
        sysRoleMapper.updateById(role);
        // 删除角色与菜单关联
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, role.getId()));
        // 新增角色与资源信息
        if(CollectionUtils.isEmpty(role.getMenuIds())) {
            return;
        }
        List<SysRoleMenu> list = new ArrayList<>(role.getMenuIds().size());
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            sysRoleMenuMapper.insertBatch(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleById(Long roleId) {
        Long count = sysUserRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId));
        if (count > 0) {
            throw new ServiceException("角色已分配，不能删除!");
        }
        // 删除角色与菜单关联
        sysRoleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        sysRoleMapper.deleteById(roleId);
    }

}
