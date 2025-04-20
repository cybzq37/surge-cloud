package com.surge.device.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.device.domain.entity.SatelliteInfo;
import org.springframework.stereotype.Repository;

@Repository
public class SatelliteInfoRepository extends CrudRepository<SatelliteInfoMapper, SatelliteInfo> {

}
