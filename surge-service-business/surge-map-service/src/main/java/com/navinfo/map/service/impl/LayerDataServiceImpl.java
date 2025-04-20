package com.surge.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.surge.common.mybatis.pagination.PageInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.surge.map.domain.entity.LayerData;
import com.surge.map.repository.LayerDataMapper;
import com.surge.map.service.ILayerDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LayerDataServiceImpl implements ILayerDataService {

    private final LayerDataMapper layerDataMapper;

        @Override
    public List<LayerData> queryLayerDataList(Long layerId) {
        List<LayerData> result = layerDataMapper.selectList(new LambdaQueryWrapper<LayerData>().eq(LayerData::getLayerId, layerId));
        return result;
    }

    @Override
    public PageInfo<LayerData> queryLayerDataPage(Long layerId, PageInfo<LayerData> page) {
        LambdaQueryWrapper<LayerData> lqw = new LambdaQueryWrapper<LayerData>().eq(LayerData::getLayerId, layerId);
        List<LayerData> list = layerDataMapper.selectList(page, lqw);
        page.setRecords(list);
        return page;
    }

    @Override
    public void saveOrUpdate(LayerData layerData) {
        // TODO：处理geoJson、geom字段, 进行校验
        // TODO：处理property字段，进行校验
    }

    @Override
    public void batchSave(Long layerId, List<LayerData> layerDataList) {

    }

    @Override
    public void deleteByIds(List<Long> ids) {
        layerDataMapper.deleteBatchIds(ids);
    }

    @Override
    public void deleteAllByLayerId(Long layerId) {
        layerDataMapper.delete(new LambdaQueryWrapper<LayerData>().eq(LayerData::getLayerId, layerId));
    }


}