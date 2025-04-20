package com.surge.device.service;

import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceManufacturer;

public interface IDeviceManufacturerService {

    DeviceManufacturer queryById(Long id);

    void create(DeviceManufacturer deviceManufacturer);

    void update(DeviceManufacturer deviceManufacturer);

    void delete(Long id);
}
