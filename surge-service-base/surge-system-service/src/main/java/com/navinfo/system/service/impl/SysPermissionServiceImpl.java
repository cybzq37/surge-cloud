//package com.surge.system.service.impl;
//
//import com.surge.common.satoken.utils.LoginHelper;
//import com.surge.system.domain.entity.SysUser;
//import com.surge.system.service.ISysResourceService;
//import com.surge.system.service.ISysPermissionService;
//import com.surge.system.service.ISysRoleService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * 用户权限处理
// *
// * @author lichunqing
// */
//@RequiredArgsConstructor
//@Service
//public class SysPermissionServiceImpl implements ISysPermissionService {
//
//    private final ISysRoleService roleService;
//    private final ISysResourceService menuService;
//
//    /**
//     * 获取角色数据权限
//     *
//     * @param user 用户
//     * @return 角色权限信息
//     */
//    @Override
//    public Set<String> getRolePermission(SysUser user) {
//        Set<String> roles = new HashSet<String>();
//        // 管理员拥有所有权限
//        if (LoginHelper.isRoot(user.getUserId())) {
//            roles.add("admin");
//        } else {
//            roles.addAll(roleService.queryRoleCodeByUserId(user.getUserId()));
//        }
//        return roles;
//    }
//
//    /**
//     * 获取菜单数据权限
//     *
//     * @param user 用户
//     * @return 菜单权限信息
//     */
//    @Override
//    public Set<String> getMenuPermission(SysUser user) {
//        Set<String> perms = new HashSet<String>();
//        // 管理员拥有所有权限
//        if (LoginHelper.isRoot(user.getUserId())) {
//            perms.add("*:*:*");
//        } else {
//            perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
//        }
//        return perms;
//    }
//}
