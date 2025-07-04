package com.surge.device.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "config.ship")
public class ShipConfig {

    private String host;
    private String key;
    private List<String> areas;


}
