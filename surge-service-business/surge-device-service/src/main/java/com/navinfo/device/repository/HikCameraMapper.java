package com.surge.device.repository;

import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.device.domain.entity.HikCamera;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HikCameraMapper extends BaseMapperPlus<HikCameraMapper, HikCamera, HikCamera> {

}
