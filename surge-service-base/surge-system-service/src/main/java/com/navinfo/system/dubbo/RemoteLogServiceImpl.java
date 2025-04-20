//package com.surge.system.dubbo;
//
//import lombok.RequiredArgsConstructor;
//import org.apache.dubbo.config.annotation.DubboService;
//import org.springframework.stereotype.Service;
//
///**
// * 操作日志记录
// *
// * @author lichunqing
// */
//@RequiredArgsConstructor
//@Service
//@DubboService
//public class RemoteLogServiceImpl implements RemoteLogService {
//
//    private final ISysOperLogService operLogService;
//    private final ISysLogininforService logininforService;
//
//    @Override
//    public Boolean saveLog(SysLogOperation sysLogOperation) {
//        return operLogService.insertOperlog(sysLogOperation) > 0;
//    }
//
//    @Override
//    public Boolean saveLogininfor(SysLogLogin sysLogLogin) {
//        return logininforService.insertLogininfor(sysLogLogin) > 0;
//    }
//}
