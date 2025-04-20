package com.surge.device.repository;

import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.device.domain.entity.SatelliteInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备实例 数据层
 *
 * @author lichunqing
 */
@Mapper
public interface SatelliteInfoMapper extends BaseMapperPlus<SatelliteInfoMapper, SatelliteInfo, SatelliteInfo> {


}
