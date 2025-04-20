package com.surge.device.service;

import com.surge.device.domain.entity.ElectricFenceEvent;

import java.util.List;

public interface IElectricFenceEventService {

    void create(ElectricFenceEvent electricFenceEvent);

    void batchUpdate(List<ElectricFenceEvent> electricFenceEvents);

    List<ElectricFenceEvent> queryList(Long deviceId, Integer... status);

}
