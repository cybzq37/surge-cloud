package com.surge.system.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.system.domain.entity.SysOrg;
import org.springframework.stereotype.Repository;

@Repository
public class SysOrgRepository extends CrudRepository<SysOrgMapper, SysOrg> {

}
