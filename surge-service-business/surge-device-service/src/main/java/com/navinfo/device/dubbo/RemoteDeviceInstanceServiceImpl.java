package com.surge.device.dubbo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.BeanUtils;
import com.surge.common.core.utils.JsonUtils;
import com.surge.common.core.utils.StringUtils;
import com.surge.device.api.RemoteDeviceInstanceService;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.DeviceManufacturer;
import com.surge.device.domain.entity.DeviceType;
import com.surge.device.repository.DeviceInstanceRepository;
import com.surge.device.repository.DeviceManufacturerRepository;
import com.surge.device.repository.DeviceTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteDeviceInstanceServiceImpl implements RemoteDeviceInstanceService {

    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceInstanceRepository deviceInstanceRepository;
    private final DeviceManufacturerRepository deviceManufacturerRepository;

    @Override
    public void saveOrUpdateByEpid(String deviceTypeCode, DeviceInstance deviceInstance) {
        DeviceType deviceType = deviceTypeRepository.queryByCode(deviceTypeCode);
        deviceInstance.setTypeId(deviceType.getId());

        QueryWrapper<DeviceInstance> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("epid", deviceInstance.getEpid());
        queryWrapper.eq("type_id", deviceType.getId());
        DeviceInstance old = deviceInstanceRepository.getOne(queryWrapper);

        if(old != null) {
            BeanUtils.copyPropertiesIgnoreNull(deviceInstance, old);
            deviceInstanceRepository.updateById(old);
        } else {
            deviceInstanceRepository.save(deviceInstance);
        }
    }

    @Override
    public void saveOrUpdate(DeviceInstance deviceInstance) {
        DeviceInstance old = deviceInstanceRepository.getBaseMapper().selectById(deviceInstance.getTid());
        if(old == null) {
            old = deviceInstanceRepository.queryOne(deviceInstance.getMfid(), deviceInstance.getEpid());
        }
        if(old == null) {
            old = deviceInstance;
        }
        // 设备属性
        ObjectNode newFieldInfo;
        if(old.getFieldInfo() == null) {
            newFieldInfo = (ObjectNode) old.getFieldInfo();
        } else {
            newFieldInfo = JsonUtils.getObjectMapper().createObjectNode();
        }
        if(deviceInstance.getFieldInfo() != null) {
            BeanUtils.copyPropertiesIgnoreNull(deviceInstance.getFieldInfo(), newFieldInfo);
        }
        BeanUtils.copyPropertiesIgnoreNull(deviceInstance, old);
        old.setFieldInfo(newFieldInfo);
        // 设备类别
        if(old.getTypeId() == null) {
            DeviceType deviceType = null;
            if(old.getTypeCode() != null) {
                deviceType = deviceTypeRepository.queryByCode(old.getTypeCode());
            }
            if(deviceType == null && StringUtils.isNotBlank(old.getTypeName())) {
                deviceType = deviceTypeRepository.createIfNotExist(old.getTypeName());
            }
            old.setTypeId(deviceType.getId());
        }
        // 设置组织机构Id
        if(old.getOrgId() == null) {
            DeviceManufacturer deviceManufacturer = deviceManufacturerRepository.getById(old.getMfid());
            if(deviceManufacturer == null) {
                throw new ServiceException("设备组织机构Id不存在");
            }
            old.setOrgId(deviceManufacturer.getOrgId());
        }

        //
        if(old.getTid() == null) {
            deviceInstanceRepository.save(old);
        } else {
            deviceInstanceRepository.updateById(old);
        }
    }



}
