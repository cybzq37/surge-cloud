package com.surge.system.controller;

import cn.hutool.core.lang.tree.Tree;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.result.R;
import com.surge.common.core.utils.tree.TreeNode;
import com.surge.common.core.utils.tree.TreeUtils;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Update;
import com.surge.common.mybatis.pagination.PageInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.core.constant.UserConstant;
import com.surge.common.core.utils.StringUtils;
import com.surge.system.domain.entity.SysOrg;
import com.surge.system.service.ISysOrgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "组织机构接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/org")
public class SysOrgController {

    private final ISysOrgService sysOrgService;

    @Operation(summary = "组织机构详情接口")
    @Parameters({
            @Parameter(name = "orgId", description = "组织机构Id", in = ParameterIn.PATH)
    })
    @GetMapping(value = "/{orgId}")
    public R<SysOrg> queryOne(@PathVariable Long orgId) {
        return R.ok(sysOrgService.queryById(orgId));
    }

    @Operation(summary = "组织机构树形接口")
    @GetMapping("/list")
    public R<List<SysOrg>> list() {
        List<SysOrg> orgList = sysOrgService.queryList(null, null);
        return R.ok(orgList);
    }

    @Operation(summary = "组织机构树形接口")
    @GetMapping("/tree")
    public R<List<TreeNode>> tree() {
        List<SysOrg> orgList = sysOrgService.queryList(null, null);
        if (CollectionUtils.isEmpty(orgList)) {
            return R.ok(Collections.EMPTY_LIST);
        }
        List<TreeNode<Long>> treeNodes = orgList.stream().map(o -> {
            TreeNode<Long> treeNode = new TreeNode<>();
            treeNode.setId(o.getId());
            treeNode.setPid(o.getPid());
            treeNode.setKey(o.getId() + "");
            treeNode.setKey(o.getName());
            treeNode.setType("org");
            return treeNode;
        }).collect(Collectors.toList());

        List<TreeNode> list = TreeUtils.buildTree(treeNodes);
        return R.ok(list);
    }

    @Operation(summary = "组织机构列表接口")
    @GetMapping("/page")
    public R<PageInfo<SysOrg>> queryPage(Long pid, String name, PageInfo<SysOrg> page) {
        return R.ok(sysOrgService.queryPage(pid, name, page));
    }

    @Operation(summary = "组织机构创建接口")
    @JsonView(Create.class)
    @PostMapping
    public R create(@Validated @RequestBody SysOrg sysOrg) {
        sysOrgService.createOrg(sysOrg);
        return R.ok();
    }

    @Operation(summary = "组织机构更新接口")
    @JsonView(Update.class)
    @PutMapping
    public R update(@Validated @RequestBody SysOrg sysOrg) {
        if (sysOrg.getPid().equals(sysOrg.getId())) {
            throw new ServiceException("修改部门失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstant.DEPT_DISABLE, sysOrg.getStatus())) {
            if (sysOrgService.queryAllChildNodes(sysOrg.getId()) > 0) {
                throw new ServiceException("该部门包含未停用的子部门，不能禁用!");
            } else if (sysOrgService.checkIfHasUser(sysOrg.getId())) {
                throw new ServiceException("该部门下存在已分配用户，不能禁用!");
            }
        }
        sysOrgService.updateOrg(sysOrg);
        return R.ok();
    }

    @Operation(summary = "组织机构删除接口")
    @Parameters({
            @Parameter(name = "sysOrgId", description = "组织机构Id", in = ParameterIn.PATH)
    })
    @DeleteMapping("/{sysOrgId}")
    public R delete(@PathVariable Long sysOrgId) {
        if (sysOrgService.hasChildNode(sysOrgId)) {
            throw new ServiceException("存在下级部门,不允许删除");
        }
        if (sysOrgService.checkIfHasUser(sysOrgId)) {
            throw new ServiceException("部门存在用户,不允许删除");
        }
        sysOrgService.deleteOrg(sysOrgId);
        return R.ok();
    }
}