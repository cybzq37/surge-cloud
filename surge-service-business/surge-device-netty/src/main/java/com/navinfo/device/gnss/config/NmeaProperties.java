package com.surge.device.gnss.config;

import lombok.Data;

@Data
public class NmeaProperties {

    private String epid;
    private String name;
    private String host;
    private int port;
}
