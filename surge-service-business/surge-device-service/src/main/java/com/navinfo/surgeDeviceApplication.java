package com.surge;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 系统模块
 *
 * @author lichunqing
 */
@EnableScheduling
@EnableDubbo
@SpringBootApplication(scanBasePackages = "com.surge")
public class surgeDeviceApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(surgeDeviceApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
