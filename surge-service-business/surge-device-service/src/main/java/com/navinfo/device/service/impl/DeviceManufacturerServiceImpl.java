package com.surge.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.DeviceManufacturer;
import com.surge.device.repository.DeviceInstanceMapper;
import com.surge.device.repository.DeviceManufacturerMapper;
import com.surge.device.service.IDeviceManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class DeviceManufacturerServiceImpl implements IDeviceManufacturerService {

    private final DeviceManufacturerMapper deviceManufacturerMapper;
    private final DeviceInstanceMapper deviceInstanceMapper;

    @Override
    public DeviceManufacturer queryById(Long id) {
        return deviceManufacturerMapper.selectById(id);
    }

    @Override
    public void create(DeviceManufacturer deviceManufacturer) {
        deviceManufacturerMapper.insert(deviceManufacturer);
    }

    @Override
    public void update(DeviceManufacturer deviceManufacturer) {
        deviceManufacturerMapper.updateById(deviceManufacturer);
    }

    @Override
    public void delete(Long mfid) {
        // 检查是否有关联的设备, 如果有, 不允许删除
        long count = deviceInstanceMapper.selectCount(new LambdaQueryWrapper<DeviceInstance>().eq(DeviceInstance::getMfid, mfid));
        if(count > 0) {
            throw new ServiceException("存在关联设备，无法删除");
        }
        deviceManufacturerMapper.deleteById(mfid);
    }
}
