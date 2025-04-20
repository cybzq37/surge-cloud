package com.surge.device.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.JsonUtils;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.core.utils.http.HttpClient;
import com.surge.device.config.ShipConfig;
import com.surge.device.domain.entity.ShipTrace;
import com.surge.device.repository.ShipTraceRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShipTraceService {

    @Autowired
    private ShipConfig shipConfig;

    private final ShipTraceRepository shipTraceRepository;

    public JsonNode queryShipTrace(Long mmsi, Long btm, Long etm) throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("v", "2");
        params.put("k", shipConfig.getKey());
        params.put("enc", "1"); // 返回json
        params.put("scode", "0");  //
        params.put("id", mmsi + ""); // 船舶Id
        params.put("btm", btm + ""); //开始时间
        params.put("etm", etm + ""); //结束时间
        params.put("cut", "0");
        String resp = HttpClient.getInstance().get(shipConfig.getHost() + "/apicall/GetShipTrack", params);
        if(StringUtils.isEmpty(resp)) {
            throw new ServiceException("/apicall/GetAreaShip 接口错误：返回结果为空");
        }

        JsonNode result = JsonUtils.getObjectMapper().readTree(resp);
        if(result.get("status").asInt() != 0){
            throw new ServiceException("接口查询错误：" + result.get("msg").asText());
        }
        // 提取并转换 data 字段为 ShipInfo 列表
        JsonNode dataNode = result.get("points");
        return dataNode;
    }
}
