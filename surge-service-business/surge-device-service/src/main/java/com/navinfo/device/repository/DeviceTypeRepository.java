package com.surge.device.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.surge.common.core.utils.JsonUtils;
import com.surge.common.core.utils.PinyinUtils;
import com.surge.device.domain.entity.DeviceType;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceTypeRepository extends CrudRepository<DeviceTypeMapper, DeviceType> {

    public DeviceType queryByCode(String code) {
        QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        return getOne(queryWrapper);
    }

    public DeviceType queryByName(String name) {
        QueryWrapper<DeviceType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return getBaseMapper().selectOne(queryWrapper, false);
    }

    public DeviceType createIfNotExist(String name) {
        DeviceType deviceType = queryByName(name);
        if (deviceType == null) {
            deviceType = new DeviceType();
            deviceType.setName(name);
            deviceType.setCode(PinyinUtils.convertToPinyin(name));
            ObjectNode dataStyle = JsonUtils.getObjectMapper().createObjectNode();
            dataStyle.put("id", "1342129182935724032"); //TODO：设置默认的模型地址
            super.save(deviceType);
        }
        return deviceType;
    }
}