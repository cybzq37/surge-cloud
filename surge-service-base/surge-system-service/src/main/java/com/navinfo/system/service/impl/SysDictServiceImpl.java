package com.surge.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.surge.common.core.utils.StringUtils;
import com.surge.system.domain.entity.SysDict;
import com.surge.system.repository.SysDictMapper;
import com.surge.system.service.ISysDictService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@DubboService
@Service
public class SysDictServiceImpl implements ISysDictService {

    private final SysDictMapper sysDictMapper;

    @Override
    public List<SysDict> selectDictList(Long pid,
                                        String type) {
        List<SysDict> list = sysDictMapper.selectList(new LambdaQueryWrapper<SysDict>()
                .eq(Objects.nonNull(pid), SysDict::getPid, pid)
                .eq(StringUtils.isNotEmpty(type), SysDict::getType, type)
                .orderByAsc(SysDict::getSort));
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public SysDict selectByCode(String code) {
        SysDict sysDict = sysDictMapper.selectOne(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getCode, code));
        return sysDict;
    }

    @Override
    public SysDict selectByTypeAndValue(String type, String value) {
        SysDict sysDict = sysDictMapper.selectOne(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getType, type)
                .eq(SysDict::getValue, value)
                .eq(SysDict::getStatus, "0")
        );
        return sysDict;
    }
}
