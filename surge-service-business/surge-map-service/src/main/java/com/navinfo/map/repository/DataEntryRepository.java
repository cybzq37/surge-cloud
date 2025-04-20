package com.surge.map.repository;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.map.domain.entity.DataEntry;
import org.springframework.stereotype.Repository;

@Repository
public class DataEntryRepository extends CrudRepository<DataEntryMapper, DataEntry> {

}
