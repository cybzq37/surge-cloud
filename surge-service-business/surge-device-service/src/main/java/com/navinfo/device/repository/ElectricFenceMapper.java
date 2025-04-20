package com.surge.device.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.ElectricFence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 电子围栏表 数据层
 *
 * @author lichunqing
 */
@Mapper
public interface ElectricFenceMapper extends BaseMapperPlus<ElectricFenceMapper, ElectricFence, ElectricFence> {

    void insertOne(ElectricFence electricFence);

    @Override
    int updateById(ElectricFence entity);

    ElectricFence selectById(@Param("id") Long id);

    @Override
    List<ElectricFence> selectList(@Param(Constants.WRAPPER) Wrapper<ElectricFence> queryWrapper);

    PageInfo<ElectricFence> selectList(@Param(Constants.WRAPPER) Wrapper<ElectricFence> queryWrapper,
                               @Param("page") PageInfo<ElectricFence> page);

    List<ElectricFence> queryByDeviceId(@Param("deviceId") Long deviceId);

}
