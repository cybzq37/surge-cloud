//package com.surge.gateway.filter;
//
//import cn.hutool.core.lang.Dict;
//import com.surge.common.core.utils.JsonUtils;
//import com.surge.common.core.utils.StringUtils;
//import com.surge.gateway.utils.WebFluxUtils;
//import com.surge.system.api.RemoteCaptchaService;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//
///**
// * 验证码过滤器
// *
// * @author lichunqing
// */
//@Component
//public class ValidateCodeFilter extends AbstractGatewayFilterFactory<Object> {
//    private final static String[] VALIDATE_URL = new String[]{"/auth/login"};
//
//    private static final String CODE = "code";
//    private static final String UUID = "uuid";
//
//    @DubboReference
//    private RemoteCaptchaService remoteCaptchaService;
//
//    @Override
//    public GatewayFilter apply(Object config) {
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//
//            // 非登录/注册请求或验证码关闭，不处理
//            if (!StringUtils.equalsAnyIgnoreCase(request.getURI().getPath(), VALIDATE_URL)) {
//                return chain.filter(exchange);
//            }
//
//            try {
//                String rspStr = WebFluxUtils.resolveBodyFromCacheRequest(exchange);
//                Dict obj = JsonUtils.parseMap(rspStr);
//                remoteCaptchaService.checkCaptcha(obj.getStr(CODE), obj.getStr(UUID));
//            } catch (Exception e) {
//                return WebFluxUtils.webFluxResponseWriter(exchange.getResponse(), e.getMessage());
//            }
//            return chain.filter(exchange);
//        };
//    }
//
//}
