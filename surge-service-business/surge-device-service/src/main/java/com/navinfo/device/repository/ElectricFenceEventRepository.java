package com.surge.device.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.device.domain.entity.ElectricFenceEvent;
import org.springframework.stereotype.Repository;

@Repository
public class ElectricFenceEventRepository extends CrudRepository<ElectricFenceEventMapper, ElectricFenceEvent> {


}
