package com.surge.device.task;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShipTraceTask {

    @Autowired
    private ShipConfig shipConfig;

    private final ShipTraceRepository shipTraceRepository;

    @Scheduled(cron = "55 5 * * * ?")
    public void task() throws JsonProcessingException {
        Date date = new Date();
        for(String area : shipConfig.getAreas()) {
            Map<String,String> params = buildAreaShipParams(area, shipConfig.getKey());
            String resp = HttpClient.getInstance().get(shipConfig.getHost() + "/apicall/GetAreaShip", params);
            if(StringUtils.isEmpty(resp)) {
                throw new ServiceException("/apicall/GetAreaShip 接口错误：返回结果为空");
            }

            JsonNode result = JsonUtils.getObjectMapper().readTree(resp);
            if(result.get("status").asInt() != 0){
                throw new ServiceException("接口查询错误：" + result.get("msg").asText());
            }
            // 提取并转换 data 字段为 ShipInfo 列表
            JsonNode dataNode = result.get("data");
            if (dataNode == null || !dataNode.isArray()) {
                throw new ServiceException("接口错误：data 字段为空或格式不正确");
            }
            List<ShipTrace> shipTraces = JsonUtils.getObjectMapper().convertValue(dataNode,
                    JsonUtils.getObjectMapper().getTypeFactory().constructCollectionType(List.class, ShipTrace.class));

            List<ShipTrace> filterShip = shipTraces.stream()
                    .filter(shipTrace -> shipTrace.getDestcode().equals("CNNJG")) // 过滤
                    .map(shipTrace -> {
                        shipTrace.setTimestamp(date);
                        shipTrace.setLon(shipTrace.getLon() / 1_000_000);         // 缩放经度
                        shipTrace.setLat(shipTrace.getLat() / 1_000_000);         // 缩放纬度
                        return shipTrace;                                        // 返回修改后的对象
                    })
                    .collect(Collectors.toList());                              // 收集结果
            // 保存到超表
            shipTraceRepository.saveBatch(filterShip, 100);
        }
    }

    public Map<String, String> buildAreaShipParams(String area, String key) {
        Map<String,String> params = new HashMap<>();
        params.put("v", "2");
        params.put("k", key);
        params.put("enc", "1"); // 返回json
        params.put("scode", "0");  // 船舶Id
        params.put("xy", buildAreaParams(area));
        return params;
    }

    public String buildAreaParams(String area){
        String[] coordPairs = area.split(";");
        List<String> transformedCoords = new ArrayList<>(4);
        for (String coordPair : coordPairs) {
            String[] coordinates = coordPair.split(",");
            double longitude = Double.parseDouble(coordinates[0]);
            double latitude = Double.parseDouble(coordinates[1]);
            long scaledLon = (long) (longitude * 1_000_000);
            long scaledLat = (long) (latitude * 1_000_000);
            String scaledCoord = scaledLon + "," + scaledLat;
            transformedCoords.add(scaledCoord);
        }
        return transformedCoords.stream().collect(Collectors.joining("-"));
    }
}
