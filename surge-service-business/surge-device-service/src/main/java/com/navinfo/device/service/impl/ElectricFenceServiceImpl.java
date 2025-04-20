package com.surge.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.core.result.R;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.common.core.utils.StringUtils;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.ElectricFence;
import com.surge.device.domain.entity.ElectricFenceDevice;
import com.surge.device.domain.request.ElectricFenceDeviceBindReq;
import com.surge.device.repository.DeviceInstanceRepository;
import com.surge.device.repository.ElectricFenceDeviceMapper;
import com.surge.device.repository.ElectricFenceDeviceRepository;
import com.surge.device.repository.ElectricFenceMapper;
import com.surge.device.service.IElectricFenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ElectricFenceServiceImpl implements IElectricFenceService {

    private final ElectricFenceMapper electricFenceMapper;
    private final ElectricFenceDeviceMapper electricFenceDeviceMapper;
    private final ElectricFenceDeviceRepository electricFenceDeviceRepository;
    private final DeviceInstanceRepository deviceInstanceRepository;

    @Override
    public void createElectricFence(ElectricFence electricFence) {
        electricFenceMapper.insertOne(electricFence);
    }

    @Override
    public void updateElectricFence(ElectricFence electricFence) {
        electricFenceMapper.updateById(electricFence);
    }

    @Override
    public ElectricFence queryById(Long id) {
        ElectricFence electricFence = electricFenceMapper.selectById(id);
        List<Long> deviceIds = electricFenceDeviceRepository.queryDeviceIds(id);
        if(deviceIds != null && deviceIds.size() > 0) {
            List<DeviceInstance> deviceInstances = deviceInstanceRepository.queryByIds(deviceIds);
            electricFence.setDeviceInstances(deviceInstances);
        }
        return electricFence;
    }

    @Override
    public void removeElectricFence(Long id) {
        electricFenceMapper.deleteById(id);
        QueryWrapper<ElectricFenceDevice> wrapper = new QueryWrapper<>();
        wrapper.eq("fence_id", id);
        electricFenceDeviceRepository.remove(wrapper);
    }

    @Override
    public List<ElectricFence> queryByDeviceId(Long deviceId) {
        return electricFenceMapper.queryByDeviceId(deviceId);
    }

    @Override
    public List<ElectricFence> queryList(QueryWrapper<ElectricFence> queryWrapper) {
        return electricFenceMapper.selectList(queryWrapper);
    }

    @Override
    public PageInfo<ElectricFence> queryPage(QueryWrapper<ElectricFence> queryWrapper, PageInfo<ElectricFence> page) {
        return electricFenceMapper.selectList(queryWrapper, page);
    }

    @Override
    public void addElectricFenceDevices(ElectricFenceDeviceBindReq electricFenceDeviceBindReq) {
        if(electricFenceDeviceBindReq.getFenceId() == null ||
                electricFenceDeviceBindReq.getDeviceIds() == null ||
                electricFenceDeviceBindReq.getDeviceIds().size() == 0) {
            return;
        }
        List<ElectricFenceDevice> list = new ArrayList<>(electricFenceDeviceBindReq.getDeviceIds().size());
        for(Long deviceId: electricFenceDeviceBindReq.getDeviceIds()) {
            ElectricFenceDevice electricFenceDevice = new ElectricFenceDevice();
            electricFenceDevice.setFenceId(electricFenceDeviceBindReq.getFenceId());
            electricFenceDevice.setDeviceId(deviceId);
            list.add(electricFenceDevice);
        }
        electricFenceDeviceMapper.insertBatch(list);
    }

    @Override
    public void deleteElectricFenceDevices(ElectricFenceDeviceBindReq electricFenceDeviceBindReq) {
        if(electricFenceDeviceBindReq.getFenceId() == null ||
                electricFenceDeviceBindReq.getDeviceIds() == null ||
                electricFenceDeviceBindReq.getDeviceIds().size() == 0) {
            return;
        }
        LambdaQueryWrapper<ElectricFenceDevice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ElectricFenceDevice::getFenceId, electricFenceDeviceBindReq.getFenceId())
                .in(ElectricFenceDevice::getDeviceId, electricFenceDeviceBindReq.getDeviceIds());
        electricFenceDeviceMapper.delete(wrapper);
    }

    @Override
    public PageInfo<DeviceInstance> queryFenceDevicePage(Long fenceId, PageInfo<DeviceInstance> page) {
        List<Long> deviceIds = electricFenceDeviceRepository.queryDeviceIds(fenceId);
        if(deviceIds != null && deviceIds.size() > 0) {
            QueryWrapper<DeviceInstance> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("tid", deviceIds);
            PageInfo<DeviceInstance> pageInfo =
                    deviceInstanceRepository.queryPage(null, queryWrapper, page);
            return pageInfo;
        }
        return page;
    }
}
