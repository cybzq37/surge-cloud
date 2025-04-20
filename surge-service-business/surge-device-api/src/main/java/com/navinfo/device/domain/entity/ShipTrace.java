package com.surge.device.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Tag(name = "船舶轨迹信息")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@TableName("nc_device.ship_trace")
public class ShipTrace {

    @Schema(description = "定位数据生成时间（精确到毫秒）", type = "Long")
    private Date timestamp;

    @JsonProperty("ShipID")
    private long shipId;

    @JsonProperty("From")
    @TableField("sfrom")
    private int from;

    @JsonProperty("mmsi")
    private long mmsi;

    // 船舶类型
    @JsonProperty("shiptype")
    private int shipType;

    @JsonProperty("imo")
    private long imo;

    @JsonProperty("name")
    private String name;

    // 呼号
    @JsonProperty("callsign")
    private String callSign;

    @JsonProperty("length")
    private int length;

    @JsonProperty("width")
    private int width;

    // 左舷距
    @JsonProperty("left")
    @TableField("sleft")
    private int left;

    // 尾距
    @JsonProperty("trail")
    private int trail;

    //吃水
    @JsonProperty("draught")
    private int draught;

    //目的地港口
    @JsonProperty("dest")
    private String dest;

    //标准化后的目的地港口
    @JsonProperty("dest_std")
    private String destStd;

    //
    @JsonProperty("destcode")
    private String destcode;

    //预到时间
    @JsonProperty("eta")
    private String eta;

    //标准化后的预到时间
    @JsonProperty("eta_std")
    private Date etaStd;

    //航行状态
    @JsonProperty("surgestat")
    private int surgestat;

    @JsonProperty("lat")
    private long lat;

    @JsonProperty("lon")
    private long lon;

    //船速，毫米/秒
    @JsonProperty("sog")
    private int sog;

    //船迹向
    @JsonProperty("cog")
    private int cog;

    //船首向
    @JsonProperty("hdg")
    private int hdg;

    //船向率
    @JsonProperty("rot")
    private int rot;

    //数据更新速率
    @JsonProperty("lasttime")
    private long lasttime;

}