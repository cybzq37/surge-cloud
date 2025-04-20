package com.surge.device.gnss.nmea;

import com.surge.device.gnss.config.GnssConfiguration;
import com.surge.device.gnss.config.NmeaProperties;
import com.surge.device.gnss.config.PropsKey;
import com.surge.device.gnss.nmea.netty.NmeaNettyTcpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.surge.device.gnss.nmea")
@ConditionalOnExpression("'${gnss.nmea}'.length() > 0")
public class NmeaInitializer {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public Map<PropsKey, NmeaNettyTcpClient> nmeaNettyTcpClients(GnssConfiguration gnssConfiguration){
        Map<PropsKey, NmeaNettyTcpClient> nmeaNettyTcpClients = new HashMap<>();
        for(NmeaProperties properties: gnssConfiguration.getNmea()){
            PropsKey key = new PropsKey(properties.getEpid(), properties.getName(), properties.getHost(), properties.getPort());
            NmeaNettyTcpClient nmeaNettyTcpClient = new NmeaNettyTcpClient(properties);
            nmeaNettyTcpClients.put(key, nmeaNettyTcpClient);
        }
        return nmeaNettyTcpClients;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().equals(applicationContext)) {
            Map<PropsKey, NmeaNettyTcpClient> nmeaNettyTcpClients = applicationContext.getBean("nmeaNettyTcpClients", Map.class);
            for (PropsKey key : nmeaNettyTcpClients.keySet()) {
                nmeaNettyTcpClients.get(key).init();
                nmeaNettyTcpClients.get(key).connect();
            }
        }
    }
}
