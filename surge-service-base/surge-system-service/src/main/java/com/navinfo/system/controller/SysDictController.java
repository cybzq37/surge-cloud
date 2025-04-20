package com.surge.system.controller;

import com.surge.common.core.result.R;
import com.surge.system.domain.entity.SysDict;
import com.surge.system.service.ISysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "字典管理接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dict")
public class SysDictController {

    private final ISysDictService dictService;

    @Operation(summary = "查询字典列表")
    @Parameters({
            @Parameter(name = "pid", description = "字典上级Id", in = ParameterIn.PATH),
            @Parameter(name = "type", description = "字典类型", in = ParameterIn.PATH)
    })
    @GetMapping("/list")
    public R<List<SysDict>> list(Long pid, String type) {
        return R.ok(dictService.selectDictList(pid, type));
    }

}
