package com.surge.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.surge.common.core.constant.enums.DelFlagType;
import com.surge.device.domain.entity.ElectricFenceEvent;
import com.surge.device.repository.ElectricFenceEventMapper;
import com.surge.device.service.IElectricFenceEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ElectricFenceEventServiceImpl implements IElectricFenceEventService {

    private final ElectricFenceEventMapper electricFenceEventMapper;

    @Override
    public void create(ElectricFenceEvent electricFenceEvent) {
        electricFenceEvent.setDelFlag(Integer.valueOf(DelFlagType.NOT_DELETED.getFlag()));
        electricFenceEventMapper.insert(electricFenceEvent);
    }

    @Override
    public void batchUpdate(List<ElectricFenceEvent> electricFenceEvents) {
        electricFenceEventMapper.updateBatchById(electricFenceEvents);
    }

    @Override
    public List<ElectricFenceEvent> queryList(Long deviceId, Integer... status) {
        LambdaQueryWrapper<ElectricFenceEvent> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ElectricFenceEvent::getDeviceId, deviceId);
        lqw.in(status != null && status.length > 0, ElectricFenceEvent::getStatus, status);
        lqw.eq(ElectricFenceEvent::getDelFlag, Integer.valueOf(DelFlagType.NOT_DELETED.getFlag()));
        return Collections.emptyList();
    }
}
