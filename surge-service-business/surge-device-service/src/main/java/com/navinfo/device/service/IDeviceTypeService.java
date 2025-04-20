package com.surge.device.service;

import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceType;

import java.util.List;

public interface IDeviceTypeService {
    
    DeviceType queryById(Long id);

    PageInfo<DeviceType> queryPage(String catelog,
                              String code,
                              String name,
                              String model,
                              PageInfo<DeviceType> page);

    void create(DeviceType deviceManufacturer);

    void update(DeviceType deviceManufacturer);

    void delete(Long id);

    List<DeviceType> queryAll();

}
