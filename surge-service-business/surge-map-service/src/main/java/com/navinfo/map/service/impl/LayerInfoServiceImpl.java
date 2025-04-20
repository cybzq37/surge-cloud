package com.surge.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.surge.common.core.exception.ServiceException;
import com.surge.map.domain.entity.LayerData;
import com.surge.map.domain.entity.LayerInfo;
import com.surge.map.repository.LayerDataMapper;
import com.surge.map.repository.LayerInfoMapper;
import com.surge.map.service.ILayerInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LayerInfoServiceImpl implements ILayerInfoService {

    private final LayerInfoMapper layerInfoMapper;
    private final LayerDataMapper layerDataMapper;

    @Override
    public LayerInfo queryById(Long layerId) {
        LayerInfo layerInfo = layerInfoMapper.selectById(layerId);
        return layerInfo;
    }

    @Override
    public List<LayerInfo> queryList() {
        return layerInfoMapper.selectList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createLayerInfo(LayerInfo layerInfo) {
        if(CollectionUtils.isEmpty(layerInfo.getOrgIds())) {
            throw new ServiceException("图层组织机构信息不能为空");
        }
        layerInfoMapper.insert(layerInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateLayerInfo(LayerInfo layerInfo) {
        if (layerInfo.getId().equals(layerInfo.getPid())) {
            throw new ServiceException("修改图层失败，上级图层不能选择自己");
        }
        if(CollectionUtils.isEmpty(layerInfo.getOrgIds())) {
            throw new ServiceException("图层组织机构信息不能为空");
        }
        layerInfoMapper.updateById(layerInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeById(Long layerId) {
        if (checkIfHasChild(layerId)) {
            throw new ServiceException("存在子图层,不允许删除");
        }
        layerInfoMapper.deleteById(layerId);
        layerDataMapper.delete(new LambdaQueryWrapper<LayerData>().eq(LayerData::getLayerId, layerId));
    }

    @Override
    public boolean checkIfHasChild(Long layerId) {
        LambdaQueryWrapper<LayerInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(LayerInfo::getPid, layerId);
        return layerInfoMapper.selectCount(lqw) > 0;
    }
}
