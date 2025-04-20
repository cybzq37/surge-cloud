package com.surge.map.controller;

import com.surge.common.json.view.Create;
import com.surge.common.json.view.Query;
import com.surge.common.mybatis.pagination.PageInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.core.result.R;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.core.utils.tree.TreeBuildUtils;
import com.surge.common.satoken.utils.LoginHelper;
import com.surge.map.domain.entity.LayerData;
import com.surge.map.domain.entity.LayerInfo;
import com.surge.map.repository.LayerDataRepository;
import com.surge.map.service.ILayerDataService;
import com.surge.map.service.ILayerInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Tag(name = "图层接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/layer")
public class LayerController {

    private final ILayerInfoService layerInfoService;
    private final ILayerDataService layerDataService;
    private final LayerDataRepository layerDataRepository;

    @JsonView({Create.class})
    @Operation(summary = "图层创建接口")
    @PostMapping
    public R createLayerInfo(@Validated @RequestBody LayerInfo layerInfo) {
        layerInfoService.createLayerInfo(layerInfo);
        return R.ok();
    }

    @Operation(summary = "图层详情接口")
    @Parameters({
            @Parameter(name = "layerId", description = "图层详情接口", in = ParameterIn.PATH)
    })
    @GetMapping(value = "/{layerId}")
    public R<LayerInfo> getLayerInfo(@PathVariable Long layerId) {
        return R.ok(layerInfoService.queryById(layerId));
    }

    @Operation(summary = "图层更新接口")
    @PutMapping
    public R updateLayerInfo(@Validated @RequestBody LayerInfo layerInfo) {
        layerInfoService.updateLayerInfo(layerInfo);
        return R.ok();
    }

    @Operation(summary = "图层树形接口接口")
    @GetMapping("/tree")
    public R tree() {
        Long userId = LoginHelper.getUserId();
        List<LayerInfo> layerInfos = layerInfoService.queryList();
        if (CollectionUtils.isEmpty(layerInfos)) {
            return R.ok(Collections.EMPTY_LIST);
        }
        List result = TreeBuildUtils.build(layerInfos, (layerInfo, tree) ->
                tree.setId(layerInfo.getId())
                        .setParentId(layerInfo.getPid())
                        .setName(layerInfo.getName())
                        .setWeight(layerInfo.getSort()));
        return R.ok(result);
    }

    @Operation(summary = "图层删除接口")
    @Parameters({
            @Parameter(name = "id", description = "菜单Id", in = ParameterIn.PATH)
    })
    @DeleteMapping("/{id}")
    public R delete(@PathVariable("id") Long id) {
        if (layerInfoService.checkIfHasChild(id)) {
            throw new ServiceException("存在子图层,不允许删除");
        }
        layerInfoService.removeById(id);
        return R.ok();
    }

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * 图层数据相关接口
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */
    @Operation(summary = "获取图层数据列表接口")
    @Parameters({
            @Parameter(name = "layerId", description = "图层数据列表接口", in = ParameterIn.PATH)
    })
    @JsonView(Query.class)
    @GetMapping(value = "/{layerId}/data/list")
    public R<List<LayerData>> getLayerDataList(@PathVariable("layerId") Long layerId,
                                            @RequestParam(required = false) Map<String, Object> fieldInfo) {
        return R.ok(layerDataRepository.selectList(layerId, fieldInfo));
    }

    @Operation(summary = "获取图层数据分页接口")
    @Parameters({
            @Parameter(name = "layerId", description = "图层数据分页接口", in = ParameterIn.PATH)
    })
    @JsonView(Query.class)
    @GetMapping(value = "/{layerId}/data/page")
    public R<PageInfo<LayerData>> getLayerDataPage(@PathVariable("layerId") Long layerId,
                                                   @RequestParam(required = false) Map<String, Object> fieldInfo,
                                                   PageInfo<LayerData> page) {
        PageInfo result = layerDataRepository.selectPage(layerId, fieldInfo, page);
        return R.ok(result);
    }

    @Operation(summary = "新增图层数据")
    @PostMapping(value = "/data")
    public R createLayerData(@RequestBody LayerData layerData) {
        layerDataService.saveOrUpdate(layerData);
        return R.ok();
    }

    @Operation(summary = "更改图层数据")
    @PutMapping(value = "/data")
    public R updateLayerData(@RequestBody LayerData layerData) {
        layerDataService.saveOrUpdate(layerData);
        return R.ok();
    }

//    @Operation(summary = "批量导入图层数据")
//    @PostMapping(value = "/{layerId}/data/csv")
//    public R importLayerDataFromCSV() {
//    }

    @Operation(summary = "删除图层数据")
    @DeleteMapping(value = "/data")
    public R deleteLayerData(@RequestBody List<Long> ids) {
        layerDataService.deleteByIds(ids);
        return R.ok();
    }

    @Operation(summary = "删除所有图层数据")
    @DeleteMapping(value = "/{layerId}/data/all")
    public R deleteAllLayerData(@PathVariable Long layerId) {
        layerDataService.deleteAllByLayerId(layerId);
        return R.ok();
    }
}
