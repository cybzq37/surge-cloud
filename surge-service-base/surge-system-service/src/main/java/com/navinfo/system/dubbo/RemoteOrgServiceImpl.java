package com.surge.system.dubbo;

import com.surge.system.api.RemoteOrgService;
import com.surge.system.domain.entity.SysOrg;
import com.surge.system.repository.SysOrgMapper;
import com.surge.system.repository.SysOrgRepository;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 部门服务
 *
 * @author lichunqing
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteOrgServiceImpl implements RemoteOrgService {

    private final SysOrgRepository sysOrgRepository;

    private final SysOrgMapper sysOrgMapper;

    @Override
    public List<SysOrg> queryAll() {
        return sysOrgRepository.list();
    }

    @Override
    public List<SysOrg> selectOrgRecursion(Long orgId){
        return sysOrgMapper.selectOrgRecursion(orgId);
    }
}
