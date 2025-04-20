package com.surge.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.surge.common.core.constant.Constants;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.satoken.utils.LoginHelper;
import com.surge.system.domain.entity.SysMenu;
import com.surge.system.domain.entity.SysRoleMenu;
import com.surge.system.repository.SysMenuMapper;
import com.surge.system.repository.SysRoleMapper;
import com.surge.system.repository.SysRoleMenuMapper;
import com.surge.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SysMenuServiceImpl implements ISysMenuService {

    private final SysRoleMapper sysRoleMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;


    @Override
    public SysMenu queryById(Long menuId) {
        return sysMenuMapper.selectById(menuId);
    }

    @Override
    public List<SysMenu> queryMenuList(Long userId) {
        return queryMenuList(new SysMenu(), userId);
    }

    @Override
    public List<SysMenu> queryMenuList(SysMenu sysMenu, Long userId) {
        List<SysMenu> menuList = null;
        // 管理员显示所有菜单信息
        if (LoginHelper.isRoot(userId)) {
            menuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .like(StringUtils.isNotBlank(sysMenu.getName()), SysMenu::getName, sysMenu.getName())
                .eq(StringUtils.isNotBlank(sysMenu.getVisible()), SysMenu::getVisible, sysMenu.getVisible())
                .eq(StringUtils.isNotBlank(sysMenu.getStatus()), SysMenu::getStatus, sysMenu.getStatus())
                .orderByAsc(SysMenu::getSort));
        } else {
            QueryWrapper<SysMenu> wrapper = Wrappers.query();
            wrapper.eq("sur.user_id", userId)
                .like(StringUtils.isNotBlank(sysMenu.getName()), "m.name", sysMenu.getName())
                .eq(StringUtils.isNotBlank(sysMenu.getVisible()), "m.visible", sysMenu.getVisible())
                .eq(StringUtils.isNotBlank(sysMenu.getStatus()), "m.status", sysMenu.getStatus())
                .orderByAsc("m.sort");
            menuList = sysMenuMapper.selectListByConditions(wrapper);
        }
        return menuList;
    }

    @Override
    public Set<String> selectCodesByUserId(Long userId) {
        return sysMenuMapper.selectCodesByUserId(userId);
    }

    @Override
    public Set<String> selectCodesByRoleId(Long roleId) {
        return sysMenuMapper.selectCodesByRoleId(roleId);
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectTreeByUserId(Long userId) {
        QueryWrapper<SysMenu> wrapper = Wrappers.query();
        wrapper.eq("sur.user_id", userId)
                .eq("m.visible", "0")
                .eq("m.status", "0")
                .in("m.type", 'C', 'M', 'N') // 只查询菜单, 不查询按钮信息
                .orderByAsc("m.sort");
        List<SysMenu> resources = sysMenuMapper.selectListByConditions(wrapper);
        return resources;
    }

//    @Override
//    public List<Long> selectResourceListByRoleId(Long roleId) {
//        SysRole role = sysRoleMapper.selectById(roleId);
//        return sysResourceMapper.selectResourceListByRoleId(roleId, role.getResourceCheckStrictly());
//    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @return 路由列表
     */
//    @Override
//    public List<RouterVo> buildResources(List<SysResource> menus) {
//        List<RouterVo> routers = new LinkedList<RouterVo>();
//        for (SysResource menu : menus) {
//            RouterVo router = new RouterVo();
//            router.setHidden("1".equals(menu.getVisible()));
//            router.setName(getRouteName(menu));
//            router.setPath(getRouterPath(menu));
//            router.setComponent(getComponent(menu));
//            router.setQuery(menu.getQueryParam());
//            router.setMeta(new MetaVo(menu.getResourceName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
//            List<SysResource> cResources = menu.getChildren();
//            if (CollUtil.isNotEmpty(cResources) && UserConstants.TYPE_DIR.equals(menu.getResourceType())) {
//                router.setAlwaysShow(true);
//                router.setRedirect("noRedirect");
//                router.setChildren(buildResources(cResources));
//            } else if (isResourceFrame(menu)) {
//                router.setMeta(null);
//                List<RouterVo> childrenList = new ArrayList<RouterVo>();
//                RouterVo children = new RouterVo();
//                children.setPath(menu.getPath());
//                children.setComponent(menu.getComponent());
//                children.setName(StringUtils.capitalize(menu.getPath()));
//                children.setMeta(new MetaVo(menu.getResourceName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
//                children.setQuery(menu.getQueryParam());
//                childrenList.add(children);
//                router.setChildren(childrenList);
//            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
//                router.setMeta(new MetaVo(menu.getResourceName(), menu.getIcon()));
//                router.setPath("/");
//                List<RouterVo> childrenList = new ArrayList<RouterVo>();
//                RouterVo children = new RouterVo();
//                String routerPath = innerLinkReplaceEach(menu.getPath());
//                children.setPath(routerPath);
//                children.setComponent(UserConstants.INNER_LINK);
//                children.setName(StringUtils.capitalize(routerPath));
//                children.setMeta(new MetaVo(menu.getResourceName(), menu.getIcon(), menu.getPath()));
//                childrenList.add(children);
//                router.setChildren(childrenList);
//            }
//            routers.add(router);
//        }
//        return routers;
//    }

    @Override
    public boolean checkIfHasChild(Long menuId) {
        return sysMenuMapper.exists(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getPid, menuId));
    }

    @Override
    public boolean checkIfExistRelateRole(Long menuId) {
        return sysRoleMenuMapper.exists(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, menuId));
    }

    @Override
    public int createMenu(SysMenu sysMenu) {
        return sysMenuMapper.insert(sysMenu);
    }

    @Override
    public int updateMenu(SysMenu sysMenu) {
        return sysMenuMapper.updateById(sysMenu);
    }

    @Override
    public int deleteById(Long menuId) {
        return sysMenuMapper.deleteById(menuId);
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
//    public String getRouteName(SysResource menu) {
//        String routerName = StringUtils.capitalize(menu.getPath());
//        // 非外链并且是一级目录（类型为目录）
//        if (isResourceFrame(menu)) {
//            routerName = StringUtils.EMPTY;
//        }
//        return routerName;
//    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
//    public String getRouterPath(SysResource menu) {
//        String routerPath = menu.getPath();
//        // 内链打开外网方式
//        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
//            routerPath = innerLinkReplaceEach(routerPath);
//        }
//        // 非外链并且是一级目录（类型为目录）
//        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getResourceType())
//            && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
//            routerPath = "/" + menu.getPath();
//        }
//        // 非外链并且是一级目录（类型为菜单）
//        else if (isResourceFrame(menu)) {
//            routerPath = "/";
//        }
//        return routerPath;
//    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
//    public String getComponent(SysResource menu) {
//        String component = UserConstants.LAYOUT;
//        if (StringUtils.isNotEmpty(menu.getComponent()) && !isResourceFrame(menu)) {
//            component = menu.getComponent();
//        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
//            component = UserConstants.INNER_LINK;
//        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
//            component = UserConstants.PARENT_VIEW;
//        }
//        return component;
//    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
//    public boolean isResourceFrame(SysResource menu) {
//        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getResourceType())
//            && menu.getIsFrame().equals(UserConstants.NO_FRAME);
//    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
//    public boolean isInnerLink(SysResource menu) {
//        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
//    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
//    public boolean isParentView(SysResource menu) {
//        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getResourceType());
//    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
//    public List<SysResource> getChildPerms(List<SysResource> list, int parentId) {
//        List<SysResource> returnList = new ArrayList<SysResource>();
//        for (SysResource t : list) {
//            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
//            if (t.getParentId() == parentId) {
//                recursionFn(list, t);
//                returnList.add(t);
//            }
//        }
//        return returnList;
//    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
//    private void recursionFn(List<SysResource> list, SysResource t) {
//        // 得到子节点列表
//        List<SysResource> childList = getChildList(list, t);
//        t.setChildren(childList);
//        for (SysResource tChild : childList) {
//            if (hasChild(list, tChild)) {
//                recursionFn(list, tChild);
//            }
//        }
//    }

    /**
     * 得到子节点列表
     */
//    private List<SysResource> getChildList(List<SysResource> list, SysResource t) {
//        return StreamUtils.filter(list, n -> n.getParentId().equals(t.getResourceId()));
//    }

    /**
     * 判断是否有子节点
     */
//    private boolean hasChild(List<SysResource> list, SysResource t) {
//        return getChildList(list, t).size() > 0;
//    }

    /**
     * 内链域名特殊字符替换
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS, Constants.WWW, ".", ":"},
            new String[]{"", "", "", "/", "/"});
    }
}
