package com.surge.system.repository;

import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.system.domain.entity.SysResource;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysResourceMapper extends BaseMapperPlus<SysResourceMapper, SysResource, SysResource> {

}
