package com.surge.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.core.result.R;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.entity.SatelliteInfo;
import com.surge.device.repository.SatelliteInfoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Tag(name = "卫星信息接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/satellite")
public class SatelliteInfoController {

    private final SatelliteInfoRepository satelliteInfoRepository;

    @Operation(summary = "设备类型列表接口")
    @GetMapping("/page")
    public R<PageInfo<SatelliteInfo>> queryPage(String orgIds,
                                                PageInfo<SatelliteInfo> page) {
        QueryWrapper<SatelliteInfo> queryWrapper = new QueryWrapper<>();
//        Date now = new Date();
//        queryWrapper.between("update_time", DateUtils.addDays(now, -1), now);
        if(StringUtils.isNotEmpty(orgIds)) {
            String[] orgIdList = orgIds.split("\\,");
            if(orgIdList != null || orgIdList.length > 0) {
                queryWrapper.in("org_id", orgIdList);
            }
        }
        return R.ok(satelliteInfoRepository.page(page, queryWrapper));
    }

    @Operation(summary = "设备类型列表接口")
    @GetMapping("/list")
    public R<List<SatelliteInfo>> queryList(String orgIds) {
        QueryWrapper<SatelliteInfo> queryWrapper = new QueryWrapper<>();
//        Date now = new Date();
//        queryWrapper.between("update_time", DateUtils.addDays(now, -1), now);
        if(StringUtils.isNotEmpty(orgIds)) {
            String[] orgIdList = orgIds.split("\\,");
            if(orgIdList != null || orgIdList.length > 0) {
                queryWrapper.in("org_id", orgIdList);
            }
        }
        return R.ok(satelliteInfoRepository.list(queryWrapper));
    }

}
