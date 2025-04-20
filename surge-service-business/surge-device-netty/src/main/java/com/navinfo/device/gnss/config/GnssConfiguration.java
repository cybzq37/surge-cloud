package com.surge.device.gnss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "gnss")
public class GnssConfiguration {

    private List<NmeaProperties> nmea;
}
