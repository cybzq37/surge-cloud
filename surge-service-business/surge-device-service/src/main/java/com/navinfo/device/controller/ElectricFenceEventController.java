package com.surge.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.surge.common.core.result.R;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.bean.ElectricFenceEventVO;
import com.surge.device.domain.entity.ElectricFenceEvent;
import com.surge.device.repository.ElectricFenceEventRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "电子围栏事件接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/electric-fence-event")
public class ElectricFenceEventController {

    private final ElectricFenceEventRepository electricFenceEventRepository;

    @Operation(summary = "事件分页数据")
    @GetMapping("/page")
    public R<PageInfo<ElectricFenceEventVO>> queryPage(ElectricFenceEventVO electricFenceEventVO,
                                                       PageInfo<ElectricFenceEventVO> page) {
        QueryWrapper<ElectricFenceEventVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(electricFenceEventVO.getDeviceId() != null, "dd.device_id", electricFenceEventVO.getDeviceId());
        queryWrapper.eq(electricFenceEventVO.getEventType() != null, "ee.event_type", electricFenceEventVO.getEventType());
        PageInfo<ElectricFenceEventVO> result = electricFenceEventRepository.getBaseMapper().selectPageVO(queryWrapper, page);
        return R.ok(result);
    }
}