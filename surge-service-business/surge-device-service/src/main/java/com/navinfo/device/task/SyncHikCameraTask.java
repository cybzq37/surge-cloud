package com.surge.device.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.surge.common.core.utils.BeanUtils;
import com.surge.common.core.utils.JsonUtils;
import com.surge.common.core.utils.StringUtils;
import com.surge.device.domain.entity.*;
import com.surge.device.hik.CameraResourceApi;
import com.surge.device.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class SyncHikCameraTask {

    @Autowired
    public CameraResourceApi cameraResourceApi;

    @Autowired
    public HikRegionRepository hikRegionRepository;

    @Autowired
    public HikCameraRepository hikCameraRepository;

    @Autowired
    public DeviceTypeMapper deviceTypeMapper;
    @Autowired
    public DeviceManufacturerMapper deviceManufacturerMapper;
    @Autowired
    private DeviceInstanceMapper deviceInstanceMapper;

    @Scheduled(cron = "0 10 2 * * ?")
    public void syncHikRegion() throws Exception {
        String regionsRoot = cameraResourceApi.getRegionsRoot();
        JSONObject root = JSON.parseObject(regionsRoot);
        JSONObject rootData = root.getJSONObject("data");
        List<HikRegion> regionIndexBeans = new ArrayList<>();
        cameraResourceApi.showAllRegions(rootData.getString("indexCode"), regionIndexBeans);
        hikRegionRepository.saveOrUpdateBatch(regionIndexBeans, 100);
        // 组织机构org_id通过数据库表手动维护，每次同步时更新下级组织机构
        LambdaQueryWrapper<HikRegion> lqw = new LambdaQueryWrapper<>();
        lqw.isNotNull(HikRegion::getOrgId);
        List<HikRegion> regions = hikRegionRepository.list(lqw);
        for(HikRegion region : regions) {
            // 更新所有子组织机构Id
            LambdaUpdateWrapper<HikRegion> lw = new LambdaUpdateWrapper<>();
            lw.like(HikRegion::getRegionPath, region.getIndexCode());
            HikRegion updateBean = new HikRegion();
            updateBean.setOrgId(region.getOrgId());
            hikRegionRepository.update(updateBean, lw);
        }
    }

    @Scheduled(cron = "0 20 2 * * ?")
    public void syncHikCamera() throws Exception {
        // 区域信息
        List<HikRegion> regionIndexBeans = hikRegionRepository.list();
        log.info("regionIndexBeans:{}", regionIndexBeans.size());
        // 摄像头信息
        List<HikCamera> cameraIndexBeans = new ArrayList<>();
        if (!CollectionUtils.isEmpty(regionIndexBeans)) {
            for (HikRegion bean : regionIndexBeans) {
                cameraResourceApi.showAllCameras(bean.getIndexCode(), cameraIndexBeans);
            }
        }
        log.info("cameraIndexBeans:{}", cameraIndexBeans.size());
        hikCameraRepository.saveOrUpdateBatch(cameraIndexBeans, 100);
    }

    @Scheduled(cron = "0 30 2 * * ?")
    public void syncHikCameraToDeviceInstance() throws Exception {
        LambdaQueryWrapper<HikCamera> lqw = new LambdaQueryWrapper<>();
        lqw.like(HikCamera::getResourceType, "camera");
        List<HikCamera> cameras = hikCameraRepository.list(lqw);
        for(HikCamera camera : cameras) {
            HikRegion hikRegion = hikRegionRepository.getOne(new LambdaQueryWrapper<HikRegion>()
                    .eq(HikRegion::getIndexCode, camera.getRegionIndexCode()));
            if(StringUtils.isEmpty(hikRegion.getOrgId())) { // 如果组织机构不存在，则跳过
                continue;
            }
            DeviceInstance deviceInstance = new DeviceInstance();
            DeviceManufacturer manufacturer = deviceManufacturerMapper.selectByCode("hikvision");
            if(manufacturer != null) {
                deviceInstance.setMfid(manufacturer.getMfid());   // 设备厂商Id
            }
            deviceInstance.setEpid(camera.getIndexCode()); //厂商设备编码
            deviceInstance.setName(camera.getName());

            DeviceType deviceType = deviceTypeMapper.selectByCode("hik_camera");
            if(deviceType != null) {
                deviceInstance.setTypeId(deviceType.getId()); // 设备类型Id
            }
            deviceInstance.setOrgId(hikRegion.getOrgId()); //组织机构Id
            deviceInstance.setFieldInfo(JsonUtils.getObjectMapper().readTree(JsonUtils.toJsonString(camera)));
            deviceInstance.setStatus(0); // 在线
            deviceInstance.setVisible(1); // 可见
            deviceInstance.setUpdateTime(new Date());
            List<DeviceInstance> list = deviceInstanceMapper.queryList(null, new LambdaQueryWrapper<DeviceInstance>().eq(DeviceInstance::getEpid, deviceInstance.getEpid()) );
            if(list != null && list.size() > 0) {
                DeviceInstance old = list.get(0);
                BeanUtils.copyPropertiesIgnoreNull(deviceInstance, old);
                deviceInstanceMapper.updateById(old);
            } else {
                deviceInstanceMapper.insert(deviceInstance);
            }
        }
    }
}
