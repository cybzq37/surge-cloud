package com.surge.device.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备实例 数据层
 *
 * @author lichunqing
 */
@Mapper
public interface DeviceInstanceMapper extends BaseMapperPlus<DeviceInstanceMapper, DeviceInstance, DeviceInstance> {

    DeviceInstance selectOne(@Param(Constants.WRAPPER) Wrapper<DeviceInstance> queryWrapper);

    DeviceInstance selectById(Long tid);

    List<DeviceInstance> queryList(JsonNode fieldInfo,
                                   @Param(Constants.WRAPPER) Wrapper<DeviceInstance> queryWrapper);

    PageInfo<DeviceInstance> queryList(JsonNode fieldInfo,
                                       @Param(Constants.WRAPPER) Wrapper<DeviceInstance> queryWrapper,
                                       @Param("page") PageInfo<DeviceInstance> page);

    int updateById(@Param("et") DeviceInstance deviceInstance);

    List<Map> queryDeviceStatusByType(@Param("orgIds") List<String> orgIds);

    void updateDevicePostion(@Param("tid") Long tid,
                             @Param("ts") Date ts,
                             @Param("lon") Double lon,
                             @Param("lat") Double lat);

    List<Map> countByCodeAndCategory(@Param("catelog") String catelog,
                                     @Param("orgIds") List<String> orgIds);
}
