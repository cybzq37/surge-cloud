package com.surge.device.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.device.domain.entity.ShipTrace;
import org.springframework.stereotype.Repository;

@Repository
public class ShipTraceRepository extends CrudRepository<ShipTraceMapper, ShipTrace> {

}
