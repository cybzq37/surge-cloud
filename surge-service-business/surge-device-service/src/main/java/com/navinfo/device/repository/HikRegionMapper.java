package com.surge.device.repository;

import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.device.domain.entity.HikRegion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HikRegionMapper extends BaseMapperPlus<HikRegionMapper, HikRegion, HikRegion> {

}
