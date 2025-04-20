//package com.surge.common.log.event;
//
//import com.surge.common.core.utils.BeanCopyUtils;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.springframework.context.event.EventListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
///**
// * 异步调用日志服务
// *
// * @author lichunqing
// */
//@Component
//public class LogEventListener {
//
//    @DubboReference
//    private RemoteLogService remoteLogService;
//
//    /**
//     * 保存系统日志记录
//     */
//    @Async
//    @EventListener
//    public void saveLog(OperLogEvent operLogEvent) {
//        SysLogOperation sysLogOperation = BeanCopyUtils.copy(operLogEvent, SysLogOperation.class);
//        remoteLogService.saveLog(sysLogOperation);
//    }
//
//    @Async
//    @EventListener
//    public void saveLogininfor(LogininforEvent logininforEvent) {
//        SysLogLogin sysLogLogin = BeanCopyUtils.copy(logininforEvent, SysLogLogin.class);
//        remoteLogService.saveLogininfor(sysLogLogin);
//    }
//
//}
