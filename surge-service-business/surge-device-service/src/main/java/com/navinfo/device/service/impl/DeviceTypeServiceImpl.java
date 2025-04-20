package com.surge.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceType;
import com.surge.device.repository.DeviceTypeMapper;
import com.surge.device.service.IDeviceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@Service
public class DeviceTypeServiceImpl implements IDeviceTypeService {

    private final DeviceTypeMapper deviceTypeMapper;

    @Override
    public DeviceType queryById(Long id) {
        return deviceTypeMapper.selectById(id);
    }

    @Override
    public PageInfo<DeviceType> queryPage(String catelog,
                                          String code,
                                          String name,
                                          String model,
                                          PageInfo<DeviceType> page) {
        LambdaQueryWrapper<DeviceType> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isNotBlank(catelog), DeviceType::getCatelog, catelog);
        lqw.like(StringUtils.isNotBlank(code), DeviceType::getCode, code);
        lqw.like(StringUtils.isNotBlank(name), DeviceType::getName, name);
        lqw.like(StringUtils.isNotBlank(model), DeviceType::getModel, model);
        return deviceTypeMapper.selectPage(page, lqw);
    }

    @Override
    public void create(DeviceType deviceType) {
        deviceTypeMapper.insert(deviceType);
    }

    @Override
    public void update(DeviceType deviceType) {
        deviceTypeMapper.updateById(deviceType);
    }

    @Override
    public void delete(Long id) {
        deviceTypeMapper.deleteById(id);
    }

    @Override
    public List<DeviceType> queryAll() {
        return deviceTypeMapper.selectList();
    }
}
