package com.surge.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.gis.util.GeomUtils;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.DeviceTrace;
import com.surge.device.domain.entity.ElectricFenceDevice;
import com.surge.device.domain.entity.ElectricFenceEvent;
import com.surge.device.repository.DeviceInstanceRepository;
import com.surge.device.repository.DeviceTraceRepository;
import com.surge.device.repository.ElectricFenceDeviceMapper;
import com.surge.device.repository.ElectricFenceEventMapper;
import com.surge.device.service.IDeviceInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DeviceInstanceServiceImpl implements IDeviceInstanceService {

    private final DeviceInstanceRepository deviceInstanceRepository;
    private final ElectricFenceDeviceMapper electricFenceDeviceMapper;
    private final ElectricFenceEventMapper electricFenceEventMapper;
    private final DeviceTraceRepository deviceTraceRepository;

    @Override
    public DeviceInstance queryById(Long id) {
        return deviceInstanceRepository.queryById(id);
    }

    @Override
    public List<DeviceTrace> queryTrace(Long id, Date start, Date end) {
        List<DeviceTrace> list = deviceTraceRepository.queryTrace(id, start, end);
        return list;
    }

    @Override
    public PageInfo<DeviceInstance> queryPage(DeviceInstance deviceInstance,
                                              PageInfo<DeviceInstance> page) {
        LambdaQueryWrapper<DeviceInstance> lambdaQueryWrapper =
                new LambdaQueryWrapper<DeviceInstance>()
                .eq(deviceInstance.getTid() != null, DeviceInstance::getTid, deviceInstance.getTid())
                .eq(deviceInstance.getMfid() != null, DeviceInstance::getMfid, deviceInstance.getMfid())
                .eq(StringUtils.isNotBlank(deviceInstance.getEpid()), DeviceInstance::getEpid, deviceInstance.getEpid())
                .eq(StringUtils.isNotBlank(deviceInstance.getName()), DeviceInstance::getName, deviceInstance.getName())
                .eq(deviceInstance.getTypeId() != null, DeviceInstance::getTypeId, deviceInstance.getTypeId())
                .eq(deviceInstance.getOrgId() != null, DeviceInstance::getOrgId, deviceInstance.getOrgId());
        PageInfo<DeviceInstance> pageInfo = deviceInstanceRepository.queryPage(deviceInstance.getFieldInfo(), lambdaQueryWrapper, page);
        return pageInfo;
    }

    @Override
    public void create(DeviceInstance deviceInstance) {
        LambdaQueryWrapper<DeviceInstance> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DeviceInstance::getEpid, deviceInstance.getEpid());
        lqw.eq(DeviceInstance::getMfid, deviceInstance.getMfid());
        if(deviceInstanceRepository.count(lqw) > 0) {
            throw new ServiceException("设备epid编码已存在");
        }
        if(deviceInstance.getLastPosGeoJson() != null) {
            deviceInstance.setLastPosGeom(GeomUtils.geoJsonToWkt(deviceInstance.getLastPosGeoJson().toString()));
        }
        deviceInstanceRepository.save(deviceInstance);
    }

    @Override
    public void update(DeviceInstance deviceInstance) {
        DeviceInstance old = deviceInstanceRepository.queryById(deviceInstance.getTid());
        if (old == null) {
            throw new ServiceException("设备不存在！");
        }
//        if(!old.getEpid().equals(deviceInstance.getEpid())) {
//            throw new ServiceException("设备epid编码不允许修改");
//        }
        deviceInstanceRepository.updateById(deviceInstance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {
        // 删除设备及其关联事件
        deviceInstanceRepository.removeById(id);
        electricFenceDeviceMapper.delete(new LambdaQueryWrapper<ElectricFenceDevice>().eq(ElectricFenceDevice::getDeviceId, id));
        electricFenceEventMapper.delete(new LambdaQueryWrapper<ElectricFenceEvent>().eq(ElectricFenceEvent::getDeviceId, id));
    }
}
