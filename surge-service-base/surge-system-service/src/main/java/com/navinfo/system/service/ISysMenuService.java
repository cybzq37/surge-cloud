package com.surge.system.service;

import com.surge.system.domain.entity.SysMenu;

import java.util.List;
import java.util.Set;


public interface ISysMenuService {

    SysMenu queryById(Long menuId);

    List<SysMenu> queryMenuList(Long userId);

    List<SysMenu> queryMenuList(SysMenu menu, Long userId);

    Set<String> selectCodesByUserId(Long userId);

    Set<String> selectCodesByRoleId(Long roleId);

    List<SysMenu> selectTreeByUserId(Long userId);

    int createMenu(SysMenu menu);

    int updateMenu(SysMenu menu);

    int deleteById(Long menuId);

//    List<Long> selectResourceListByRoleId(Long roleId);
//
//    List<RouterVo> buildResources(List<SysResource> menus);
//
//    List<Tree<Long>> buildResourceTreeSelect(List<SysResource> menus);

    boolean checkIfHasChild(Long menuId);

    boolean checkIfExistRelateRole(Long menuId);
}
