package com.surge.device.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("nc_device.device_trace")
public class DeviceTrace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "定位数据生成时间（精确到毫秒）", type = "Long")
    private Date ts;

    @Schema(description = "平台内设备唯一Id", type = "Long")
    private Long tid;

    @Schema(description = "厂商ID，由平台分配", type = "Long")
    private Long mfid;

    @Schema(description = "厂商内部设备唯一标识", type = "String")
    private String epid;

    @Schema(description = "定位数据来源 0=GPS数据，1=WIFI数据，2=基站数据，3=补交点", type = "Integer")
    private Integer source;

    @Schema(description = "海拔高度", type = "Double")
    private Double elevation;

    @Schema(description = "经度", type = "Double")
    private Double lon;

    @Schema(description = "纬度", type = "Double")
    private Double lat;

    @Schema(description = "速度，单位公里每小时", type = "Double")
    private Double speed;

    @Schema(description = "地面航向角，单位为度", type = "Double")
    private Double cog;

    @Schema(description = "关联事件", type = "String")
    private String event;

    @Schema(description = "数据标识位", type = "String")
    private Integer status;

//    @TableField(exist = false)
//    @Schema(description = "坐标误差", type = "Integer")
//    private Integer accuracy;
}
