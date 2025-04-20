package com.surge.device.gnss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.core.result.R;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.SatelliteInfo;
import com.surge.device.gnss.service.SatelliteInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "卫星信息接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/satellite")
public class SatelliteController {

    private final SatelliteInfoService satelliteInfoService;

    @GetMapping("/crontask")
    public R crontask() {
        satelliteInfoService.cronTask();
        return R.ok();
    }

    @GetMapping("/cache/org")
    public R orgCache() {
        return R.ok(SatelliteInfoService.ORG_INFO);
    }

    @GetMapping("/cache/satellite")
    public R satelliteCache() {
        return R.ok(SatelliteInfoService.SATELLITE_INFO);
    }

}
