package com.surge.job.service;

import com.surge.system.api.RemoteUserService;
import com.surge.system.domain.entity.SysUser;
import com.surge.system.domain.model.LoginUser;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * XxlJob 多服务调用
 *
 * @author lichunqing
 */
@Slf4j
@Service
public class MultiService {

    @DubboReference
    private RemoteUserService remoteUserService;

    /**
     * 多服务调用
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @XxlJob("multiServiceHandler")
    public void multiServiceHandler() throws Exception {
        LoginUser admin = remoteUserService.getUserInfo("admin");
        XxlJobHelper.log("XXL-JOB, multiServiceHandler result: {}", admin.toString());
        SysUser sysUser = new SysUser();
//        sysUser.setUserName("test");
//        sysUser.setNickName("test");
//        remoteUserService.registerUserInfo(sysUser);
    }

}
