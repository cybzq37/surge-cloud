package com.surge.map.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonView;
import com.surge.common.core.result.R;
import com.surge.common.core.utils.JsonUtils;
import com.surge.common.core.utils.StringUtils;
import com.surge.common.core.utils.tree.TreeNode;
import com.surge.common.json.view.Create;
import com.surge.common.json.view.Query;
import com.surge.map.dataentry.upload.ShpFileRelease;
import com.surge.map.domain.dto.DataEntryUploadDTO;
import com.surge.map.domain.dto.DataEntryUploadDbDTO;
import com.surge.map.domain.dto.DataEntryUploadFileDTO;
import com.surge.map.domain.entity.DataEntry;
import com.surge.map.domain.entity.DataEntrySet;
import com.surge.system.api.RemoteResourceService;
import com.surge.system.domain.entity.SysOrg;
import com.surge.system.domain.entity.SysResource;
import com.surge.map.repository.DataEntryRepository;
import com.surge.map.repository.DataEntrySetRepository;
import com.surge.map.service.IDataEntryService;
import com.surge.map.service.IDataEntrySetService;
import com.surge.system.api.RemoteOrgService;
import com.surge.system.api.RemoteUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "数据入库")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/data-entry")
public class DataEntryController {

    private final IDataEntryService dataEntryService;
    private final IDataEntrySetService dataEntrySetService;
    private final DataEntryRepository dataEntryRepository;
    private final DataEntrySetRepository dataEntrySetRepository;
    private final ShpFileRelease shpFileRelease;
    @DubboReference
    private RemoteResourceService remoteResourceService;

    @DubboReference
    private RemoteUserService remoteUserService;

    @DubboReference
    private RemoteOrgService remoteOrgService;

    @JsonView({Create.class})
    @Operation(summary = "入库文件上传接口",description = "入库文件上传接口")
    @PostMapping("/upload")
    public R dataEntryUpload(@Valid DataEntryUploadDTO dataEntryUploadDTO) {
        if(StringUtils.isNotEmpty(dataEntryUploadDTO.getUploadFile())) {
            dataEntryUploadDTO.setUploadFileDTOList(JsonUtils.parseArray(dataEntryUploadDTO.getUploadFile(),
                    DataEntryUploadFileDTO.class));
        }
        if(StringUtils.isNotEmpty(dataEntryUploadDTO.getUploadDB())) {
            dataEntryUploadDTO.setUploadDbDTOList(JsonUtils.parseArray(dataEntryUploadDTO.getUploadDB(),
                    DataEntryUploadDbDTO.class));
        }
        shpFileRelease.uploadData(dataEntryUploadDTO);
        return R.ok();
    }

    @Operation(summary = "图层数据树形接口")
    @GetMapping("/tree")
    public R<List<TreeNode>> queryTree() {
        List<TreeNode> treeNodes = dataEntryService.queryTreeList();
        return R.ok(treeNodes);
    }

    @Operation(summary = "入库基础信息查询接口")
    @GetMapping("/{id}")
    public R<DataEntry> queryOne(@PathVariable("id") Long dataEntryId) {
        DataEntry dataEntry = dataEntryService.queryById(dataEntryId);
        return R.ok(dataEntry);
    }

    @Operation(summary = "入库基础信息更新接口")
    @PutMapping
    public R updateOne(@RequestBody DataEntry dataEntry) {
        dataEntryService.update(dataEntry);
        return R.ok();
    }

    @Operation(summary = "入库基础信息删除接口")
    @DeleteMapping("/{id}")
    public R deleteOne(@PathVariable("id") Long dataEntryId) {
        dataEntryService.removeById(dataEntryId);
        return R.ok();
    }

    @JsonView(Query.class)
    @Operation(summary = "入库详细数据集查询接口")
    @GetMapping("/{id}/set/list")
    public R<List<DataEntrySet>> queryDataEntrySetList(@PathVariable("id") Long dataEntryId,
                                                  @RequestParam(required = false) Map<String, Object> fieldInfo) {
        QueryWrapper<DataEntrySet> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(DataEntrySet::getDataEntryId, dataEntryId);
        queryWrapper.lambda().isNotNull(DataEntrySet::getGeom);
        List<DataEntrySet> list = dataEntrySetService.queryList(fieldInfo, queryWrapper);
        return R.ok(list);
    }

    @Operation(summary = "入库详细数据集新增接口")
    @PostMapping("/set")
    public R createDataEntrySet(@RequestBody DataEntrySet dataEntrySet) {
        dataEntrySetService.save(dataEntrySet);
        return R.ok();
    }

    @Operation(summary = "入库详细数据集更新接口")
    @PutMapping("/set")
    public R updateDataEntrySet(@RequestBody DataEntrySet dataEntrySet) {
        dataEntrySetService.update(dataEntrySet);
        return R.ok();
    }

    @Operation(summary = "入库详细数据集删除接口")
    @DeleteMapping("/set/{id}")
    public R deleteDataEntrySet(@PathVariable("id") Long id) {
        dataEntrySetRepository.removeById(id);
        return R.ok();
    }

    @JsonView({Query.class})
    @Operation(summary = "图层列表和数据接口")
    @GetMapping("/list-with-datasets")
    public R queryAllWithDatasets(@RequestParam(required = false) String orgId) {
        // 查询关联关系 type=1表示查询entry_set相关的数据
        List<SysResource> sysResources = remoteResourceService.queryByTypeAndOrgId(1, Long.valueOf(orgId));
        if(sysResources.size() == 0) {
            return R.ok(Collections.EMPTY_LIST);
        }
        // 查询图层数据
        QueryWrapper<DataEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", sysResources.stream().map(o -> o.getResourceId()).collect(Collectors.toList()));
        List<DataEntry> dataEntries = dataEntryRepository.list(queryWrapper);
        for (DataEntry dataEntry : dataEntries) {
            QueryWrapper<DataEntrySet> dataEntrySetQueryWrapper = new QueryWrapper<>();
            dataEntrySetQueryWrapper.eq("data_entry_id", dataEntry.getId());
            List<DataEntrySet> dataEntrySets = dataEntrySetService.queryList(null, dataEntrySetQueryWrapper);
            dataEntry.setDataEntrySets(dataEntrySets);
        }
        return R.ok(dataEntries);
    }
}
