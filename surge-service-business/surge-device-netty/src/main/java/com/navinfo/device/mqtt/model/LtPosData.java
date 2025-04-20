package com.surge.device.mqtt.model;

import lombok.Data;

import java.util.List;

@Data
public class LtPosData {

    private String dataType;
    private int pkgTotalNum;
    private String devNo;
    private List<LtPosDataItem> data;

    @Data
    public static class LtPosDataItem {
        private Integer pkgNo; //包编号
        private String serNo;  //消息流水号
        private Long time;     //unix时间戳，13位
        private Integer accStat; // ACC 状态 0: ACC 关;1:ACC 开
        private Integer posStat; // 定位状态 1 表示定位，0 表示未定位
        private Double lon;     // 经度
        private Double lat;     // 纬度
        private Double speed;   // 对地速度
        private Double heading; // 对地真航向,单位为度,正北是 0
        private Double alt;     // 高程
        private Integer posType; // 定位类型
        private Double netSigQ; //CSQ值,网络信号强度
        private Double vccVal;  //电压
    }
}
