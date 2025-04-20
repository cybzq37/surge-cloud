package com.surge.system.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.mybatis.pagination.PageInfo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.surge.common.core.constant.UserConstant;
import com.surge.common.core.utils.StreamUtils;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.mybatis.helper.DataBaseHelper;
import com.surge.system.domain.entity.*;
import com.surge.system.repository.*;
import com.surge.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements ISysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysOrgMapper sysOrgMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysUserOrgMapper sysUserOrgMapper;
    private final SysMenuMapper sysMenuMapper;

    @Override
    public SysUser queryById(Long userId) {
        SysUser sysUser = sysUserMapper.selectByUserId(userId);
        // 查询菜单
        QueryWrapper<SysMenu> wrapper = Wrappers.query();
        wrapper.eq("sur.user_id", userId)
                .orderByAsc("m.sort");
        List<SysMenu> menus = sysMenuMapper.selectListByConditions(wrapper);
        sysUser.getMenus().addAll(menus);
        return sysUser;
    }

    @Override
    public SysUser queryByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public PageInfo<SysUser> queryPage(String username,
                                    String realname,
                                    String phone,
                                    String status,
                                    Long sysOrgId,
                                    PageInfo<SysUser> page) {
        // 组织机构id查询
        List<SysOrg> sysOrgList = sysOrgMapper.selectList(new LambdaQueryWrapper<SysOrg>()
                .apply(DataBaseHelper.findInSet(sysOrgId, "ancestors")));
        Set<Long> sysOrgIds = StreamUtils.toSet(sysOrgList, SysOrg::getId);
        sysOrgIds.add(sysOrgId);
        // 查询分页
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstant.USER_NORMAL)
                .like(StringUtils.isNotBlank(username), "u.username", username)
                .like(StringUtils.isNotBlank(realname), "u.realname", realname)
                .like(StringUtils.isNotBlank(phone), "u.phone", phone)
                .eq(StringUtils.isNotBlank(status), "u.status", status)
                .and(ObjectUtil.isNotNull(sysOrgId), w -> w.in("suo.org_id", sysOrgIds));
        page = sysUserMapper.selectPageUserList(page, wrapper);
        return page;
    }

    @Override
    public boolean checkIfUsernameExist(String username) {
        return sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getUsername, username));
    }

    @Override
    public boolean checkIfPhoneExist(String phone) {
        boolean exist = sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getPhone, phone)
            .ne(ObjectUtil.isNotNull(phone), SysUser::getPhone, phone));
        return !exist;
    }

    @Override
    public boolean checkIfEmailExist(String email) {
        boolean exist = sysUserMapper.exists(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getEmail, email)
            .ne(ObjectUtil.isNotNull(email), SysUser::getEmail, email));
        return !exist;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(SysUser user) {
        // 新增用户信息
        sysUserMapper.insert(user);
        // 新增用户与角色管理
        if (ArrayUtil.isNotEmpty(user.getRoleIds())) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<>(user.getRoleIds().size());
            for (Long roleId : user.getRoleIds()) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            sysUserRoleMapper.insertBatch(list);
        }
        // 新增用户与组织机构管理
        if (ArrayUtil.isNotEmpty(user.getOrgIds())) {
            // 新增用户与角色管理
            List<SysUserOrg> list = new ArrayList<>(user.getOrgIds().size());
            for (Long orgId : user.getOrgIds()) {
                SysUserOrg ur = new SysUserOrg();
                ur.setUserId(user.getId());
                ur.setOrgId(orgId);
                list.add(ur);
            }
            sysUserOrgMapper.insertBatch(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUser user) {
        // 角色更新
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, user.getId()));
        if (ArrayUtil.isNotEmpty(user.getRoleIds())) {
            List<SysUserRole> list = new ArrayList<>(user.getRoleIds().size());
            for (Long roleId : user.getRoleIds()) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            sysUserRoleMapper.insertBatch(list);
        }
        // 组织机构更新
        sysUserOrgMapper.delete(new LambdaQueryWrapper<SysUserOrg>().eq(SysUserOrg::getUserId, user.getId()));
        if (ArrayUtil.isNotEmpty(user.getOrgIds())) {
            List<SysUserOrg> list = new ArrayList<>(user.getOrgIds().size());
            for (Long orgId : user.getOrgIds()) {
                SysUserOrg ur = new SysUserOrg();
                ur.setUserId(user.getId());
                ur.setOrgId(orgId);
                list.add(ur);
            }
            sysUserOrgMapper.insertBatch(list);
        }
        // 更新用户信息
        sysUserMapper.updateById(user);
    }

    @Override
    public void resetPassword(Long id, String password) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(password);
        sysUserMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long userId) {
        // 逻辑删除, 删除关联角色, 保持组织机构关系
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        sysUserMapper.deleteById(userId);
    }

}
