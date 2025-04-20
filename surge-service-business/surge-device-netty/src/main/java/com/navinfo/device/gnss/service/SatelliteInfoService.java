package com.surge.device.gnss.service;

import com.alibaba.fastjson.JSONObject;
import com.surge.common.core.utils.StringUtils;
import com.surge.device.api.RemoteSatelliteInfoService;
import com.surge.device.domain.entity.SatelliteInfo;
import com.surge.device.gnss.config.NmeaProperties;
import com.surge.minedata.api.RemoteMinedataService;
import com.surge.minedata.domain.entity.OrgEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SatelliteInfoService {

    @Autowired
    public RedissonClient redissonClient;

    public static ConcurrentHashMap<String, String> ORG_INFO = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, SatelliteInfo> SATELLITE_INFO = new ConcurrentHashMap<>();

    @DubboReference
    public RemoteSatelliteInfoService remoteSatelliteInfoService;

    @DubboReference
    public RemoteMinedataService remoteMinedataService;

    public void batchSaveOrUpdate(NmeaProperties nmeaProperties,
                                  List<SatelliteInfo> satelliteInfos) {
        for(SatelliteInfo satelliteInfo : satelliteInfos) {
            String orgId = ORG_INFO.get(nmeaProperties.getName());
            if(StringUtils.isEmpty(orgId)) {
                OrgEntity orgEntity = remoteMinedataService.queryByName(nmeaProperties.getName());
                if(orgEntity != null) {
                    ORG_INFO.put(nmeaProperties.getName(), orgEntity.getId());
                }
            }
            satelliteInfo.setOrgId(ORG_INFO.get(nmeaProperties.getName()));
            String key = String.format("%s:%s:%s", satelliteInfo.getStationEpid(), satelliteInfo.getType(), satelliteInfo.getNumber());
            SATELLITE_INFO.put(key, satelliteInfo);
        }
    }

    @Scheduled(cron = "0 10 1 * * ?")
    public void cronTask() {
        log.info("remote satellite update task executed");
        List<SatelliteInfo> satelliteInfos = new ArrayList<>();
        for(String key : SATELLITE_INFO.keySet()) {
            SatelliteInfo satelliteInfo = SATELLITE_INFO.get(key);
            satelliteInfos.add(satelliteInfo);
        }
        remoteSatelliteInfoService.batchSaveOrUpdate(satelliteInfos);
    }
}
