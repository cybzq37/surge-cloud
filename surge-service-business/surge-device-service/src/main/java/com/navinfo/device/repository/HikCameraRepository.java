package com.surge.device.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.device.domain.entity.HikCamera;
import org.springframework.stereotype.Repository;

@Repository
public class HikCameraRepository extends CrudRepository<HikCameraMapper, HikCamera> {

}