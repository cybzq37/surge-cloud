package com.surge.device.domain.request;

import lombok.Data;

import java.util.List;

@Data
public class ElectricFenceDeviceBindReq {

    private Long fenceId;
    private List<Long> deviceIds;

}
