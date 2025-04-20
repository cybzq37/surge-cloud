package com.surge.map.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.surge.map.domain.entity.DataEntrySet;
import org.springframework.stereotype.Repository;

@Repository
public class DataEntrySetRepository extends CrudRepository<DataEntrySetMapper, DataEntrySet> {

    public void removeByEntryId(Long entryId) {
        LambdaQueryWrapper<DataEntrySet> lambdaQueryWrapper = new QueryWrapper<DataEntrySet>().lambda();
        lambdaQueryWrapper.eq(DataEntrySet::getDataEntryId, entryId);
        super.remove(lambdaQueryWrapper);
    }
}
