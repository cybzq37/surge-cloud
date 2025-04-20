package com.surge.device.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceInstance;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DeviceInstanceRepository extends CrudRepository<DeviceInstanceMapper, DeviceInstance> {

    public List<Map> queryDeviceStatusByType(List<String> orgIds) {
        return getBaseMapper().queryDeviceStatusByType(orgIds);
    }

    public DeviceInstance queryById(Long tid) {
        return getBaseMapper().selectById(tid);
    }

    public DeviceInstance queryOne(Long mfid, String epid) {
        QueryWrapper<DeviceInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mfid", mfid);
        queryWrapper.eq("epid", epid);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public List<DeviceInstance> queryByIds(List<Long> tids) {
        return getBaseMapper().selectByIds(tids);
    }

    public List<DeviceInstance> queryList(JsonNode fieldInfo,
                                          Wrapper<DeviceInstance> queryWrapper) {
        return getBaseMapper().queryList(fieldInfo, queryWrapper);
    }

    public PageInfo<DeviceInstance> queryPage(JsonNode fieldInfo,
                                              Wrapper<DeviceInstance> queryWrapper,
                                              PageInfo<DeviceInstance> pageInfo) {
        return getBaseMapper().queryList(fieldInfo, queryWrapper, pageInfo);
    }

    public boolean updateById(DeviceInstance deviceInstance) {
        return getBaseMapper().updateById(deviceInstance) > 1;
    }
}
