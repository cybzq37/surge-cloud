package com.surge.device.dubbo;

import com.surge.common.gis.util.GeomUtils;
import com.surge.device.api.RemoteKafkaRtpdSyncService;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.DeviceTrace;
import com.surge.device.domain.entity.ElectricFence;
import com.surge.device.domain.entity.ElectricFenceEvent;
import com.surge.device.domain.enums.FenceEventStatusEnum;
import com.surge.device.domain.enums.RealtimeDataTraceFlag;
import com.surge.device.domain.kafka.KafkaRtpd;
import com.surge.device.domain.timerules.TimeRuleChecker;
import com.surge.device.repository.*;
import com.surge.device.rules.ElectricFenceRuleContext;
import com.surge.device.rules.ElectricFenceRuleEnum;
import com.surge.device.service.IDeviceInstanceService;
import com.surge.device.service.IElectricFenceEventService;
import com.surge.device.service.IElectricFenceService;
import com.surge.station.api.RemoteSbcEquipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteKafkaRtpdSyncServiceImpl implements RemoteKafkaRtpdSyncService {

    private final IElectricFenceService electricFenceService;
    private final IElectricFenceEventService electricFenceEventService;
    private final IDeviceInstanceService deviceInstanceService;
    private final DeviceInstanceRepository deviceInstanceRepository;
    private final DeviceInstanceMapper deviceInstanceMapper;
    private final ElectricFenceRuleContext electricFenceRuleContext;
    private final DeviceTraceRepository deviceTraceRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceManufacturerRepository deviceManufacturerRepository;

    @DubboReference
    private RemoteSbcEquipService remoteSbcEquipService;

    @Override
    public void syncKafkaRtpdList(List<KafkaRtpd> kafkaRtpdList) {
        List<DeviceTrace> deviceTraces = new ArrayList<>(kafkaRtpdList.size());
        for(KafkaRtpd kafkaRtpd : kafkaRtpdList) {
            DeviceInstance deviceInstance = deviceInstanceRepository.queryOne(Long.valueOf(kafkaRtpd.getMfid()), kafkaRtpd.getEpid());
            if(deviceInstance == null) {
//                SbcEquip sbcEquip = remoteSbcEquipService.querySbcEquip(kafkaRtpd.getEpid());
//                deviceInstance = new DeviceInstance();
//                deviceInstance.setMfid(Long.valueOf(kafkaRtpd.getMfid()));
//                deviceInstance.setEpid(kafkaRtpd.getEpid());
//                deviceInstance.setName(sbcEquip.getEquipName());
//                deviceInstance.setModel(sbcEquip.getEquipFormat());
//                DeviceType deviceType = deviceTypeRepository.createIfNotExist(sbcEquip.getEquipTypeName());
//                if(deviceType == null) {
//                    throw new ServiceException("获取设备类型失败");
//                }
//                deviceInstance.setTypeId(deviceType.getId()); // 根据设备名字获取设备类型Id
//                DeviceManufacturer deviceManufacturer = deviceManufacturerRepository.getById(deviceInstance.getMfid());
//                String orgId = deviceManufacturer.getOrgId(); // 从设备来源获取
//                if(StringUtils.isEmpty(orgId)) {
//                    throw new ServiceException("获取组织机构失败");
//                }
//                deviceInstance.setOrgId(orgId); // 区域转组织机构
//                deviceInstanceRepository.save(deviceInstance);
                continue;
            }
            DeviceTrace deviceTrace = new DeviceTrace();
            deviceTrace.setTs(new Date(kafkaRtpd.getTimestamp()));
            deviceTrace.setTid(deviceInstance.getTid());
            deviceTrace.setMfid(deviceInstance.getMfid());
            deviceTrace.setEpid(deviceInstance.getEpid());
            deviceTrace.setElevation(kafkaRtpd.getElevation());
            deviceTrace.setLon(kafkaRtpd.getLon());
            deviceTrace.setLat(kafkaRtpd.getLat());
            deviceTrace.setSpeed(kafkaRtpd.getSpeed());
            deviceTrace.setCog(kafkaRtpd.getCog());
            deviceTrace.setSource(0); // 默认值
            deviceTrace.setStatus(kafkaRtpd.getStatus()); // 在线状态

            // 检测偏移
//            if(!validTraceBySpeed(deviceTrace)) {
//                deviceTrace.setFlag(RealtimeDataTraceFlag.SPEED_ERROR.ordinal());
//            }
            deviceTraces.add(deviceTrace);
        }
        // 先存储数据
        deviceTraceRepository.saveBatch(deviceTraces, 50);
        for(DeviceTrace deviceTrace : deviceTraces) {
            // 更新实时位置信息
            deviceInstanceMapper.updateDevicePostion(deviceTrace.getTid(),
                    deviceTrace.getTs(),
                    deviceTrace.getLon(),
                    deviceTrace.getLat());
            // 检查电子围栏事件
            checkElectricFence(deviceTrace);
        }
    }


    /*
数据过滤：轨迹数据有可能与当前时间存在较大差别，这种数据是由于断网或关闭应用导致数据没来得及上传，在恢复网络或者第二次打开应用后才被上传
该轨迹数据与后续正常轨迹数据可能会存在较大时间与空间差，显示事件时会造成页面渲染问题，所以不将此数据设为触发事件数据。
如果轨迹数据的事件在3分钟之前，则过滤此数据
*/
    public boolean validTraceByTime(DeviceTrace deviceTrace) {
        if(System.currentTimeMillis() - deviceTrace.getTs().getTime() > 3 *60 * 1000L) {
            deviceTrace.setStatus(RealtimeDataTraceFlag.TIME_ERROR.ordinal()); // 时间错误
            return false;
        }
        return true;
    }

    // // 根据速度进行数据过滤
    public boolean validTraceBySpeed(DeviceTrace deviceTrace) {
        // speed的单位是公里每小时，需要转换成米每秒
        double speed = deviceTrace.getSpeed() * 1000 / 3600;
        // 获取上个经纬度信息
        DeviceInstance deviceInstance = deviceInstanceMapper.selectById(deviceTrace.getTid());
        double[] lastPos = GeomUtils.geoJsonToLonlat(deviceInstance.getLastPosGeoJson().toString());
        double evaluateDistance = speed * (deviceTrace.getTs().getTime() - deviceInstance.getLastPosTime().getTime()) * 1000;
        double actualDistance = GeomUtils.calculateDistance(lastPos[0], lastPos[1],
                deviceTrace.getLon(), deviceTrace.getLat());
        if (actualDistance > evaluateDistance * 1.5) {
            return false;
        }
        return true;
    }

    public void checkElectricFence(DeviceTrace deviceTrace) {
        // 读取设备关联的电子围栏列表
        List<ElectricFence> electricFences = electricFenceService.queryByDeviceId(deviceTrace.getTid());
        if(CollectionUtils.isEmpty(electricFences)) {
            return;
        }
        // 循环电子围栏数据
        for(ElectricFence electricFence: electricFences) {
            // 判断是否在时间范围内
            if(electricFence.getTimeRange() != null) {
                boolean isInTimeRange = TimeRuleChecker.isWithinRules(electricFence.getTimeRange().toString(), deviceTrace.getTs());
                if(!isInTimeRange) { // 时间范围不为空，不在时间范围内，跳过本次循环
                    continue;
                }
            }
            boolean flag = false; //判断是否触发电子围栏事件, 触发规则为true, 没有触发规则为false
            if (String.valueOf(electricFence.getRuleType()).equals(ElectricFenceRuleEnum.NO_ENTRY.getType())) { //禁止进入
                flag = electricFenceRuleContext.getRuleByType(ElectricFenceRuleEnum.NO_ENTRY).check(electricFence, deviceTrace);
            } else if (String.valueOf(electricFence.getRuleType()).equals(ElectricFenceRuleEnum.NO_EXIT.getType())) { //禁止离开
                flag = electricFenceRuleContext.getRuleByType(ElectricFenceRuleEnum.NO_EXIT).check(electricFence, deviceTrace);
            }

            // 查询设备deviceId关联的事件信息(告警状态)
            List<ElectricFenceEvent> eventList = electricFenceEventService.queryList(Long.valueOf(deviceTrace.getTid()),
                    FenceEventStatusEnum.ALARMING.getCode());

            if(eventList.size() > 0) { // 存在关联的事件
                if(flag) { // 存在关联的告警事件，本次触发告警，更新最后一次上报时间
                    eventList.forEach(event -> event.setLastTraceTime(deviceTrace.getTs()));
                } else { // 存在关联的告警事件，本次未触发告警，自动关闭告警
                    eventList.forEach(event -> {
                        event.setEndTime(new Date());
                        event.setLastTraceTime(deviceTrace.getTs());
                        event.setStatus(FenceEventStatusEnum.CANCELED.getCode());
                    });
                }
                electricFenceEventService.batchUpdate(eventList);
                // 获取字符串id集合
                String eventIds = eventList.stream().map(ElectricFenceEvent::getId).map(String::valueOf).collect(Collectors.joining(","));
                deviceTrace.setEvent(eventIds);

            } else { // 不存在关联的事件
                if(flag) { // 不存在关联的告警事件，并触发告警，新增告警
                    ElectricFenceEvent electricFenceEvent = new ElectricFenceEvent();
                    electricFenceEvent.setDeviceId(Long.valueOf(deviceTrace.getTid()));
                    electricFenceEvent.setFenceId(electricFence.getId());
                    electricFenceEvent.setEventType(electricFence.getRuleType());
                    electricFenceEvent.setTraceTime(deviceTrace.getTs());
                    electricFenceEvent.setLastTraceTime(deviceTrace.getTs());
                    electricFenceEvent.setStartTime(new Date());
                    DeviceInstance deviceInstance = deviceInstanceRepository.queryById(deviceTrace.getTid());
                    electricFenceEvent.setOrgId(deviceInstance.getOrgId());
                    electricFenceEvent.setStatus(FenceEventStatusEnum.ALARMING.getCode());
                    electricFenceEventService.create(electricFenceEvent);
                    deviceTrace.setEvent(electricFence.getId() + "");
                } else { // 不存在关联的告警事件，未触发告警，忽略
                    continue;
                }
            }
        }
    }
}
