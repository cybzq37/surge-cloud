package com.surge.device.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.ElectricFence;
import com.surge.device.domain.request.ElectricFenceDeviceBindReq;

import java.util.List;

public interface IElectricFenceService {

    void createElectricFence(ElectricFence electricFence);

    void updateElectricFence(ElectricFence electricFence);

    ElectricFence queryById(Long id);

    void removeElectricFence(Long id);

    List<ElectricFence> queryByDeviceId(Long deviceId);

    List<ElectricFence> queryList(QueryWrapper<ElectricFence> queryWrapper);

    PageInfo<ElectricFence> queryPage(QueryWrapper<ElectricFence> queryWrapper,
                                      PageInfo<ElectricFence> page);

    void addElectricFenceDevices(ElectricFenceDeviceBindReq electricFenceDeviceBindReq);

    void deleteElectricFenceDevices(ElectricFenceDeviceBindReq electricFenceDeviceBindReq);

    /**
     * 查询电子围栏关联设备分页
     * @return
     */
    PageInfo<DeviceInstance> queryFenceDevicePage(Long fenceId, PageInfo<DeviceInstance> page);



}
