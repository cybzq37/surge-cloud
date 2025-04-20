package com.surge.system.api;

import com.surge.system.domain.entity.SysResource;

import java.util.List;

public interface RemoteResourceService {

    List<SysResource> queryByTypeAndOrgId(Integer type, Long orgId);
}
