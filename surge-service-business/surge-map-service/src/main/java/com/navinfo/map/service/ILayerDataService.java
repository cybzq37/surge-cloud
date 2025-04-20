package com.surge.map.service;

import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.map.domain.entity.LayerData;

import java.util.List;

public interface ILayerDataService {

    List<LayerData> queryLayerDataList(Long layerId);

    PageInfo<LayerData> queryLayerDataPage(Long layerId, PageInfo<LayerData> page);

    void saveOrUpdate(LayerData layerData);

    void batchSave(Long layerId, List<LayerData> layerDataList);

    void deleteByIds(List<Long> ids);

    void deleteAllByLayerId(Long layerId);

}
