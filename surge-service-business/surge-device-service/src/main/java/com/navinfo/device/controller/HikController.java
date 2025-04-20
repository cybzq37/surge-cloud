package com.surge.device.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.surge.common.core.result.R;
import com.surge.device.domain.bean.CameraIndexBean;
import com.surge.device.domain.bean.RegionIndexBean;
import com.surge.device.domain.entity.HikCamera;
import com.surge.device.domain.entity.HikRegion;
import com.surge.device.hik.CameraResourceApi;
import com.surge.device.repository.HikCameraRepository;
import com.surge.device.task.SyncHikCameraTask;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "卫星信息接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hik")
public class HikController {

    @Autowired
    private CameraResourceApi cameraResourceApi;

    @GetMapping("/region")
    public R queryRegion() throws Exception {
        String root = cameraResourceApi.getRegionsRoot();
        JSONObject rootJSON = JSON.parseObject(root);
        JSONObject dataJSON = rootJSON.getJSONObject("data");
        // 查询区域组织信息
        List<RegionIndexBean> regionIndexBeans = new ArrayList<>();
        cameraResourceApi.showNoAllRegions(dataJSON.getString("indexCode"), regionIndexBeans);
        return R.ok(regionIndexBeans);
    }

    @GetMapping("/location")
    public R queryLocation() throws Exception {
        String root = cameraResourceApi.getRegionsRoot();
        JSONObject rootJSON = JSON.parseObject(root);
        JSONObject dataJSON = rootJSON.getJSONObject("data");
        // 查询区域组织信息
        List<HikRegion> regionIndexBeans = new ArrayList<>();
        cameraResourceApi.showAllRegions(dataJSON.getString("indexCode"), regionIndexBeans);
        return R.ok(regionIndexBeans);
    }

    @GetMapping("/camera")
    public R queryCamera() throws Exception {
        String root = cameraResourceApi.getRegionsRoot();
        JSONObject rootJSON = JSON.parseObject(root);
        JSONObject dataJSON = rootJSON.getJSONObject("data");
        // 查询区域组织信息
        List<HikRegion> regionIndexBeans = new ArrayList<>();
        cameraResourceApi.showAllRegions(dataJSON.getString("indexCode"), regionIndexBeans);
        // 查询摄像头
        List<HikCamera> cameraIndexBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(regionIndexBeans)) {
            for (HikRegion bean : regionIndexBeans) {
                cameraResourceApi.showAllCameras(bean.getIndexCode(), cameraIndexBeans);
            }
        }
        return R.ok(cameraIndexBeans);
    }

    @GetMapping("/cameraUrl")
    public R queryCameraUrl(String indexCode,
                            @RequestParam(defaultValue = "rtsp") String protocol) throws Exception {
        String result = cameraResourceApi.GetCameraPreviewURL(indexCode, protocol);
        return R.ok(result);
    }

    @Autowired
    private SyncHikCameraTask syncHikCameraTask;

    @GetMapping("/test")
    public R test() throws Exception {
//        syncHikCameraTask.syncHikRegion();
//        syncHikCameraTask.syncHikCamera();
        syncHikCameraTask.syncHikCameraToDeviceInstance();
        return R.ok();
    }
}
