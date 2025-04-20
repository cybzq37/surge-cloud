package com.surge.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.result.R;
import com.surge.common.core.utils.DateUtils;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.DeviceTrace;
import com.surge.device.repository.DeviceInstanceRepository;
import com.surge.device.service.IDeviceInstanceService;
import com.surge.minedata.api.RemoteMinedataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "设备实例接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/instance")
public class DeviceInstanceController {

    private final IDeviceInstanceService deviceInstanceService;
    private final DeviceInstanceRepository deviceInstanceRepository;
    @DubboReference
    private RemoteMinedataService remoteMinedataService;
    
    @Operation(summary = "设备实例详情接口")
    @Parameters({
            @Parameter(name = "id", description = "设备实例Id", in = ParameterIn.PATH)
    })
    @GetMapping(value = "/{id}")
    public R<DeviceInstance> queryOne(@PathVariable Long id) {
        return R.ok(deviceInstanceService.queryById(id));
    }

    @Operation(summary = "设备实例轨迹查询")
    @Parameters({
            @Parameter(name = "id", description = "设备实例轨迹查询", in = ParameterIn.PATH)
    })
    @GetMapping(value = "/trace")
    public R<List<DeviceTrace>> queryTrace(@RequestParam Long id,
                                           String start,
                                           String end) {
        Date startDate = DateUtils.parseDate(start);
        Date endDate = DateUtils.parseDate(end);
        List<DeviceTrace> deviceTraces = deviceInstanceService.queryTrace(id, startDate, endDate);
        return R.ok(deviceTraces);
    }

    @Operation(summary = "设备实例分页接口")
    @GetMapping("/page")
    public R<PageInfo<DeviceInstance>> queryPage(DeviceInstance deviceInstance,
                                              PageInfo<DeviceInstance> page) {
        return R.ok(deviceInstanceService.queryPage(deviceInstance, page));
    }

    /**
     * @param orgIds
     * @param deviceTypeIds
     * @param status
     * @param deviceTypeInclude 这个参数默认不返回全部，防止数据过多
     * @return
     */
    @Operation(summary = "设备实例分页接口")
    @GetMapping("/list")
    public R<List<DeviceInstance>> queryList(String orgIds,
                                             String deviceTypeIds,
                                             Integer status,
                                             @RequestParam(defaultValue = "none") String deviceTypeInclude) {
        List<Long> deviceTypeIdList = null;
        if (deviceTypeIds != null && !deviceTypeIds.isEmpty()) {
            try {
                // 根据逗号分割并转换为 Long 类型
                deviceTypeIdList = Arrays.stream(deviceTypeIds.split(","))
                        .map(String::trim) // 去掉可能的空格
                        .map(Long::valueOf) // 转换为 Long 类型
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                throw new ServiceException("设备类型格式不正确");
            }
        } else if(deviceTypeInclude.equals("none")) {
            return R.ok(Collections.emptyList());
        }

        List<String> orgIdList = null;
        if (orgIds != null && !orgIds.isEmpty()) {
            try {
                orgIdList = Arrays.stream(orgIds.split(",")).collect(Collectors.toList());
            } catch (NumberFormatException e) {
                throw new ServiceException("设备类型格式不正确");
            }
        } else {
            return R.ok(Collections.emptyList());
        }
        QueryWrapper<DeviceInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(orgIdList != null && orgIdList.size() > 0,"org_id", orgIdList);
        queryWrapper.in(deviceTypeIdList != null && deviceTypeIdList.size() > 0, "type_id", deviceTypeIdList);
        queryWrapper.eq(status != null, "status", status);
        List<DeviceInstance> list = deviceInstanceRepository.queryList(null, queryWrapper);
        return R.ok(list);
    }

    @Operation(summary = "设备实例创建接口")
    @PostMapping
    public R create(@Validated @RequestBody DeviceInstance deviceType) {
        deviceInstanceService.create(deviceType);
        return R.ok();
    }

    // TODO: 导入接口, 并返回excel的导入结果
    @Operation(summary = "设备实例更新接口")
    @PutMapping
    public R update(@Validated @RequestBody DeviceInstance deviceInstance) {
        deviceInstanceService.update(deviceInstance);
        return R.ok();
    }

    @Operation(summary = "设备实例删除接口")
    @Parameters({
            @Parameter(name = "id", description = "设备实例Id", in = ParameterIn.PATH)
    })
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        deviceInstanceService.delete(id);
        return R.ok();
    }

    @Operation(summary = "设备组织机构列表")
    @Parameters({
            @Parameter(name = "orgIds", description = "组织机构Id列表", in = ParameterIn.QUERY)
    })
    @GetMapping("/status/statistics")
    public R queryDeviceStatusStatistics(String orgIds) {
        List<String> orgIdList = null;
        if (orgIds != null && !orgIds.isEmpty()) {
            try {
                orgIdList = Arrays.stream(orgIds.split(","))
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                throw new ServiceException("设备类型格式不正确");
            }
        }
        List<Map> result = deviceInstanceRepository.queryDeviceStatusByType(orgIdList);
        return R.ok(result);
    }

    @GetMapping("/countByCodeAndCatelog")
    public R countByCodeAndCategory(String catelog, String orgIds) throws Exception {
        List<String> orgIdList = null;
        List<String> allChildOrgIds = null;
        if (orgIds != null && !orgIds.isEmpty()) {
            try {
                orgIdList = Arrays.stream(orgIds.split(","))
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                throw new ServiceException("设备类型格式不正确");
            }
            // 递归查询数据
            allChildOrgIds = remoteMinedataService.queryAllChildOrgIds(orgIdList);
        }
        // 查询orgIds下面的数据
        List<Map> list = deviceInstanceRepository.getBaseMapper().countByCodeAndCategory(catelog, allChildOrgIds);
        return R.ok(list);
    }

}
