package com.surge.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.result.R;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.gis.util.GeomUtils;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.ElectricFence;
import com.surge.device.domain.enums.FenceTypeEnum;
import com.surge.device.domain.enums.FenceRuleEnum;
import com.surge.device.domain.request.ElectricFenceDeviceBindReq;
import com.surge.device.domain.timerules.TimeRuleChecker;
import com.surge.device.service.IElectricFenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Tag(name = "电子围栏接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/electric-fence")
public class ElectricFenceController {

    private final IElectricFenceService electricFenceService;


    @Operation(summary = "围栏详情")
    @GetMapping("/{id}")
    public R queryOne(@PathVariable Long id) {
        ElectricFence electricFence = electricFenceService.queryById(id);
        return R.ok(electricFence);
    }

    @Operation(summary = "围栏列表")
    @GetMapping("/list")
    public R queryList(String orgIds, Integer status) {
        QueryWrapper<ElectricFence> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(orgIds)) {
            String[] orgIdList = orgIds.split("\\,");
            if(orgIdList != null || orgIdList.length > 0) {
                queryWrapper.in("org_id", orgIdList);
            }
        }
        queryWrapper.eq(status != null, "status", status);
        List<ElectricFence> list = electricFenceService.queryList(queryWrapper);
        return R.ok(list);
    }

    @Operation(summary = "围栏分页")
    @GetMapping("/page")
    public R queryPage(String orgIds, String name, Integer ruleType, PageInfo<ElectricFence> page) {
        QueryWrapper<ElectricFence> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(orgIds)) {
            String[] orgIdList = orgIds.split("\\,");
            if(orgIdList != null || orgIdList.length > 0) {
                queryWrapper.in("org_id", orgIdList);
            }
        }
        queryWrapper.eq(ruleType != null, "rule_type", ruleType);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        PageInfo<ElectricFence> list = electricFenceService.queryPage(queryWrapper, page);
        return R.ok(list);
    }

    @Operation(summary = "新建围栏")
    @PostMapping("/create")
    public R create(@RequestBody ElectricFence electricFence) {
        if(StringUtils.isEmpty(electricFence.getOrgId())) {
            throw new ServiceException("组织机构Id不能为空");
        }
        // 判断规则是否符合预期，线状围栏允许设置轨迹偏离规则，面状围栏允许设置禁止进入、禁止离开规则
        FenceTypeEnum fenceTypeEnum = FenceTypeEnum.valueOf(electricFence.getFenceType());
        FenceRuleEnum fenceRuleEnum = FenceRuleEnum.valueOf(electricFence.getRuleType());
        if(!Objects.equals(fenceRuleEnum.getFenceTypeEnum().getType(), fenceTypeEnum.getType())) {
            throw new ServiceException("围栏规则与围栏类型不匹配");
        }

//        try {
//            TimeRuleChecker.validateJsonRules(electricFence.getTimeRange().toString());
//        } catch (Exception e) {
//            throw new ServiceException(e.getMessage());
//        }

        // 校验geoJson格式
        try {
            GeomUtils.validateGeoJSON(electricFence.getGeoJson().toString());
        } catch (Exception e) {
            throw new ServiceException("围栏范围解析失败");
        }
        electricFence.setStatus(0); // 有效
        electricFence.setDelFlag(0); // 未删除
        electricFenceService.createElectricFence(electricFence);
        return R.ok();
    }

    @Operation(summary = "更新围栏")
    @PostMapping("/update")
    public R update(@RequestBody ElectricFence electricFence) {
        // 判断规则是否符合预期，线状围栏允许设置轨迹偏离规则，面状围栏允许设置禁止进入、禁止离开规则
        FenceTypeEnum fenceTypeEnum = FenceTypeEnum.valueOf(electricFence.getFenceType());
        FenceRuleEnum fenceRuleEnum = FenceRuleEnum.valueOf(electricFence.getRuleType());
        if(!Objects.equals(fenceRuleEnum.getFenceTypeEnum().getType(), fenceTypeEnum.getType())) {
            throw new ServiceException("围栏规则与围栏类型不匹配");
        }

//        try {
//            TimeRuleChecker.validateJsonRules(electricFence.getTimeRange().toString());
//        } catch (Exception e) {
//            throw new ServiceException(e.getMessage());
//        }

        // 校验geoJson格式
        try {
            GeomUtils.validateGeoJSON(electricFence.getGeoJson().toString());
        } catch (Exception e) {
            throw new ServiceException("围栏范围解析失败");
        }
        electricFenceService.updateElectricFence(electricFence);
        return R.ok();
    }

    @Operation(summary = "删除围栏")
    @DeleteMapping("/{id}")
    public R remove(@PathVariable Long id) {
        electricFenceService.removeElectricFence(id);
        return R.ok();
    }

    @Operation(summary = "绑定设备关联关系")
    @GetMapping("/device")
    public R getDeviceList(Long fenceId, PageInfo<DeviceInstance> page) {
        PageInfo<DeviceInstance> pageInfo = electricFenceService.queryFenceDevicePage(fenceId, page);
        return R.ok(pageInfo);
    }

    @Operation(summary = "绑定设备关联关系")
    @PostMapping("/device")
    public R addDevice(@RequestBody ElectricFenceDeviceBindReq electricFenceDeviceBindReq) {
        electricFenceService.addElectricFenceDevices(electricFenceDeviceBindReq);
        return R.ok();
    }

    @Operation(summary = "删除设备关联关系")
    @DeleteMapping("/device")
    public R removeDevice(@RequestBody ElectricFenceDeviceBindReq electricFenceDeviceBindReq) {
        electricFenceService.deleteElectricFenceDevices(electricFenceDeviceBindReq);
        return R.ok();
    }

}
