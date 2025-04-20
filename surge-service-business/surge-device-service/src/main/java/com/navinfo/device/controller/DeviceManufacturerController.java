package com.surge.device.controller;

import com.surge.common.core.result.R;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.DeviceManufacturer;
import com.surge.device.repository.DeviceManufacturerMapper;
import com.surge.device.repository.DeviceManufacturerRepository;
import com.surge.device.service.IDeviceManufacturerService;
import com.surge.minedata.api.RemoteMinedataService;
import com.surge.minedata.domain.entity.OrgEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "设备供应商接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/manufacturer")
public class DeviceManufacturerController {

    private final IDeviceManufacturerService deviceManufacturerService;
    private final DeviceManufacturerRepository deviceManufacturerRepository;
    @DubboReference
    private RemoteMinedataService remoteMinedataService;

    @Operation(summary = "设备供应商详情接口")
    @Parameters({
            @Parameter(name = "mfid", description = "供应商Id", in = ParameterIn.PATH)
    })
    @GetMapping(value = "/{mfid}")
    public R<DeviceManufacturer> queryOne(@PathVariable Long mfid) {
        return R.ok(deviceManufacturerService.queryById(mfid));
    }

    @Operation(summary = "设备供应商列表接口")
    @GetMapping("/page")
    public R<PageInfo<DeviceManufacturer>> queryPage(String name,
                                                String orgId,
                                               String code,
                                               PageInfo<DeviceManufacturer> page) {
        PageInfo<DeviceManufacturer> result =
                deviceManufacturerRepository.getBaseMapper().selectPageVO(name, orgId, code, null, page);
        for(DeviceManufacturer o : result.getRecords()) {
            OrgEntity orgEntity = remoteMinedataService.queryById(o.getOrgId());
            if(orgEntity != null) {
                o.setOrgName(orgEntity.getName());
            }
        }
        return R.ok(result);
    }

    @Operation(summary = "设备供应商创建接口")
    @PostMapping
    public R create(@Validated @RequestBody DeviceManufacturer deviceManufacturer) {
        deviceManufacturerService.create(deviceManufacturer);
        return R.ok();
    }

    @Operation(summary = "设备供应商更新接口")
    @PutMapping
    public R update(@Validated @RequestBody DeviceManufacturer deviceManufacturer) {
        deviceManufacturerService.update(deviceManufacturer);
        return R.ok();
    }

    @Operation(summary = "设备供应商删除接口")
    @Parameters({
            @Parameter(name = "mfid", description = "设备供应商Id", in = ParameterIn.PATH)
    })
    @DeleteMapping("/{mfid}")
    public R delete(@PathVariable Long mfid) {
        deviceManufacturerService.delete(mfid);
        return R.ok();
    }
}
