package com.surge.system.api;

import com.surge.system.domain.entity.SysOrg;

import java.util.List;

/**
 * 部门服务
 *
 * @author lichunqing
 */
public interface RemoteOrgService {

    List<SysOrg> queryAll();

    List<SysOrg> selectOrgRecursion(Long orgId);
}
