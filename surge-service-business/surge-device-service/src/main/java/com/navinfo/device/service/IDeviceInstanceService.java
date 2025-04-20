package com.surge.device.service;

import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.DeviceTrace;

import java.util.Date;
import java.util.List;

public interface IDeviceInstanceService {

    DeviceInstance queryById(Long id);

    List<DeviceTrace> queryTrace(Long id, Date start, Date end);

    PageInfo<DeviceInstance> queryPage(DeviceInstance deviceInstance,
                                       PageInfo<DeviceInstance> page);

    void create(DeviceInstance deviceInstance);

    void update(DeviceInstance deviceInstance);

    void delete(Long id);
}
