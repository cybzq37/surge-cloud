package com.surge.device.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceManufacturer;
import com.surge.device.domain.entity.DeviceType;
import com.surge.map.domain.entity.LayerData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备厂商表 数据层
 *
 * @author lichunqing
 */
@Mapper
public interface DeviceManufacturerMapper extends BaseMapperPlus<DeviceManufacturerMapper, DeviceManufacturer, DeviceManufacturer> {

    DeviceManufacturer selectByCode(@Param("code") String code);

    PageInfo<DeviceManufacturer> selectPageVO(
            @Param("name") String name,
            @Param("orgId") String orgId,
            @Param("code") String code,
            @Param(Constants.WRAPPER) Wrapper<DeviceManufacturer> queryWrapper,
            @Param("page") PageInfo<DeviceManufacturer> page);

}
