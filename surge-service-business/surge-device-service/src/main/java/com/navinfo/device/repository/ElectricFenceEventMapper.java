package com.surge.device.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.common.mybatis.pagination.PageInfo;
import com.surge.device.domain.bean.ElectricFenceEventVO;
import com.surge.device.domain.entity.ElectricFenceEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 电子围栏事件表 数据层
 *
 * @author lichunqing
 */
@Mapper
public interface ElectricFenceEventMapper extends BaseMapperPlus<ElectricFenceEventMapper, ElectricFenceEvent, ElectricFenceEvent> {

    List<ElectricFenceEventVO> selectListVO(@Param(Constants.WRAPPER) Wrapper<ElectricFenceEventVO> queryWrapper);

    PageInfo<ElectricFenceEventVO> selectPageVO(@Param(Constants.WRAPPER) Wrapper<ElectricFenceEventVO> queryWrapper,
                                                @Param("page") PageInfo<ElectricFenceEventVO> page);
}
