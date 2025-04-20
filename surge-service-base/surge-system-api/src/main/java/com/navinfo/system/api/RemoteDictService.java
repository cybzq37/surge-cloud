package com.surge.system.api;

import com.surge.system.domain.entity.SysDict;

/**
 * 字典服务
 *
 * @author zuoqiu
 */
public interface RemoteDictService {

    SysDict selectByTypeAndValue(String type, String value);

}
