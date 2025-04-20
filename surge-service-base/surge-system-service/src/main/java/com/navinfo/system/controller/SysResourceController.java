package com.surge.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.core.result.R;
import com.surge.system.domain.entity.SysResource;
import com.surge.system.domain.model.ResourceDTO;
import com.surge.system.repository.SysResourceRepository;
import com.surge.system.service.ISysResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "资源关系")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/resource")
public class SysResourceController {

    private final SysResourceRepository sysResourceRepository;
    private final ISysResourceService resourceDisplayService;

    @Operation(summary = "图层创建接口")
    @PostMapping
    public R saveOrUpdate(@Validated @RequestBody ResourceDTO resourceDTO) {
        resourceDisplayService.saveOrUpdate(resourceDTO);
        return R.ok();
    }

    @Operation(summary = "获取资源Id集合")
    @GetMapping("org/{orgId}")
    public R queryResourceList(@PathVariable("orgId") Long orgId) {
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_id", orgId);
        List<SysResource> list =  sysResourceRepository.list(queryWrapper);
        return R.ok(list);
    }

    @Operation(summary = "获取资源Id集合")
    @GetMapping("list")
    public R queryResourceList(int type, long orgId) {
        List<SysResource> list = sysResourceRepository.queryResouceByTypeAndOrgId(type, orgId);
        return R.ok(list);
    }
}
