package com.surge.device.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.device.domain.entity.DeviceManufacturer;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceManufacturerRepository extends CrudRepository<DeviceManufacturerMapper, DeviceManufacturer> {

}