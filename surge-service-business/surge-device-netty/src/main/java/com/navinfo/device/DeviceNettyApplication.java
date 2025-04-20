package com.surge.device;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDubbo
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.surge")
public class DeviceNettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceNettyApplication.class, args);
    }

}
