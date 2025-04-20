package com.surge.map.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.surge.common.mybatis.pagination.PageInfo;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.map.domain.entity.DataEntrySet;
import com.surge.map.domain.entity.LayerData;
import org.gdal.ogr.Layer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class LayerDataRepository extends CrudRepository<LayerDataMapper, LayerData> {

    public List<LayerData> selectList(Long layerId, Map<String, Object> fieldInfo) {
        LambdaQueryWrapper<LayerData> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(LayerData::getLayerId, layerId);
        return getBaseMapper().selectList(fieldInfo, lambdaQueryWrapper);
    }

    public PageInfo<LayerData> selectPage(Long layerId, Map<String, Object> fieldInfo, PageInfo<LayerData> page) {
        LambdaQueryWrapper<LayerData> lambdaQueryWrapper = new LambdaQueryWrapper<LayerData>().eq(LayerData::getLayerId, layerId);
        lambdaQueryWrapper.eq(LayerData::getLayerId, layerId);
        List<LayerData> list = getBaseMapper().selectList(fieldInfo, lambdaQueryWrapper, page);
        page.setRecords(list);
        return page;
    }

}
