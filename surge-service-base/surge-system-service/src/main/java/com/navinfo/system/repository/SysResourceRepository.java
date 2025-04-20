package com.surge.system.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.system.domain.entity.SysResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysResourceRepository extends CrudRepository<SysResourceMapper, SysResource> {

    public List<SysResource> queryResouceByTypeAndOrgId(int type, long orgId) {
        QueryWrapper<SysResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("resource_type", type);
        queryWrapper.eq("org_id", orgId);
        return baseMapper.selectList(queryWrapper);
    }

}
