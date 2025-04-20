package com.surge.system.service;

import com.surge.system.domain.model.ResourceDTO;

public interface ISysResourceService {

    void saveOrUpdate(ResourceDTO resourceDTO);
}
