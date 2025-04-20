package com.surge.device.repository;

import com.surge.common.mybatis.core.mapper.BaseMapperPlus;
import com.surge.device.domain.entity.ShipTrace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShipTraceMapper extends BaseMapperPlus<ShipTraceMapper, ShipTrace, ShipTrace> {

    List<Map> selectMonthlyShipCountStats();
}
