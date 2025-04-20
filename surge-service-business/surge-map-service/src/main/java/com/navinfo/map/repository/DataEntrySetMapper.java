package com.surge.map.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.map.domain.entity.DataEntrySet;
import com.surge.map.domain.entity.LayerData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 图层信息
 *
 * @author lichunqing
 */
public interface DataEntrySetMapper extends BaseMapperPlus<DataEntrySetMapper, DataEntrySet, DataEntrySet> {

    DataEntrySet selectOne(@Param("entryId") Long entryId,
                           @Param("fieldInfo") Map<String, String> fieldInfo);

    List<DataEntrySet> selectList(Map<String, Object> fieldInfo,
                                  @Param(Constants.WRAPPER) Wrapper<DataEntrySet> queryWrapper);

    @Override
    int updateById(DataEntrySet entity);
}
