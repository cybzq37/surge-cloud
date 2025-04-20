package com.surge.map.service;

import com.surge.map.domain.entity.LayerInfo;

import java.util.List;

public interface ILayerInfoService {

    LayerInfo queryById(Long layerId);

    List<LayerInfo> queryList();

    void createLayerInfo(LayerInfo layerInfo);

    void updateLayerInfo(LayerInfo layerInfo);

    void removeById(Long layerId);

    boolean checkIfHasChild(Long layerId);


}
