package com.surge.device.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备类型表 数据层
 *
 * @author lichunqing
 */
@Mapper
public interface DeviceTypeMapper extends BaseMapperPlus<DeviceTypeMapper, DeviceType, DeviceType> {

    List<DeviceType> selectList(@Param(Constants.WRAPPER) Wrapper<DeviceType> queryWrapper);

    List<DeviceType> selectList(@Param(Constants.WRAPPER) Wrapper<DeviceType> queryWrapper,
                               @Param("page") PageInfo<DeviceType> page);

    @Override
    int updateById(@Param("et") DeviceType entity);

    DeviceType selectByCode(@Param("code") String code);
}