package com.surge.device.repository;

import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.device.domain.entity.ElectricFenceDevice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 电子围栏设备关联关系 数据层
 *
 * @author lichunqing
 */
@Mapper
public interface ElectricFenceDeviceMapper extends BaseMapperPlus<ElectricFenceDeviceMapper, ElectricFenceDevice, ElectricFenceDevice> {

}
