package com.surge.map.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.surge.common.mybatis.pagination.PageInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.map.domain.entity.LayerData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 图层数据
 *
 * @author lichunqing
 */
public interface LayerDataMapper extends BaseMapperPlus<LayerDataMapper, LayerData, LayerData> {

    List<LayerData> selectList(Map<String, Object> fieldInfo,
                               @Param(Constants.WRAPPER) Wrapper<LayerData> queryWrapper);

    List<LayerData> selectList(Map<String, Object> fieldInfo,
                               @Param(Constants.WRAPPER) Wrapper<LayerData> queryWrapper,
                               @Param("page") PageInfo<LayerData> page);
}
