package com.surge.device.dubbo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.BeanUtils;
import com.surge.common.core.utils.JsonUtils;
import com.surge.device.api.RemoteKafkaSbtzSyncService;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.DeviceManufacturer;
import com.surge.device.domain.entity.DeviceType;
import com.surge.device.domain.kafka.KafkaSbtz;
import com.surge.device.repository.DeviceInstanceRepository;
import com.surge.device.repository.DeviceManufacturerRepository;
import com.surge.device.repository.DeviceTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteKafkaSbtzSyncServiceImpl implements RemoteKafkaSbtzSyncService {

    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceInstanceRepository deviceInstanceRepository;
    private final DeviceManufacturerRepository deviceManufacturerRepository;

    @Override
    public void syncKafkaSbtzList(List<KafkaSbtz> sbtzList) {
        for (KafkaSbtz kafkaSbtz : sbtzList) {
            if(3 == kafkaSbtz.getOperation()) { // 删除操作
                DeviceInstance deviceInstance = deviceInstanceRepository.queryOne(Long.valueOf(kafkaSbtz.getMfid()), kafkaSbtz.getEpid());
                if(deviceInstance != null) {
                    deviceInstance.setDeleteFlag(1);
                    deviceInstanceRepository.updateById(deviceInstance);
                }
            } else { // 新增或更新
                DeviceInstance waitForUpdate = deviceInstanceRepository.queryOne(Long.valueOf(kafkaSbtz.getMfid()), kafkaSbtz.getEpid());
                if(waitForUpdate == null) { // 新增操作
                    waitForUpdate = new DeviceInstance();
                    waitForUpdate.setMfid(Long.valueOf(kafkaSbtz.getMfid())); // mfid写死，来源rtk设备
                    waitForUpdate.setEpid(kafkaSbtz.getEpid());
                    waitForUpdate.setName(kafkaSbtz.getDeviceName());
                    waitForUpdate.setModel(kafkaSbtz.getDeviceModel());
                    waitForUpdate.setOwnerName(kafkaSbtz.getOwnerName());
                    waitForUpdate.setOwnerId(kafkaSbtz.getOwnerId());
                    waitForUpdate.setOwnerIdType(kafkaSbtz.getOwnerIdType());
                    // 更新设备类别
                    DeviceType deviceType = deviceTypeRepository.createIfNotExist(kafkaSbtz.getDeviceType());
                    if(deviceType == null) {
                        throw new ServiceException("获取设备类型失败");
                    }
                    waitForUpdate.setTypeId(deviceType.getId()); // 根据设备名字获取设备类型Id
                    waitForUpdate.setTypeName(kafkaSbtz.getDeviceType());
                    // 更新设备属性
                    waitForUpdate.setFieldInfo(kafkaSbtz.getExtend());
                    // 设置组织机构Id
                    DeviceManufacturer deviceManufacturer = deviceManufacturerRepository.getById(waitForUpdate.getMfid());
                    if(deviceManufacturer == null) {
                        throw new ServiceException("设备组织机构Id不存在");
                    }
                    waitForUpdate.setOrgId(deviceManufacturer.getOrgId());
                    deviceInstanceRepository.save(waitForUpdate);
                } else { // 修改操作
                    waitForUpdate.setName(kafkaSbtz.getDeviceName());
                    waitForUpdate.setModel(kafkaSbtz.getDeviceModel());
                    waitForUpdate.setOwnerName(kafkaSbtz.getOwnerName());
                    waitForUpdate.setOwnerId(kafkaSbtz.getOwnerId());
                    waitForUpdate.setOwnerIdType(kafkaSbtz.getOwnerIdType());
                    // 更新设备类别
                    DeviceType deviceType = deviceTypeRepository.createIfNotExist(kafkaSbtz.getDeviceType());
                    if(deviceType == null) {
                        throw new ServiceException("获取设备类型失败");
                    }
                    waitForUpdate.setTypeId(deviceType.getId()); // 根据设备名字获取设备类型Id
                    waitForUpdate.setTypeName(kafkaSbtz.getDeviceType());
                    // 更新设备属性
                    ObjectNode newFieldInfo;
                    if(waitForUpdate.getFieldInfo() == null) {
                        newFieldInfo = JsonUtils.getObjectMapper().createObjectNode();
                    } else {
                        newFieldInfo = (ObjectNode) waitForUpdate.getFieldInfo();
                    }
                    if(kafkaSbtz.getExtend() != null) {
                        ObjectNode extendNode = JsonUtils.getObjectMapper().convertValue(kafkaSbtz.getExtend(), ObjectNode.class);
                        newFieldInfo.putAll(extendNode);
                    }
                    waitForUpdate.setFieldInfo(newFieldInfo);
                    deviceInstanceRepository.updateById(waitForUpdate);
                }
            }
        }
    }
}
