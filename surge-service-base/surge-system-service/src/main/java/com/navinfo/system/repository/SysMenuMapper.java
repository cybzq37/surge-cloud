package com.surge.system.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.system.domain.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜单表 数据层
 *
 * @author lichunqing
 */
public interface SysMenuMapper extends BaseMapperPlus<SysMenuMapper, SysMenu, SysMenu> {

    List<SysMenu> selectListByConditions(@Param(Constants.WRAPPER) Wrapper<SysMenu> queryWrapper);

    Set<String> selectCodesByUserId(Long userId);

    Set<String> selectCodesByRoleId(Long roleId);

    // 只查询菜单信息
//    List<SysResource> selectTreeByUserId(Long userId);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId            角色ID
     * @param menuCheckStrictly 菜单树选择项是否关联显示
     * @return 选中菜单列表
     */
//    List<Long> selectListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

}
