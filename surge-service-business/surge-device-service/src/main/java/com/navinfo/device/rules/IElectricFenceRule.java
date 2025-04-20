package com.surge.device.rules;

import com.surge.device.domain.entity.DeviceTrace;
import com.surge.device.domain.entity.ElectricFence;

public interface IElectricFenceRule {

    boolean check(ElectricFence electricFence, DeviceTrace deviceTrace);
}
