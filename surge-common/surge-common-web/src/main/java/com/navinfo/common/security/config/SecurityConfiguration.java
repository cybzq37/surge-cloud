package com.surge.common.security.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.util.SaResult;
import com.surge.common.core.constant.HttpStatus;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 权限安全配置
 *
 * @author lichunqing
 */
@AutoConfiguration
public class SecurityConfiguration implements WebMvcConfigurer {

    /**
     * 注册sa-token的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    /**
     * 校验是否从网关转发
     */
    @Bean
    public SaServletFilter getSaServletFilter(IgnoreWhiteProperties ignoreWhite) {
        return new SaServletFilter()
            .addInclude("/**")
            .addExclude("/actuator/**", "/v3/api-docs/**", "/webjars/**", "/swagger-ui/**", "/doc.html", "/swagger-ui.html")
            // 鉴权方法：每次访问进入
            .setAuth(obj -> {
                // 登录校验 -- 拦截所有路由
                SaRouter.match("/**")
                        .notMatch(ignoreWhite.getWhites())
                        .check(r -> {
                            if (SaManager.getConfig().getCheckSameToken()) {
                                SaSameUtil.checkCurrentRequestToken();
                            }
                        });
            }).setError(e -> SaResult.error("认证失败，无法访问系统资源").setCode(HttpStatus.UNAUTHORIZED));

//            .setAuth(obj -> {
//                if (SaManager.getConfig().getCheckSameToken()) {
//                    SaSameUtil.checkCurrentRequestToken();
//                }
//            })
//            .setError(e -> SaResult.error("认证失败，无法访问系统资源").setCode(HttpStatus.UNAUTHORIZED));
    }

}