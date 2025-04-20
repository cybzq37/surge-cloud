package com.surge.device.service.impl;

import com.surge.device.repository.SatelliteInfoRepository;
import com.surge.device.service.ISatelliteInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Service
@DubboService
@RequiredArgsConstructor
public class SatelliteInfoServiceImpl implements ISatelliteInfoService {

    private final SatelliteInfoRepository satelliteInfoRepository;

}
