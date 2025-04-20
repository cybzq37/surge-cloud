package com.surge.system.dubbo;

import com.surge.system.api.RemoteResourceService;
import com.surge.system.domain.entity.SysResource;
import com.surge.system.repository.SysResourceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@DubboService
public class RemoteResourceServiceImpl implements RemoteResourceService {

    private final SysResourceRepository sysResourceRepository;

    @Override
    public List<SysResource> queryByTypeAndOrgId(Integer type, Long orgId) {
        return sysResourceRepository.queryResouceByTypeAndOrgId(type, orgId);
    }
}
