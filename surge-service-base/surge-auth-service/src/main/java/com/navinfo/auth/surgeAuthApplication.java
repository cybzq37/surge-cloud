package com.surge.auth;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 认证授权中心
 *
 * @author lichunqing
 */
@EnableDubbo
@SpringBootApplication(scanBasePackages = "com.surge", exclude = {DataSourceAutoConfiguration.class})
public class surgeAuthApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(surgeAuthApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  认证授权中心启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
