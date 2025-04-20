package com.surge.device.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.device.domain.entity.DeviceTrace;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class DeviceTraceRepository extends CrudRepository<DeviceTraceMapper, DeviceTrace> {

    public List<DeviceTrace> queryTrace(Long tid, Date start, Date end) {
        QueryWrapper<DeviceTrace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tid", tid);
        // 处理时间范围条件
        if (start != null && end != null) {
            queryWrapper.between("ts", start, end);
        } else if (start != null) {
            queryWrapper.ge("ts", start);  // 大于等于开始时间
        } else if (end != null) {
            queryWrapper.le("ts", end);    // 小于等于结束时间
        }
        queryWrapper.orderByAsc("ts");     // 按时间升序排列
        return getBaseMapper().selectList(queryWrapper);
    }
}