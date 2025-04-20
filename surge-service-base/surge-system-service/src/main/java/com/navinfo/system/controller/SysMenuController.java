package com.surge.system.controller;

import cn.hutool.core.lang.tree.Tree;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.result.R;
import com.surge.common.core.utils.tree.TreeBuildUtils;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Update;
import com.surge.common.satoken.utils.LoginHelper;
import com.surge.system.domain.entity.SysMenu;
import com.surge.system.service.ISysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "菜单管理接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    @Operation(summary = "菜单详情接口")
    @Parameters({
            @Parameter(name = "menuId", description = "菜单详情", in = ParameterIn.PATH)
    })
    @GetMapping(value = "/{menuId}")
    public R<SysMenu> getInfo(@PathVariable Long menuId) {
        return R.ok(sysMenuService.queryById(menuId));
    }

    @Operation(summary = "菜单列表接口")
    @GetMapping("/list")
    public R<List<SysMenu>> list() {
        Long userId = LoginHelper.getUserId();
        return R.ok(sysMenuService.queryMenuList(new SysMenu(), userId));
    }

    @Operation(summary = "菜单树形接口接口")
    @GetMapping("/tree")
    public R<List<Tree<Long>>> tree() {
        Long userId = LoginHelper.getUserId();
        List<SysMenu> menuList = sysMenuService.queryMenuList(new SysMenu(), userId);
        if (CollectionUtils.isEmpty(menuList)) {
            return R.ok(Collections.EMPTY_LIST);
        }
        return R.ok(TreeBuildUtils.build(menuList, (menu, tree) ->
                tree.setId(menu.getId())
                        .setParentId(menu.getPid())
                        .setName(menu.getName())
                        .setWeight(menu.getSort())));
    }

    @Operation(summary = "菜单创建接口")
    @JsonView(Create.class)
    @PostMapping
    public R create(@Validated @RequestBody SysMenu menu) {
        sysMenuService.createMenu(menu);
        return R.ok();
    }

    @Operation(summary = "菜单更新接口")
    @JsonView(Update.class)
    @PutMapping
    public R update(@Validated @RequestBody SysMenu menu) {
        if (menu.getId().equals(menu.getPid())) {
            throw new ServiceException("修改菜单失败，上级菜单不能选择自己");
        }
        sysMenuService.updateMenu(menu);
        return R.ok();
    }

    @Operation(summary = "菜单删除接口")
    @Parameters({
            @Parameter(name = "id", description = "菜单Id", in = ParameterIn.PATH)
    })
    @DeleteMapping("/{id}")
    public R delete(@PathVariable("id") Long id) {
        if (sysMenuService.checkIfHasChild(id)) {
            throw new ServiceException("存在子菜单,不允许删除");
        }
        if (sysMenuService.checkIfExistRelateRole(id)) {
            throw new ServiceException("菜单已分配,不允许删除");
        }
        sysMenuService.deleteById(id);
        return R.ok();
    }
}
