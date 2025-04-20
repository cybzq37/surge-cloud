package com.surge.system.service.impl;

import com.surge.common.json.plugins.sensitive.SensitiveService;
import com.surge.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;

@Service
public class SysSensitiveServiceImpl implements SensitiveService {

    /**
     * 是否脱敏
     */
    @Override
    public boolean isSensitive() {
        return !LoginHelper.isRoot();
    }

}
