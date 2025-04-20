package com.surge.system.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.system.domain.entity.SysFile;
import org.springframework.stereotype.Repository;

@Repository
public class SysFileRepository extends CrudRepository<SysFileMapper, SysFile> {


}
