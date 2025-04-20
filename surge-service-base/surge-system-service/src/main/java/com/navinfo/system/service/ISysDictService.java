package com.surge.system.service;

import com.surge.system.domain.entity.SysDict;

import java.util.List;


public interface ISysDictService {

    List<SysDict> selectDictList(Long pid, String type);

    SysDict selectByCode(String code);

    SysDict selectByTypeAndValue(String type, String value);

}
