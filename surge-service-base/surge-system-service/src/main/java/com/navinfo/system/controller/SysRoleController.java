package com.surge.system.controller;

import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.result.R;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Update;
import com.surge.common.mybatis.pagination.PageInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.system.domain.entity.SysRole;
import com.surge.system.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "角色管理接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
public class SysRoleController {

    private final ISysRoleService roleService;

    @Operation(summary = "查询角色详情")
    @Parameters({
            @Parameter(name = "roleId", description = "角色Id", in = ParameterIn.PATH)
    })
    @GetMapping(value = "/{roleId}")
    public R<SysRole> getInfo(@PathVariable Long roleId) {
        return R.ok(roleService.queryById(roleId));
    }

    @Operation(summary = "用户分页")
    @Parameters({
            @Parameter(name = "name", description = "角色名称", in = ParameterIn.QUERY),
            @Parameter(name = "code", description = "角色编码", in = ParameterIn.QUERY),
            @Parameter(name = "status",description = "角色状态（0正常 1停用）", in = ParameterIn.QUERY),
            @Parameter(name = "current",description = "页码", in = ParameterIn.QUERY),
            @Parameter(name = "size",description = "分页大小", in = ParameterIn.QUERY)
    })
    @GetMapping("/page")
    public R<PageInfo<SysRole>> queryPage(SysRole role, PageInfo<SysRole> pageQuery) {
        return R.ok(roleService.queryPage(role, pageQuery));
    }

    @Operation(summary = "用户创建",description = "用户创建")
    @JsonView(Create.class)
    @PostMapping
    public R createRole(@Validated @RequestBody SysRole role) {
        if (roleService.checkIfRoleNameExist(role)) {
            throw new ServiceException("角色名称已存在");
        } else if (roleService.checkIfRoleCodeExist(role)) {
            throw new ServiceException("角色编码已存在");
        }
        roleService.createRole(role);
        return R.ok();
    }

    @Operation(summary = "用户更新",description = "用户更新")
    @JsonView(Update.class)
    @PutMapping
    public R updateRole(@Validated @RequestBody SysRole role) {
        if (roleService.checkIfRoleNameExist(role)) {
            throw new ServiceException("角色名称已存在");
        } else if (roleService.checkIfRoleCodeExist(role)) {
            throw new ServiceException("角色编码已存在");
        }
        roleService.updateRole(role);
        return R.ok();
    }

    @Operation(summary = "角色删除")
    @Parameters({
            @Parameter(name = "roleId", description = "角色Id", in = ParameterIn.PATH)
    })
    @DeleteMapping("/{roleId}")
    public R deleteRole(@PathVariable Long roleId) {
        roleService.deleteRoleById(roleId);
        return R.ok();
    }

}

