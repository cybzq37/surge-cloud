package com.surge.system.dubbo;

import com.surge.system.api.RemoteDictService;
import com.surge.system.domain.entity.SysDict;
import com.surge.system.service.ISysDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 文件请求处理
 *
 * @author lichunqing
 */
@Slf4j
@Service
@RequiredArgsConstructor
@DubboService
public class RemoteDictServiceImpl implements RemoteDictService {

    private final ISysDictService sysDictService;

    @Override
    public SysDict selectByTypeAndValue(String type, String value) {
        return sysDictService.selectByTypeAndValue(type, value);
    }
}
