package com.surge.device.dubbo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.surge.common.core.utils.BeanUtils;
import com.surge.device.api.RemoteSatelliteInfoService;
import com.surge.device.domain.entity.SatelliteInfo;
import com.surge.device.repository.SatelliteInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteSatelliteInfoServiceImpl implements RemoteSatelliteInfoService {

    @Autowired
    private SatelliteInfoRepository satelliteInfoRepository;

    @Override
    public void batchSaveOrUpdate(List<SatelliteInfo> satelliteInfos) {
        log.info("batchSaveOrUpdate satelliteInfos: {}", satelliteInfos.size());
        for(SatelliteInfo satelliteInfo : satelliteInfos) {
            LambdaQueryWrapper<SatelliteInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SatelliteInfo::getStationEpid, satelliteInfo.getStationEpid());
            queryWrapper.eq(SatelliteInfo::getType, satelliteInfo.getType());
            queryWrapper.eq(SatelliteInfo::getNumber, satelliteInfo.getNumber());
            SatelliteInfo old = satelliteInfoRepository.getOne(queryWrapper);
            if(old != null) {
                BeanUtils.copyPropertiesIgnoreNull(satelliteInfo, old);
                log.info("update satelliteInfo:{}", old);
                satelliteInfoRepository.updateById(old);
            } else {
                log.info("save satelliteInfo:{}", old);
                satelliteInfoRepository.save(satelliteInfo);
            }
        }
    }
}
