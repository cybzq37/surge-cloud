package com.surge.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.core.result.R;
import com.surge.common.json.view.Query;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceInstance;
import com.surge.device.domain.entity.DeviceType;
import com.surge.device.repository.DeviceInstanceRepository;
import com.surge.device.repository.DeviceTypeMapper;
import com.surge.device.service.IDeviceTypeService;
import com.surge.system.api.RemoteResourceService;
import com.surge.system.domain.entity.SysResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "设备类型接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/type")
public class DeviceTypeController {

    private final IDeviceTypeService deviceTypeService;
    private final DeviceTypeMapper deviceTypeMapper;
    private final DeviceInstanceRepository deviceInstanceRepository;
    @DubboReference
    private RemoteResourceService remoteResourceService;

    @Operation(summary = "设备类型详情接口")
    @Parameters({
            @Parameter(name = "id", description = "设备类型Id", in = ParameterIn.PATH)
    })
    @GetMapping(value = "/{id}")
    public R<DeviceType> queryOne(@PathVariable Long id) {
        return R.ok(deviceTypeService.queryById(id));
    }

    @Operation(summary = "设备类型列表接口")
    @GetMapping("/page")
    public R<PageInfo<DeviceType>> queryPage(String catelog,
                                       String code,
                                       String name,
                                       String model,
                                       PageInfo<DeviceType> page) {
        return R.ok(deviceTypeService.queryPage(catelog, code, name, model, page));
    }

    @Operation(summary = "设备类型创建接口")
    @PostMapping
    public R create(@Validated @RequestBody DeviceType deviceType) {
        deviceTypeService.create(deviceType);
        return R.ok();
    }

    @Operation(summary = "设备类型更新接口")
    @PutMapping
    public R update(@Validated @RequestBody DeviceType deviceType) {
        deviceTypeService.update(deviceType);
        return R.ok();
    }

    @Operation(summary = "设备类型删除接口")
    @Parameters({
            @Parameter(name = "id", description = "设备类型Id", in = ParameterIn.PATH)
    })
    @DeleteMapping("/{id}")
    public R delete(@PathVariable Long id) {
        deviceTypeService.delete(id);
        return R.ok();
    }

    @Operation(summary = "设备类型列表接口")
    @GetMapping("/list")
    public R queryList() {
        List<DeviceType> list = deviceTypeService.queryAll();
        return R.ok(list);
    }


}
