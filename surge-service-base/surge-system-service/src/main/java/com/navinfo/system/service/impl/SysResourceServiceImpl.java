package com.surge.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.core.utils.IdUtils;
import com.surge.system.domain.entity.SysResource;
import com.surge.system.domain.model.ResourceDTO;
import com.surge.system.repository.SysResourceRepository;
import com.surge.system.service.ISysResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SysResourceServiceImpl implements ISysResourceService {

    private final SysResourceRepository sysResourceRepository;

    @Transactional
    @Override
    public void saveOrUpdate(ResourceDTO resourceDTO) {
//        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("resource_type", resourceDTO.getResourceType());
//        queryWrapper.eq("org_id", resourceDTO.getOrgId());
//        sysResourceRepository.remove(queryWrapper);
//
//        List<SysResource> sysResources = new ArrayList<>();
//        for(Long resourceId : resourceDTO.getResourceIds()) {
//            SysResource sysResource = new SysResource();
//            sysResource.setId(IdUtils.generateLongId());
//            sysResource.setResourceId(resourceId);
//            sysResource.setResourceType(resourceDTO.getResourceType());
//            sysResource.setOrgId(resourceDTO.getOrgId());
//            sysResources.add(sysResource);
//        }
//        sysResourceRepository.saveBatch(sysResources, 100);
    }
}
