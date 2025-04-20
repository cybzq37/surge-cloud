package com.surge.system.runner;

import com.surge.system.service.ISysDictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SystemApplicationRunner implements ApplicationRunner {

    private final ISysDictService dictService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        dictService.loadingDictCache();
        log.info("加载字典缓存数据成功");
    }

}
