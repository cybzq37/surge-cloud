package com.surge.device.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.device.domain.entity.ElectricFenceDevice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ElectricFenceDeviceRepository extends CrudRepository<ElectricFenceDeviceMapper, ElectricFenceDevice> {

    public List<Long> queryDeviceIds(Long fenceId) {
        QueryWrapper<ElectricFenceDevice> wrapper = new QueryWrapper<>();
        wrapper.eq("fence_id", fenceId);
        List<ElectricFenceDevice> list = this.list(wrapper);
        return list.stream().map(ElectricFenceDevice::getDeviceId).collect(Collectors.toList());
    }
}
