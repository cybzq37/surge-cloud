package com.surge.device.api;

import com.surge.device.domain.entity.SatelliteInfo;

import java.util.List;

public interface RemoteSatelliteInfoService {

    void batchSaveOrUpdate(List<SatelliteInfo> satelliteInfos);
}
