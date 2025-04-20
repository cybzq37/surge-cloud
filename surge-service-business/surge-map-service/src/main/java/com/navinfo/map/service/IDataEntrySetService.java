package com.surge.map.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.map.domain.entity.DataEntrySet;
import com.surge.map.domain.entity.LayerData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface IDataEntrySetService {

    void save(DataEntrySet dataEntrySet);

    void batchSave(List<DataEntrySet> dataEntrySetList);

    List<DataEntrySet> queryList(Map<String, Object> fieldInfo,
                                 @Param(Constants.WRAPPER) Wrapper<DataEntrySet> queryWrapper);

    void update(DataEntrySet dataEntrySet);
}
