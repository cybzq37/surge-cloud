package com.surge.device.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.result.R;
import com.surge.common.core.utils.JsonUtils;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.core.utils.http.HttpClient;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.config.ShipConfig;
import com.surge.device.domain.entity.SatelliteInfo;
import com.surge.device.domain.entity.ShipTrace;
import com.surge.device.repository.ShipTraceRepository;
import com.surge.device.service.ShipTraceService;
import com.surge.device.task.ShipTraceTask;
import com.surge.map.api.RemoteDataEntryService;
import com.surge.map.domain.entity.DataEntrySet;
import com.surge.station.api.RemoteShipVisitService;
import com.surge.station.domain.entity.ljyard.ShipVisit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "船舶接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/ship")
public class ShipController {

    @Autowired
    private ShipConfig shipConfig;

    private final ShipTraceRepository shipTraceRepository;
    private final ShipTraceService shipTraceService;
    private final ShipTraceTask shipTraceTask;
    @DubboReference
    private RemoteDataEntryService remoteDataEntryService;

    @DubboReference
    private RemoteShipVisitService remoteShipVisitService;

    @Operation(summary = "多船查询")
    @GetMapping("/manyShip")
    public R<PageInfo<SatelliteInfo>> queryManyShip() {
        Map<String,String> params = new HashMap<>();
        params.put("v", "2");
        params.put("k", shipConfig.getKey());
        params.put("enc", "1"); // 返回json
        params.put("id", "1");  // 船舶Id
//        params.put("jsf", "func"); //jsonp 回调
        HttpClient.getInstance().get("/apicall/GetManyShip", null, params);
        return R.ok();
    }

    @Operation(summary = "区域船舶查询")
    @GetMapping("/areaShip")
    public R areaShip(String area) throws JsonProcessingException {
        String[] lonlats = area.split("\\;");
        if(lonlats.length != 4) {
            throw new ServiceException("区域坐标错误");
        }
        List<String> lonlatList = new ArrayList<>(4);
        for(String s : lonlats){
            String[] lonlat = s.split("\\,");
            double lon = Double.parseDouble(lonlat[0]);
            double lat = Double.parseDouble(lonlat[1]);
            long llon = (long) (lon * 1000000);
            long llat = (long) (lat * 1000000);
            String llonlat = llon + "," + llat;
            lonlatList.add(llonlat);
        }

        Map<String,String> params = new HashMap<>();
        params.put("v", "2");
        params.put("k", shipConfig.getKey());
        params.put("enc", "1"); // 返回json
        params.put("scode", "0");  // 船舶Id
        params.put("xy", lonlatList.stream().collect(Collectors.joining("-")));
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
                .collect(Collectors.toList());                              // 收集结果
        return R.ok(filterShip);
    }

    @Operation(summary = "船舶轨迹查询")
    @GetMapping("/task")
    public R task() throws JsonProcessingException {
        shipTraceTask.task();
        return R.ok();
    }

    @Operation(summary = "船舶轨迹查询")
    @GetMapping("/shipTrack")
    public R shipTrack(Long mmsi, Long btm, Long etm) throws Exception {
        JsonNode result = shipTraceService.queryShipTrace(mmsi, btm, etm);
        return R.ok(result);
    }

    @Operation(summary = "按月统计船只数量")
    @GetMapping("/monthlyShipCountStats")
    public R monthlyShipCountStats() throws Exception {
        List<Map> list = shipTraceRepository.getBaseMapper().selectMonthlyShipCountStats();
        return R.ok(list);
    }

    @Operation(summary = "查询在港船舶")
    @GetMapping("/visit")
    public R shipVisit() {
        List<ShipVisit> shipVisits = remoteShipVisitService.queryAllBerthShipVisit();
        for(ShipVisit shipVisit : shipVisits){
            DataEntrySet entrySet = remoteDataEntryService.queryOne(null, "stakeId", shipVisit.getBeginBollardCode());
            if(entrySet != null){
                shipVisit.setPosGeoJson(entrySet.getGeoJson());
            }
        }
        return R.ok(shipVisits);
    }

}
