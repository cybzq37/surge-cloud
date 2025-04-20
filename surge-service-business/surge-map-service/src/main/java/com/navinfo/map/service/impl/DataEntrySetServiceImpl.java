package com.surge.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.core.exception.ServiceException;
import com.surge.common.gis.util.GeomUtils;
import com.surge.map.domain.entity.DataEntrySet;
import com.surge.map.domain.entity.LayerData;
import com.surge.map.repository.DataEntrySetMapper;
import com.surge.map.service.IDataEntrySetService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class DataEntrySetServiceImpl implements IDataEntrySetService {

    private final DataEntrySetMapper dataEntrySetMapper;

    @Override
    public void save(DataEntrySet dataEntrySet) {
        try {
            GeomUtils.validateGeoJSON(dataEntrySet.getGeoJson().toPrettyString());
        } catch (Exception e) {
            throw new ServiceException("geojson格式错误");
        }
        dataEntrySetMapper.insert(dataEntrySet);
    }

    @Override
    public void batchSave(List<DataEntrySet> dataEntrySetList) {
        dataEntrySetMapper.insertBatch(dataEntrySetList);
    }

    @Override
    public List<DataEntrySet> queryList(Map<String, Object> fieldInfo,
                                        @Param(Constants.WRAPPER) Wrapper<DataEntrySet> queryWrapper) {
        return dataEntrySetMapper.selectList(fieldInfo, queryWrapper);
    }

    @Override
    public void update(DataEntrySet dataEntrySet) {
        dataEntrySetMapper.updateById(dataEntrySet);
    }
}
