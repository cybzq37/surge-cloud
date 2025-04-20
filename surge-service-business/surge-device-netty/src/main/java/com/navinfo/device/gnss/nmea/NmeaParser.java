package com.surge.device.gnss.nmea;

import com.surge.device.domain.entity.SatelliteInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class NmeaParser {

    public static List<SatelliteInfo> parseGSV(String lineData) {
        Pattern pattern = Pattern.compile("\\$(.*?)\\*");
        Matcher matcher = pattern.matcher(lineData);

        if (matcher.find()) {
            String data = matcher.group(1);
            String[] parts = data.split(",");
            String type = parts[0].substring(0,2);
            Date date = new Date();
            List<SatelliteInfo> satelliteInfos = new ArrayList<>();
            for(int i=0; i<(parts.length-4)/4; i++) {
                if(parts[i] != null && parts[i].length() > 0) {
                    SatelliteInfo satelliteInfo = new SatelliteInfo();
                    satelliteInfo.setType(type);
                    satelliteInfo.setNumber(parts[4+4*i]);
                    satelliteInfo.setElevation(parts[4+4*i+1]);
                    satelliteInfo.setAzimuth(parts[4+4*i+2]);
                    satelliteInfo.setRatio(parts[4+4*i+3]);
                    satelliteInfo.setUpdateTime(date);
                    satelliteInfos.add(satelliteInfo);
                }
            }
            return satelliteInfos;
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}
